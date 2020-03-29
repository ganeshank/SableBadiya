package com.sb.integration.vo;

import java.io.Serializable;

public class MediaVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long MediaId;
	private String mediaName;
	private String mediaWebpath;
	private Integer mediaTypeId;
	
	public Long getMediaId() {
		return MediaId;
	}
	public void setMediaId(Long mediaId) {
		MediaId = mediaId;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public String getMediaWebpath() {
		return mediaWebpath;
	}
	public void setMediaWebpath(String mediaWebpath) {
		this.mediaWebpath = mediaWebpath;
	}
	public Integer getMediaTypeId() {
		return mediaTypeId;
	}
	public void setMediaTypeId(Integer mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}
	@Override
	public String toString() {
		return "MediaVo [MediaId=" + MediaId + ", mediaName=" + mediaName + ", mediaWebpath=" + mediaWebpath
				+ ", mediaTypeId=" + mediaTypeId + "]";
	}
	
	
}
