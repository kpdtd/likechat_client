package com.app.library.vo;

import com.app.library.entity.GsonObj;

public class AppUpdateVo extends GsonObj<AppUpdateVo>
{
	private Integer versionCode;//要升级的新版本号(整数)
	private String versionName; //要显示的新版本号：不为空，则说明有要升级的版本
	private Boolean isForce;//是否强制升级  1-强制升级   0或null 不强制升级
	private String url;//app下载地址
	private String desc;//升级描述
	

	public void setIsForce(Boolean value) {
		this.isForce = value;
	}
	
	public Boolean getIsForce() {
		return this.isForce;
	}
	public void setUrl(String value) {
		this.url = value;
	}
	
	public String getUrl() {
		return this.url;
	}
	public void setDesc(String value) {
		this.desc = value;
	}
	
	public String getDesc() {
		return this.desc;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
}

