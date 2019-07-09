package com.auxing.znhy.util.meeting;

/**
 * @auther liuyy
 * @date 2018/10/31 0031 上午 10:41
 */

public class SelectSource {
    private Integer type;//选看源类型 1-终端；2-画面合成；
    private String mt_id;//源终端号(仅选看源类型为终端有效) 最大字符长度：48个字节

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMt_id() {
        return mt_id;
    }

    public void setMt_id(String mt_id) {
        this.mt_id = mt_id;
    }
}
