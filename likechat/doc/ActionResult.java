package com.fun.likechat.interceptor;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.web.servlet.ModelAndView;

import com.fun.likechat.constant.ErrCodeEnum;

/**
 */
public class ActionResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//返回码
	private int code;
	//返回描述
	private String codemsg;
	//返回的数据
	private Object data;
	//分页戳
	private Object stamp;
	//是否有下一页
	private Boolean nextPage;
	
	/**
	 * 返回ActionResult
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @param data - 返回的数据
	 * @param stamp - 分页戳
	 * @return ActionResult
	 */
	public static ActionResult getResult(int code , String codemsg , Object data , Object stamp){
		ActionResult result = new ActionResult();
		result.code = code;
		result.codemsg = codemsg;
		result.data = data;
		result.stamp = stamp;
		
		return result;
	}
	
	/**
	 * 返回成功
	 * @return ActionResult
	 */
	public static ActionResult success() {
		return success(null , null , null);
	}
	
	/**
	 * 返回成功
	 * @param data - 返回的数据
	 * @return ActionResult
	 */
	public static ActionResult success(Object data) {
		return success(data , null , null);
	}
	
	/**
	 * 返回成功
	 * @param data - 返回的数据
	 * @param successMsg - 自定义文案
	 * @return ActionResult
	 */
	public static ActionResult success(Object data,String successMsg) {
		 ActionResult result = success(data , null , null);
		 result.setCodemsg(successMsg);
		 return result;
	}
	
	/**
	 * 返回成功
	 * @param data - 返回的数据
	 * @param stamp - 分页戳
	 * @param nextPage - 是否有下一页
	 * @return ActionResult
	 */
	public static ActionResult success(Object data , Object stamp , Boolean nextPage){
		int code = ErrCodeEnum.success.getCode();
		
		ActionResult result = getResult(code , ErrCodeEnum.getDescByCode(code) , data , stamp);
		result.setNextPage(nextPage);
		
		return result;
	}
	
	/**
	 * 返回默认的错误失败
	 * @return - ActionResult
	 */
	public static ActionResult fail() {
		int code = ErrCodeEnum.error.getCode();
		return getResult(code , ErrCodeEnum.getDescByCode(code) , null , null);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , String codemsg) {
		return getResult(code , codemsg , null , null);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @return ActionResult
	 */
	public static ActionResult fail(int code) {
		return getResult(code , ErrCodeEnum.getDescByCode(code) , null , null);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @param data - 返回的数据
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , String codemsg , Object data) {
		return getResult(code , codemsg , data , null);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param data - 返回的数据
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , Object data) {
		return getResult(code , ErrCodeEnum.getDescByCode(code) , data , null);
	}
	
	/**
	 * 跳转
	 * @param redirectUrl - 目标链接
	 * @throws IOException 
	 */
	public static void redirect(String redirectUrl) throws IOException {
		//RequestUtils.getCurrent().getResponse().sendRedirect(redirectUrl);
	}
	
	/**
	 * 返回一个错误视图
	 * @param err - 错误原因
	 * @return ModelAndView
	 */
	public static ModelAndView errorModel(String err){
		ModelAndView model =  new ModelAndView("error");
		model.addObject("msg", err);
		
		return model;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCodemsg() {
		return codemsg;
	}

	public void setCodemsg(String codemsg) {
		this.codemsg = codemsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getStamp() {
		return stamp;
	}

	public void setStamp(Object stamp) {
		this.stamp = stamp;
	}

	public Boolean getNextPage() {
		return nextPage;
	}

	public void setNextPage(Boolean nextPage) {
		this.nextPage = nextPage;
	}

}
