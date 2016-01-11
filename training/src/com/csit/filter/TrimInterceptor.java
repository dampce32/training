package com.csit.filter;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 
 * @Description: 去除参数的两端空格
 * @Copyright: 福州骏华科技信息有限公司 (c)2012
 * @Created Date : 2012-4-5
 * @author longweier
 * @vesion 1.0
 */
public class TrimInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 2365641900033439481L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> parameters = invocation.getInvocationContext().getParameters();
		String[] keySet = parameters.keySet().toArray(new String[0]);
		for (String key : keySet) {
			Object value = parameters.get(key);
			if (value instanceof String[]) {
				String[] valueArray = (String[]) value;
				for (int i = 0; i < valueArray.length; i++) {
					if(StringUtils.isNotEmpty(valueArray[i])){
						valueArray[i] = valueArray[i].trim();
					}else{
						parameters.remove(key);
					}
				}
			}
		}
		return invocation.invoke();
	}

}