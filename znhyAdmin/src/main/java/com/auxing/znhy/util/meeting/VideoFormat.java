package com.auxing.znhy.util.meeting;

public class VideoFormat {
	private Integer format;			 		//主视频格式1-MPEG;2-H.261;3-H.263;4-H.264_HP;5-H.264_BP;6-H.265;7-H.263+; 
	private Integer resolution;				//主视频分辨率1-QCIF;2-CIF;3-4CIF;12-720P;13-1080P;14-WCIF;15-W4CIF;16-4k;
	private Integer frame;					// 帧率 
	private Integer bitrate;				// 码率 
	
	
	public Integer getFormat() {
		return format;
	}
	public void setFormat(Integer format) {
		this.format = format;
	}
	public Integer getResolution() {
		return resolution;
	}
	public void setResolution(Integer resolution) {
		this.resolution = resolution;
	}
	public Integer getFrame() {
		return frame;
	}
	public void setFrame(Integer frame) {
		this.frame = frame;
	}
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	
	
}
