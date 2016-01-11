package com.csit.util;


import java.util.List;

import net.sf.json.util.PropertyFilter;
/**
 * @Description:JSON过滤器
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-27
 * @author lys
 * @vesion 1.0
 */
public class JSONPropertyFilter implements PropertyFilter {
	
	private List<String>  propertyList;

	public JSONPropertyFilter(List<String> propertyList) {
		this.propertyList = propertyList;
	}

	public boolean apply(Object source, String name, Object value) {
		for (String property : propertyList) {
			if(property.equals(name)){
				return false;
			}
		}
		return true;
	}

}
