package com.netease.nim.uikit.miliao.vo;


import com.netease.nim.uikit.miliao.entity.GsonObj;

public class TagVo extends GsonObj<TagVo>
{
	private String identifying;
	private String tagName;
	private String pic;
	
	public String getIdentifying() {
		return identifying;
	}
	public void setIdentifying(String identifying) {
		this.identifying = identifying;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	@Override
    public String toString() {
	    return "TagVo [identifying=" + identifying + ", tagName=" + tagName + ", pic=" + pic + "]";
    }
}

