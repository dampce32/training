package com.csit.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.csit.model.User;
/**
 * @Description: 登录验证过滤器
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-29
 * @author lys
 * @vesion 1.0
 */
public class LoginVerifyFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String path = request.getRequestURI().toString();
		if(path.contains("system/loginUser.do")
				||request.getSession().getAttribute(User.LOGIN_USERID)!=null
				){
			chain.doFilter(req, res);
			return;
		}else{
			response.sendRedirect("login.html");
		}
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
