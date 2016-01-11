package com.csit.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;

/**
 * @Description: 接受界面数据时，对接受的参数进行的处理
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-26
 * @author lys
 * @vesion 1.0
 */
public class ParameterConverter extends DefaultTypeConverter {

	private static final DateFormat[] ACCEPT_DATE_FORMATS_YMD = {
			new SimpleDateFormat("yyyy-MM-dd"),
			new SimpleDateFormat("dd/MM/yyyy"),
			new SimpleDateFormat("yyyy/MM/dd") }; // 支持转换的日期格式
	
	private static final DateFormat[] ACCEPT_DATE_FORMATS_YM = {
		new SimpleDateFormat("yyyy-MM"),
		new SimpleDateFormat("MM/yyyy"),
		new SimpleDateFormat("yyyy/MM") }; // 支持转换的日期格式

	@SuppressWarnings("rawtypes")
	public Object convertValue(Map context, Object value, Class toType) {
		String dataString = null;
		String[] params = (String[]) value;
		dataString = params[0];// 获取日期的字符串
		
		if (toType == Date.class) { // 浏览器向服务器提交时，进行String to Date的转换
			if(dataString.length()==10){
				for (DateFormat format : ACCEPT_DATE_FORMATS_YMD) {
					try {
						return format.parse(dataString);// 遍历日期支持格式，进行转换
					} catch (Exception e) {
						continue;
					}
				}
			}else if(dataString.length()==7){
				for (DateFormat format : ACCEPT_DATE_FORMATS_YM) {
					try {
						return format.parse(dataString);// 遍历日期支持格式，进行转换
					} catch (Exception e) {
						continue;
					}
				}
			}
			return null;
		} else if (toType == String.class) { // 服务器向浏览器输出时，进行Date to String的类型转换
			return dataString.trim();
		}else if(toType == Boolean.class){
			if("0".equals(dataString)||"false".equals(dataString.toLowerCase())){
				return false;
			}else if("1".equals(dataString)||"true".equals(dataString.toLowerCase())){
				return true;
			}else{
				return null;
			}
		}
		return null;
	}
}
