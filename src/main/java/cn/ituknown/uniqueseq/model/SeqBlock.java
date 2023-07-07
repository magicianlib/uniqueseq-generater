package cn.ituknown.uniqueseq.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列块
 *
 * @author Shilin <br > mingrn97@gmail.com
 * @since 2023/07/07 14:52
 */
public class SeqBlock {

    private final AtomicLong begin;
    private final Long end;

    public SeqBlock(Pair<Long, Long> range) {
        this.begin = new AtomicLong(range.getLeft());
        this.end = range.getRight();
    }

    public Long take() {
        if (isExhausted()) {
            return null;
        }

        long seq = begin.incrementAndGet();
        if (seq <= end) {
            return seq;
        } else {
            return null;
        }
    }

    /**
     * 获取 {@code size} 个序列
     */
    public List<Long> take(int size) {
        if (isExhausted()) {
            return Collections.emptyList();
        }

        List<Long> seq = new ArrayList<>();

        long begin = this.begin.getAndAdd(size);
        long end = (begin + size);

        for (; begin < end; ++begin) {
            if (begin < this.end) {
                seq.add(begin);
            } else {
                break;
            }
        }

        return seq;
    }

    /**
     * 是否已耗尽
     */
    public boolean isExhausted() {
        return begin.get() > end;
    }
}
