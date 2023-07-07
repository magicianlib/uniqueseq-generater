package cn.ituknown.uniqueseq.repository;

import cn.ituknown.uniqueseq.request.ApplyBlockRequest;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface SeqBlockRepository {

    /**
     * 查询所有序列
     */
    List<String> findAllSeqname();

    /**
     * 生成序列范围
     */
    Pair<Long, Long> generateBlockRange(String seqname);

    /**
     * 申请新序列块
     */
    void applySeqBlock(ApplyBlockRequest request);
}
