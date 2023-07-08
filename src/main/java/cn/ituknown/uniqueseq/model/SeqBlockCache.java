package cn.ituknown.uniqueseq.model;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * 序列块缓存
 *
 * @author Shilin <br > mingrn97@gmail.com
 * @since 2023/07/07 14:53
 */
public class SeqBlockCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeqBlockCache.class);

    /**
     * 安全序列块数量限制
     */
    private static final int BLOCK_MIN_LIMIT = 3;

    private static final int BLOCK_MAX_LIMIT = 16;

    /**
     * 序列块名称
     */
    private final String seqname;

    private final Function<String, Pair<Long, Long>> function;

    private final TaskExecutor executor;

    private final List<SeqBlock> blocks = new ArrayList<>();

    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    private final AtomicBoolean supplementing = new AtomicBoolean(false);

    public SeqBlockCache(String seqname, Function<String, Pair<Long, Long>> function, TaskExecutor executor) {
        this.seqname = seqname;
        this.function = function;
        this.executor = executor;
        try {
            syncSupplementBlock(); // 同步填充序列块
        } catch (Exception e) {
            LOGGER.error("syncSupplementBlock failure: {}", e.getMessage(), e);
        }
    }

    public Long take() {
        rw.readLock().lock();
        boolean exhausted = blocks.size() < BLOCK_MIN_LIMIT;
        try {
            Long result = null;
            for (SeqBlock block : blocks) {
                result = block.take();
                if (result == null || block.isExhausted()) {
                    exhausted = true;
                }
                if (result != null) {
                    break;
                }
            }
            return result;
        } finally {
            rw.readLock().unlock();
            if (exhausted) { // 异步补充序列块
                LOGGER.info("take: {} is exhausted!, asyncSupplementBlock...", seqname);
                asyncSupplementBlock();
            }
        }
    }

    public List<Long> take(int size) {
        rw.readLock().lock();
        List<Long> result = new ArrayList<>();
        boolean exhausted = blocks.size() < BLOCK_MIN_LIMIT;
        try {
            int s = size;
            for (SeqBlock block : blocks) {
                result.addAll(block.take(s));
                s = s - result.size();
                if (block.isExhausted()) {
                    exhausted = true;
                }
                if (result.size() == size) {
                    break;
                }
            }
            return result;
        } finally {
            rw.readLock().unlock();
            if (exhausted) {
                LOGGER.info("take(int): {} is exhausted!, asyncSupplementBlock...", seqname);
                asyncSupplementBlock();
            }
        }
    }

    /**
     * 异步补充序列块{@link SeqBlock}
     */
    private void asyncSupplementBlock() {
        removeExhaustedBlock();
        if (supplementing.compareAndSet(false, true)) {
            executor.execute(() -> {
                try {
                    syncSupplementBlock();
                } catch (Exception e) {
                    LOGGER.error("asyncSupplementBlock failure: {}", e.getMessage(), e);
                } finally {
                    supplementing.set(false);
                }
            });
        }
    }

    /**
     * 同步补充序列块{@link SeqBlock}
     */
    private void syncSupplementBlock() {
        do {
            // 获取新序列块范围
            Pair<Long, Long> range = function.apply(seqname);
            if (Objects.nonNull(range)) {
                supplementBlock(range);
            }
        } while (blocks.size() < BLOCK_MAX_LIMIT);
    }

    /**
     * 补充序列块
     */
    private void supplementBlock(Pair<Long, Long> range) {
        rw.writeLock().lock();
        try {
            blocks.add(new SeqBlock(range));
        } finally {
            rw.writeLock().unlock();
        }
    }

    /**
     * 移除已耗尽的Block
     */
    private void removeExhaustedBlock() {
        rw.writeLock().lock();
        try {
            blocks.removeIf(SeqBlock::isExhausted);
        } finally {
            rw.writeLock().unlock();
        }
    }

    final public ReentrantReadWriteLock lock() {
        return rw;
    }
}