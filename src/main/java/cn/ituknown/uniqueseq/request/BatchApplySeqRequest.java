package cn.ituknown.uniqueseq.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class BatchApplySeqRequest implements Serializable {

    @NotBlank
    private String seqname;

    @NotNull
    @Min(2)
    private Integer size;
}
