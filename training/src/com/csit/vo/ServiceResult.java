package com.csit.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
/**
 * @Description: action层调用service层执行的结果
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-26
 * @author lys
 * @vesion 1.0
 */
public class ServiceResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 返回的信息
	 */
	private String message;
	/**
	 * 执行是否成功
	 */
	private Boolean isSuccess;
	
	private Map<String,Object> data = new HashMap<String,Object>();
	
	public ServiceResult(){}
	
	public ServiceResult(Boolean isSuccess){
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	/**
	 * @Description: 将执行结果以json的形式，返回界面
	 * @Create: 2012-10-26 下午10:57:21
	 * @author lys
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public String toJSON() {
		JSONObject result = new JSONObject();
		result.put("message", message);
		result.put("isSuccess", isSuccess);
		result.put("data", data);
		return result.toString();
	}
	/**
	 * @Description: 添加数据
	 * @Create: 2012-12-18 下午11:08:30
	 * @author lys
	 * @update logs
	 * @param key
	 * @param value
	 */
	public void addData(String key,Object value){
		data.put(key, value);
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
