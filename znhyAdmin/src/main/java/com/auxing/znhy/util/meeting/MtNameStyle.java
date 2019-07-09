package com.auxing.znhy.util.meeting;

/**
 * 画面合成参数
 *
 */
public class MtNameStyle {
	private Integer font_size = 1;				//台标字体大小，默认为：10-小；1-中；2-大；
	private String font_color = "#FFFFFF";		//台标字体三原色#RGB格式，十六进制表示，默认为：#FFFFFF白色
	private Integer position = 1;				//台标显示位置，默认为1 0-左上角；1-左下角；2-右上角；3-右下角；4-底部中间；
	
	public Integer getFont_size() {
		return font_size;
	}
	public void setFont_size(Integer font_size) {
		this.font_size = font_size;
	}
	public String getFont_color() {
		return font_color;
	}
	public void setFont_color(String font_color) {
		this.font_color = font_color;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	
}
