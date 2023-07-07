package cn.ituknown.uniqueseq.service;

import cn.ituknown.uniqueseq.enums.BlockTypeEnum;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.task.TaskExecutor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

// @Service
public class TradingSeqService extends AbstractSeqService {

    @Override
    protected List<String> seqnameList() {
        return Collections.emptyList();
    }

    @Override
    protected Function<String, Pair<Long, Long>> addBlock() {
        // TODO: Depending on the business
        return null;
    }

    @Override
    protected TaskExecutor executor() {
        // TODO: Depending on the business
        return null;
    }

    @Override
    protected BlockTypeEnum type() {
        return BlockTypeEnum.TRADING;
    }

    @Override
    protected long timerPeriod() {
        // TODO: Depending on the business
        return 0L;
    }
}
