package cn.ituknown.uniqueseq.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class TakeNSeqResponse implements Serializable {

    private List<Long> data;
}
