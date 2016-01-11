package com.csit.action;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Student;
import com.csit.model.User;
import com.csit.service.StudentService;
import com.csit.service.UserService;
import com.csit.util.JCaptchaEngine;
import com.csit.util.MD5Util;
import com.csit.vo.LoginType;
import com.csit.vo.ServiceResult;
import com.octo.captcha.service.CaptchaService;
/**
 * @Description:用户登录
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-29
 * @Author lys
 */
@Controller
@Scope("prototype")
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = -1379893197712674493L;
	
//	private Logger logger = Logger.getLogger(LoginAction.class);
	
	private String userCode;
	
	private String userPwd;
	
	@Resource
	private CaptchaService captchaService;
	@Resource
	private StudentService studentService;
	@Resource
	private UserService userService;
	
	/**
	 * 
	 * @Description: 学生端的登录
	 * @param
	 * @Create: 2012-9-20 下午02:30:27
	 * @author longweier
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void studentLogin(){
		try {
			ServiceResult result = new ServiceResult(false);
			String captchaID = request.getSession().getId();
			String challengeResponse = StringUtils.upperCase(request.getParameter(JCaptchaEngine.CAPTCHA_INPUT_NAME));
			
			if (StringUtils.isEmpty(challengeResponse) || captchaService.validateResponseForID(captchaID, challengeResponse) == false) {
				result.setMessage("验证码错误");
				ajaxJson(result.toJSON());
				return;
			}
			// 根据用户名和密码判断是否允许登录
			Integer userCodeInt = null;
			try {
				userCodeInt = Integer.parseInt(userCode);
			} catch (java.lang.NumberFormatException e) {
				result.setMessage("请输入正确的学号!!");
				ajaxJson(result.toJSON());
				return;
			}
			Student loginStudent = studentService.login(userCodeInt,MD5Util.getMD5(userPwd));
			if (null == loginStudent) {
				result.setMessage("用户名或密码错误!!");
			}else{
				setSession(Student.LOGIN_STUDENTID,loginStudent.getStudentId());
				result.setIsSuccess(true);
				setSession(LoginType.LOGINTYPE,LoginType.STUDENT);
			}
			String ajaxString = result.toJSON();
			ajaxJson(ajaxString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 用户端登录
	 * @Created Time: 2013-3-29 上午11:29:36
	 * @Author lys
	 */
	public void userLogin(){
		ServiceResult result = new ServiceResult(false);
		String captchaID = request.getSession().getId();
		String challengeResponse = StringUtils.upperCase(request.getParameter(JCaptchaEngine.CAPTCHA_INPUT_NAME));
		
		if (StringUtils.isEmpty(challengeResponse) || captchaService.validateResponseForID(captchaID, challengeResponse) == false) {
			result.setMessage("验证码错误");
			ajaxJson(result.toJSON());
			return;
		}
		// 根据用户名和密码判断是否允许登录
		
		User loginUser = userService.login(userCode,MD5Util.getMD5(userPwd));
		if (null == loginUser) {
			result.setMessage("用户名或密码错误!!");
		}else{
			setSession(User.LOGIN_USERID,loginUser.getUserId());
			setSession(User.LOGIN_SCHOOLID,loginUser.getSchool().getSchoolId());
			setSession(User.LOGIN_SCHOOLCODE,loginUser.getSchool().getSchoolCode());
			setSession(LoginType.LOGINTYPE,LoginType.USER);
			result.setIsSuccess(true);
		}
		String ajaxString = result.toJSON();
		ajaxJson(ajaxString);
	}
	/**
	 * @Description: 登录成功
	 * @Created Time: 2013-3-29 下午3:49:14
	 * @Author lys
	 * @return
	 */
	public String loginSucc(){
		LoginType loginType = (LoginType) getSession(LoginType.LOGINTYPE);
		if(LoginType.USER==loginType){
			return "main";
		}else if(LoginType.STUDENT==loginType){
			return "mainStu";
		}
		return "login";
	}
	
	
	/**
	 * 
	 * @Description: 注销
	 * @param
	 * @Create: 2012-9-20 下午03:46:55
	 * @author longweier
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	public void logout(){
		getSession().clear();
		ServiceResult result = new ServiceResult(true);
		ajaxJson(result.toJSON());
	}
	
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}


	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
}
