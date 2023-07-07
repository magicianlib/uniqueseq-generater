package cn.ituknown.uniqueseq.service;

import cn.ituknown.uniqueseq.enums.BlockTypeEnum;
import cn.ituknown.uniqueseq.repository.SeqBlockRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Function;

@Service
public class CommonSeqService extends AbstractSeqService {

    @Resource
    private TaskExecutor taskExecutor;

    @Resource
    private SeqBlockRepository seqBlockRepository;

    @Override
    protected List<String> seqnameList() {
        return seqBlockRepository.findAllSeqname();
    }

    @Override
    protected Function<String, Pair<Long, Long>> addBlock() {
        return s -> seqBlockRepository.generateBlockRange(s);
    }

    @Override
    protected TaskExecutor executor() {
        return taskExecutor;
    }

    @Override
    protected BlockTypeEnum type() {
        return BlockTypeEnum.COMMON;
    }

    @Override
    protected long timerPeriod() {
//        return 1000 * 60 * 60; // 1 hour.
        return 1000 * 10; // 10 Second.
    }
}
