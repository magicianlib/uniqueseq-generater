package cn.ituknown.uniqueseq.repository.impl;

import cn.ituknown.uniqueseq.mapper.SeqBlockMapper;
import cn.ituknown.uniqueseq.po.SeqBlockPo;
import cn.ituknown.uniqueseq.repository.SeqBlockRepository;
import cn.ituknown.uniqueseq.request.ApplyBlockRequest;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class SeqBlockRepositoryImpl implements SeqBlockRepository {

    @Value("${biz.default.seqmax}")
    private Long defaultSeqMax;

    @Value("${biz.default.seqstep}")
    private Long defaultSeqStep;

    @Resource
    private SeqBlockMapper seqBlockMapper;

    @Override
    public List<String> findAllSeqname() {
        return seqBlockMapper.selectList(null).stream().map(SeqBlockPo::getSeqname).distinct().collect(Collectors.toList());
    }

    @Override
    public Pair<Long, Long> generateBlockRange(String seqname) {

        boolean update;
        long latestSeqMax;
        SeqBlockPo old;
        do {
            if (Objects.isNull(old = seqBlockMapper.selectById(seqname))) {
                throw new RuntimeException("cannot find `seqname` '" + seqname + "', please apply first.");
            }

            // new max seq
            latestSeqMax = (old.getSeqmax() + old.getSeqstep());

            LambdaUpdateWrapper<SeqBlockPo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(SeqBlockPo::getSeqmax, latestSeqMax).set(SeqBlockPo::getUpdateTime, LocalDateTime.now());
            updateWrapper.eq(SeqBlockPo::getSeqname, seqname).eq(SeqBlockPo::getSeqmax, old.getSeqmax());

            update = seqBlockMapper.update(null, updateWrapper) > 0;
        } while (!update); // Try again until successful.

        return Pair.of(old.getSeqmax() + 1, latestSeqMax);
    }

    @Override
    public void applySeqBlock(ApplyBlockRequest request) {
        if (Objects.isNull(request.getSeqmax())) {
            request.setSeqmax(defaultSeqMax);
        }
        if (Objects.isNull(request.getSeqstep())) {
            request.setSeqmax(defaultSeqStep);
        }

        if (Objects.nonNull(seqBlockMapper.selectById(request.getSeqname()))) {
            return;
        }

        SeqBlockPo record = new SeqBlockPo();
        record.setSeqname(record.getSeqname());
        record.setSeqmax(record.getSeqmax());
        record.setSeqstep(record.getSeqstep());
        record.setSeqdesc(record.getSeqdesc());
        record.setSeqgroup(record.getSeqgroup());

        seqBlockMapper.insert(record);
    }
}