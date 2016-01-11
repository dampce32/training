package com.csit.filter;

import com.csit.model.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @Description:员工登陆拦截器
 * 在要访问员工可访问资源前，判断员工是否已登陆
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-29
 * @Author lys
 */
public class UserLoginVerifyInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -86246303854807787L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Integer userLogin = (Integer) invocation.getInvocationContext().getSession().get(User.LOGIN_USERID);
		if (userLogin == null) {
			return "login";
		}
		return invocation.invoke();
	}

}