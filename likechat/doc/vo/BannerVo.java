package com.fun.likechat.vo;

import java.util.Date;

public class BannerVo {
    
    private String  displayName;
    private String  identifying;
    private String  visitUrl;
    private String  icon;
    
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getIdentifying() {
		return identifying;
	}
	public void setIdentifying(String identifying) {
		this.identifying = identifying;
	}
	public String getVisitUrl() {
		return visitUrl;
	}
	public void setVisitUrl(String visitUrl) {
		this.visitUrl = visitUrl;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Override
    public String toString() {
	    return "BannerVo [displayName=" + displayName + ", identifying=" + identifying + ", visitUrl=" + visitUrl + ", icon=" + icon + "]";
    }


}
