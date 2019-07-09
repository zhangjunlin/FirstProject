package com.auxing.znhy.util.meeting;

/**
 * @auther liuyy
 * @date 2018/10/31 0031 上午 10:43
 */

public class TargetSource {
    private String mt_id;//目的终端号(选看源类型为画面合成时, 必须为主席终端号) 最大字符长度：48个字节

    public String getMt_id() {
        return mt_id;
    }

    public void setMt_id(String mt_id) {
        this.mt_id = mt_id;
    }
}
