package cn.ituknown.uniqueseq.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class BatchApplySeqResponse implements Serializable {

    private List<Long> seqList;
}
