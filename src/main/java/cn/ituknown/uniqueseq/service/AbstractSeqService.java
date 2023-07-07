package cn.ituknown.uniqueseq.service;

import cn.ituknown.uniqueseq.enums.BlockTypeEnum;
import cn.ituknown.uniqueseq.model.SeqBlockCache;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public abstract class AbstractSeqService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSeqService.class);

    private final ConcurrentHashMap<String, SeqBlockCache> seqnameCache = new ConcurrentHashMap<>();

    protected abstract List<String> seqnameList();

    protected abstract Function<String, Pair<Long, Long>> addBlock();

    protected abstract TaskExecutor executor();

    protected abstract BlockTypeEnum type();

    protected abstract long timerPeriod();

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

    public boolean verifySeqname(String seqname) {
        return seqnameCache.containsKey(seqname);
    }

    private void initCacheList(List<String> seqnameList) {
        CountDownLatch countDownLatch = new CountDownLatch(seqnameList.size());
        try {
            for (String seqname : seqnameList()) {
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
        if (!seqnameCache.containsKey(seqname)) {
            LOGGER.info("initCache(seqname='{}')", seqname);
            seqnameCache.put(seqname, new SeqBlockCache(seqname, addBlock(), executor()));
        }
    }

    public Long take(String seqname) {
        initCacheIfNeed(seqname);
        SeqBlockCache block = seqnameCache.get(seqname);
        return block.take();
    }

    public List<Long> take(String seqname, int sieze) {
        initCacheIfNeed(seqname);
        SeqBlockCache block = seqnameCache.get(seqname);
        return block.take(sieze);
    }
}
