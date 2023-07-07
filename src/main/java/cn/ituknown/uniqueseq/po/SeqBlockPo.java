package cn.ituknown.uniqueseq.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("seq_block")
public class SeqBlockPo implements Serializable {

    private static final long serialVersionUID = 5320388649480031296L;

    @TableId("seq_name")
    private String seqname;

    @TableField("seq_max")
    private Long seqmax;

    @TableField("seq_step")
    private Long seqstep;

    @TableField("seq_desc")
    private String seqdesc;

    @TableField("seq_group")
    private String seqgroup;

    @TableField("add_time")
    private LocalDateTime addTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
