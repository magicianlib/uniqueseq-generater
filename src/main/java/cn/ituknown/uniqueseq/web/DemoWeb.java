package cn.ituknown.uniqueseq.web;

import cn.ituknown.uniqueseq.request.ApplyBlockRequest;
import cn.ituknown.uniqueseq.request.ApplySeqRequest;
import cn.ituknown.uniqueseq.request.BatchApplySeqRequest;
import cn.ituknown.uniqueseq.response.ApplySeqResponse;
import cn.ituknown.uniqueseq.response.BatchApplySeqResponse;
import cn.ituknown.uniqueseq.service.CommonSeqService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/")
public class DemoWeb {

    @Resource
    private CommonSeqService commonSeqService;

    @PostMapping("/applyBlock")
    public void createBlock(@Validated @RequestBody ApplyBlockRequest request) {

    }

    @PostMapping("/applySeq")
    public ApplySeqResponse createBlock(@Validated @RequestBody ApplySeqRequest request) {
        if (!commonSeqService.verifySeqname(request.getSeqname())) {
            throw new IllegalArgumentException(String.format("seqname:%s非法, 请先申请该序列号.", request.getSeqname()));
        }

        ApplySeqResponse response = new ApplySeqResponse();
        response.setSeq(commonSeqService.take(request.getSeqname()));
        return response;
    }

    @PostMapping("/batchApplySeq")
    public BatchApplySeqResponse createBlock(@Validated @RequestBody BatchApplySeqRequest request) {
        if (!commonSeqService.verifySeqname(request.getSeqname())) {
            throw new IllegalArgumentException(String.format("seqname:%s非法, 请先申请该序列号.", request.getSeqname()));
        }

        BatchApplySeqResponse response = new BatchApplySeqResponse();
        response.setSeqList(commonSeqService.take(request.getSeqname(), request.getSize()));
        return response;
    }

}
