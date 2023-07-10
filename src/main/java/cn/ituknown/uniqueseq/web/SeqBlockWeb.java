package cn.ituknown.uniqueseq.web;

import cn.ituknown.uniqueseq.request.AddBlockRequest;
import cn.ituknown.uniqueseq.request.TakeSeqRequest;
import cn.ituknown.uniqueseq.service.CommonSeqService;
import cn.ituknown.uniqueseq.result.Result;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public Result<Long> seqtake(@Validated @RequestBody TakeSeqRequest request) {
        if (!commonSeqService.exist(request.getSeqname())) {
            return Result.failure(String.format("seqname:%s illegal, please apply seqname first.", request.getSeqname()));
        }

        return Result.success(commonSeqService.take(request.getSeqname()));
    }

    @PostMapping("/api/seq/take/{n}")
    public Result<List<Long>> seqtakeN(@PathVariable Integer n, @Validated @RequestBody TakeSeqRequest request) {
        if (n <= 0) {
            return Result.failure(String.format("seqname: %s take N, must be greater than zero", request.getSeqname()));
        }
        if (!commonSeqService.exist(request.getSeqname())) {
            return Result.failure(String.format("seqname:%s illegal, please apply seqname first.", request.getSeqname()));
        }

        return Result.success(commonSeqService.take(request.getSeqname(), n));
    }
}