package com.csit.util;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * @Description:共用的工具类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-13
 * @author lys
 * @vesion 1.0
 */
public class CommonUtil {
	static Properties pro = new Properties();
	static{
		try {
			pro.load(CommonUtil.class.getClassLoader().getResourceAsStream("codePrefix.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @Description: 取得单据的默认编号配置文件key对应的值
	 * @Create: 2013-1-13 上午8:57:04
	 * @author lys
	 * @update logs
	 * @param key
	 * @return
	 */
	public static String getValue(String key){
		return pro.get(key)==null?null:pro.get(key).toString();
	}
	/**
	 * @Description: 取得编号前缀
	 * @Create: 2013-1-13 上午9:27:36
	 * @author lys
	 * @update logs
	 * @param tableKey
	 * @return
	 */
	public static String getCodePrefix(String tableKey){
		String codePrefix =getValue(tableKey)+ DateUtil.dateToString(new Date(),"yyyyMMdd");
		return codePrefix;
	}
	/**
	 * @Description: 取得编号字段
	 * @Create: 2013-1-13 上午9:31:44
	 * @author lys
	 * @update logs
	 * @param tableKey
	 * @return
	 */
	public static String getCodeField(String tableKey){
		String codeField = getValue(tableKey+"codeField");
		if(codeField==null){
			codeField = tableKey+"Code";
		}
		return codeField;
	}
}
