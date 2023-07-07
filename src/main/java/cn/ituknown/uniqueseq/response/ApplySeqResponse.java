package cn.ituknown.uniqueseq.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ApplySeqResponse implements Serializable {

    private Long seq;
}
