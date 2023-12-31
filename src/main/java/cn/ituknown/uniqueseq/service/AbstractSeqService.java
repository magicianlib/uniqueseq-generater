package cn.ituknown.uniqueseq.service;

import cn.ituknown.uniqueseq.enums.BlockTypeEnum;
import cn.ituknown.uniqueseq.model.SeqBlockCache;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public abstract class AbstractSeqService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSeqService.class);

    private final ReentrantLock LOCK = new ReentrantLock();

    private final Map<String, SeqBlockCache> seqnameCache = new ConcurrentHashMap<>();

    /**
     * 序列块业务类型
     */
    protected abstract BlockTypeEnum type();

    /**
     * 业务类型下序列列表
     */
    protected abstract List<String> seqnameList();

    /**
     * 指定序列取值范围
     */
    protected abstract Function<String, Pair<Long, Long>> addBlock();

    /**
     * 线程池
     */
    protected abstract TaskExecutor executor();

    /**
     * 定时同步序列块周期(ms)
     */
    protected abstract long timerPeriod();

    /**
     * 定时同步序列
     */
    @PostConstruct
    public void initTimer() {
        initCacheList(seqnameList());
        Timer timer = new Timer(type().name(), true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    initCacheList(seqnameList());
                } catch (Exception e) {
                    LOGGER.error("timer schedule initCacheList failure: {}", e.getMessage(), e);
                }
            }
        }, timerPeriod(), timerPeriod());
    }

    private void initCacheList(List<String> seqnameList) {
        removeCacheIfAbandoned(seqnameList);
        CountDownLatch countDownLatch = new CountDownLatch(seqnameList.size());
        try {
            for (String seqname : seqnameList) {
                executor().execute(() -> {
                    try {
                        LOGGER.info("initCacheIfNeed(seqname='{}')...", seqname);
                        initCacheIfNeed(seqname);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("initCacheList failure: {}", e.getMessage(), e);
        }
    }

    private void initCacheIfNeed(String seqname) {
        if (!seqnameCache.containsKey(seqname)) { // Don't use putIfAbsent
            LOCK.lock();
            try {
                if (!seqnameCache.containsKey(seqname)) {
                    LOGGER.info("initCache(seqname='{}')", seqname);
                    seqnameCache.put(seqname, new SeqBlockCache(seqname, addBlock(), executor()));
                }
            } finally {
                LOCK.unlock();
            }
        }
    }

    /**
     * 移除废弃序列块
     */
    private void removeCacheIfAbandoned(List<String> seqnameList) {
        Collection<String> subtract = CollectionUtils.subtract(seqnameCache.keySet(), seqnameList);
        LOGGER.info("removeCacheIfAbandoned, subtract={}", subtract);
        if (CollectionUtils.isNotEmpty(subtract)) {
            LOCK.lock();
            try {
                subtract = CollectionUtils.subtract(seqnameCache.keySet(), seqnameList);
                if (CollectionUtils.isNotEmpty(subtract)) {
                    for (String seqname : subtract) {
                        if (seqnameCache.containsKey(seqname)) {
                            final ReentrantReadWriteLock rw = seqnameCache.get(seqname).lock();
                            if (rw.getReadLockCount() == 0 && rw.getWriteHoldCount() == 0) {
                                LOGGER.info("seqname='{}' has been abandoned, remove!", seqname);
                                seqnameCache.remove(seqname);
                            }
                        }
                    }
                }
            } finally {
                LOCK.unlock();
            }
        }
    }

    public boolean exist(String seqname) {
        return seqnameCache.containsKey(seqname);
    }

    public Long take(String seqname) {
        initCacheIfNeed(seqname);
        SeqBlockCache block = seqnameCache.get(seqname);
        return block.take();
    }

    public List<Long> take(String seqname, int size) {
        initCacheIfNeed(seqname);
        SeqBlockCache block = seqnameCache.get(seqname);
        return block.take(size);
    }
}