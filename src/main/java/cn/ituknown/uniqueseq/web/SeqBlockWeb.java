package cn.ituknown.uniqueseq.web;

import cn.ituknown.uniqueseq.request.AddBlockRequest;
import cn.ituknown.uniqueseq.request.TakeSeqRequest;
import cn.ituknown.uniqueseq.response.TakeNSeqResponse;
import cn.ituknown.uniqueseq.response.TakeSeqResponse;
import cn.ituknown.uniqueseq.service.CommonSeqService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(
        value = "/",
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class SeqBlockWeb {

    @Resource
    private CommonSeqService commonSeqService;

    @PostMapping("/api/block/add")
    public void createBlock(@Validated @RequestBody AddBlockRequest request) {

    }

    @PostMapping("/api/seq/take")
    public TakeSeqResponse seqtake(@Validated @RequestBody TakeSeqRequest request) {
        if (!commonSeqService.exist(request.getSeqname())) {
            throw new IllegalArgumentException(String.format("seqname:%s illegal, please apply seqname first", request.getSeqname()));
        }

        TakeSeqResponse response = new TakeSeqResponse();
        response.setData(commonSeqService.take(request.getSeqname()));
        return response;
    }

    @PostMapping("/api/seq/take/{n}")
    public TakeNSeqResponse seqtakeN(@PathVariable Integer n, @Validated @RequestBody TakeSeqRequest request) {
        if (n <= 0) {
            throw new IllegalArgumentException(String.format("seqname: %s take N, must be greater than zero", request.getSeqname()));
        }
        if (!commonSeqService.exist(request.getSeqname())) {
            throw new IllegalArgumentException(String.format("seqname:%s illegal, please apply seqname first.", request.getSeqname()));
        }

        TakeNSeqResponse response = new TakeNSeqResponse();
        response.setData(commonSeqService.take(request.getSeqname(), n));
        return response;
    }
}