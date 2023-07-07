package cn.ituknown.uniqueseq.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class ApplyBlockRequest implements Serializable {

    @NotBlank
    private String seqname;

    @Min(1)
    private Long seqmax;

    @Min(1)
    private Long seqstep;

    private String seqdesc;

    private String seqgroup;
}
