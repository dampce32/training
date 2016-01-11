package com.csit.action;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Clazz;
import com.csit.model.User;
import com.csit.service.ClassService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description: 班级Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @author yk
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class ClassAction extends BaseAction implements ModelDriven<Clazz> {
	
	private static final long serialVersionUID = 4621188145109535862L;
	private static final Logger logger = Logger.getLogger(ClassAction.class);
	private Clazz model = new Clazz();
	@Resource
	private ClassService classService;

	@Override
	public Clazz getModel() {
		return model;
	}
	/**
	 * 
	 * @Description: 保存班级 
	 * @param
	 * @Create: 2013-3-4 下午04:53:53
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = classService.save(model);
		} catch (Throwable e) {
			result.setMessage("保存班级失败");
			logger.error("保存班级失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 删除班级 
	 * @param
	 * @Create: 2013-3-4 下午04:53:42
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = classService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除班级失败");
				logger.error("删除班级失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询班级 
	 * @param
	 * @Create: 2013-3-4 下午04:53:25
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		Integer status = Integer.parseInt(getParameter("status"));
		try {
			result = classService.query(model,page,rows,status);
		} catch (Exception e) {
			result.setMessage("查询班级失败");
			logger.error("查询班级失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 教师分页查询班级 
	 * @param
	 * @Create: 2013-3-4 下午04:53:25
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryByTeacher(){
		ServiceResult result = new ServiceResult(false);
		try {
			Integer userId=(Integer) getSession(User.LOGIN_USERID);
			User user=new User();
			user.setUserId(userId);
			model.setTeacher(user);
			result = classService.query(model,page,rows,0);
		} catch (Exception e) {
			result.setMessage("查询班级失败");
			logger.error("查询班级失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计班级 
	 * @param
	 * @Create: 2013-3-4 下午04:53:10
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		Integer status = Integer.parseInt(getParameter("status"));
		try {
			result = classService.getTotalCount(model,status);
		} catch (Exception e) {
			result.setMessage("统计班级失败");
			logger.error("统计班级失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 教师统计班级 
	 * @param
	 * @Create: 2013-3-4 下午04:53:10
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void getTotalCountByTeacher(){
		ServiceResult result = new ServiceResult(false);
		try {
			Integer userId=(Integer) getSession(User.LOGIN_USERID);
			User user=new User();
			user.setUserId(userId);
			model.setTeacher(user);
			result = classService.getTotalCount(model,0);
		} catch (Exception e) {
			result.setMessage("统计班级失败");
			logger.error("统计班级失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: combobox查询 
	 * @param
	 * @Create: 2013-3-4 下午04:52:56
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryCombobox() {
		try {
			String jsonString = classService.queryCombobox(model);
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description: combobox查询 
	 * @param
	 * @Create: 2013-3-4 下午04:52:56
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryComboboxByTeacher() {
		try {
			Integer userId=(Integer) getSession(User.LOGIN_USERID);
			User user=new User();
			user.setUserId(userId);
			model.setTeacher(user);
			String jsonString = classService.queryCombobox(model);
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description: combobox查询指定校区、上课状态、讲师下的班级
	 * @Create: 2013-3-21 下午04:10:46
	 * @author yk
	 * @update logs
	 */
	public void queryInSSTCombobox(){
		Integer courseId = null;
		if(model.getCourse()!=null&&model.getCourse().getCourseId()!=null){
			courseId = model.getCourse().getCourseId();
		}
		Integer schoolId = null;
		if(model.getSchool()!=null&&model.getSchool().getSchoolId()!=null){
			schoolId = model.getSchool().getSchoolId();
		}
		Integer lessonStatus = null;
		if(StringUtils.isNotEmpty(getParameter("lessonStatus"))){
			lessonStatus = Integer.parseInt(getParameter("lessonStatus"));
		}
		Integer teacherId = null;
		if(model.getTeacher()!=null&&model.getTeacher().getUserId()!=null){
			teacherId = model.getTeacher().getUserId();
		}
		try {
			String jsonString = classService.queryInSSTCombobox(courseId,schoolId,lessonStatus,teacherId);
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description: 根据班级Id查询班级，根据学生Id、班级Id查询选班状态
	 * @Create: 2013-4-1 下午03:00:38
	 * @author yk
	 * @update logs
	 */
	public void queryOne(){
		ServiceResult result = new ServiceResult(false);
		Integer studentId = null;
		if(StringUtils.isNotEmpty(getParameter("studentId"))){
			studentId = Integer.parseInt(getParameter("studentId"));
		}
		try {
			result = classService.queryOne(studentId,model.getClassId());
		} catch (Exception e) {
			result.setMessage("查询班级失败");
			logger.error("查询班级失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
