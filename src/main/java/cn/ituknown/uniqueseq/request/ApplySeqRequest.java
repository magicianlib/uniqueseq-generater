package cn.ituknown.uniqueseq.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class ApplySeqRequest implements Serializable {

    @NotBlank
    private String seqname;
}
