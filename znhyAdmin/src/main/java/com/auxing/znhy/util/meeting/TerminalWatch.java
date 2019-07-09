package com.auxing.znhy.util.meeting;

/**
 * @auther liuyy
 * @date 2018/10/31 0031 上午 10:38
 */

public class TerminalWatch {
    private Integer mode;//选看模式 1-视频 2-音频
    private SelectSource src;//选看源
    private TargetSource dst;//选看目的

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public SelectSource getSrc() {
        return src;
    }

    public void setSrc(SelectSource src) {
        this.src = src;
    }

    public TargetSource getDst() {
        return dst;
    }

    public void setDst(TargetSource dst) {
        this.dst = dst;
    }
}
