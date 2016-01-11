package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Leave;
import com.csit.model.Student;
import com.csit.model.User;
import com.csit.service.LeaveService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:请假Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author lys
 */
@Controller
@Scope("prototype")
public class LeaveAction extends BaseAction implements ModelDriven<Leave> {
	
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(LeaveAction.class);
	private Leave model = new Leave();
	
	@Resource
	private LeaveService leaveService;
	
	public Leave getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存课程类型
	 * @Create: 2012-12-23 下午5:42:17
	 * @author cjp
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = leaveService.save(model);
		} catch (Exception e) {
			result.setMessage("保存请假失败");
			logger.error("保存请假失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询请假
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode=getParameter("schoolCode");
			if(schoolCode==null||"".equals(schoolCode)){
				schoolCode=getSession(User.LOGIN_SCHOOLCODE).toString();
			}
			result = leaveService.query(model,schoolCode,page,rows);
		} catch (Exception e) {
			result.setMessage("查询请假失败");
			logger.error("查询请假失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 教师分页查询请假
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void queryByTeacher(){
		ServiceResult result = new ServiceResult(false);
		try {
			Integer userId=(Integer) getSession(User.LOGIN_USERID);
			User user=new User();
			user.setUserId(userId);
			model.setUser(user);
			result = leaveService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询请假失败");
			logger.error("查询请假失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 
	 * @Create: 2013-4-24 下午01:57:29
	 * @author jcf
	 * @update logs
	 */
	public void queryByStu(){
		ServiceResult result = new ServiceResult(false);
		try {
			Integer stuId=(Integer) getSession(Student.LOGIN_STUDENTID);
			Student student=new Student();
			student.setStudentId(stuId);
			model.setStudent(student);
			result = leaveService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询请假失败");
			logger.error("查询请假失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计请假
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode=getParameter("schoolCode");
			if(schoolCode==null||"".equals(schoolCode)){
				schoolCode=getSession(User.LOGIN_SCHOOLCODE).toString();
			}
			result = leaveService.getTotalCount(model,schoolCode);
		} catch (Exception e) {
			result.setMessage("统计请假失败");
			logger.error("统计请假失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description:教师统计请假
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCountByTeacher(){
		ServiceResult result = new ServiceResult(false);
		try {
			Integer userId=(Integer) getSession(User.LOGIN_USERID);
			User user=new User();
			user.setUserId(userId);
			model.setUser(user);
			result = leaveService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计请假失败");
			logger.error("统计请假失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 学员统计
	 * @Create: 2013-4-24 下午01:58:05
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCountByStu(){
		ServiceResult result = new ServiceResult(false);
		try {
			Integer stuId=(Integer) getSession(Student.LOGIN_STUDENTID);
			Student student=new Student();
			student.setStudentId(stuId);
			model.setStudent(student);
			result = leaveService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计请假失败");
			logger.error("统计请假失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 删除请假
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = leaveService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除请假失败");
				logger.error("删除请假失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
