package com.csit.filter;

import com.csit.model.Student;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 
 * @Description: 判断学生是否已登录
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-9-20
 * @author longweier
 * @vesion 1.0
 */
public class StudentLoginVerifyInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -86246303854807787L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Integer studentLogin = (Integer) invocation.getInvocationContext().getSession().get(Student.LOGIN_STUDENTID);
		if (studentLogin == null) {
			return "login";
		}
		return invocation.invoke();
	}

}