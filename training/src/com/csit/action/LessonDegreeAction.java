package com.csit.action;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.LessonDegree;
import com.csit.model.Student;
import com.csit.model.User;
import com.csit.service.LessonDegreeService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description: 排课表Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author yk
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class LessonDegreeAction extends BaseAction implements
		ModelDriven<LessonDegree> {
	
	private static final long serialVersionUID = -6150044116147933184L;
	private static final Logger logger = Logger.getLogger(LessonDegreeAction.class);
	private LessonDegree model = new LessonDegree();
	@Resource
	private LessonDegreeService lessonDegreeService;
	public LessonDegree getModel() {
		return model;
	}
	/**
	 * 
	 * @Description: 保存逐个排课
	 * @param
	 * @Create: 2013-3-7 下午02:39:10
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = lessonDegreeService.save(model);
		} catch (Exception e) {
			result.setMessage("保存排课课次失败");
			logger.error("保存排课课次失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 删除排课
	 * @param
	 * @Create: 2013-3-7 下午02:39:21
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = lessonDegreeService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException||
					e instanceof org.hibernate.exception.ConstraintViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除排课课次失败");
				logger.error("删除排课课次失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询排课
	 * @param
	 * @Create: 2013-3-7 下午02:39:40
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = lessonDegreeService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询排课课次失败");
			logger.error("查询排课课次失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计排课
	 * @param
	 * @Create: 2013-3-7 下午02:39:48
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = lessonDegreeService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计排课课次失败");
			logger.error("统计排课课次失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 保存自动排课 
	 * @param
	 * @Create: 2013-3-12 下午04:12:54
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void autoDegreeSave(){
		ServiceResult result = new ServiceResult(false);
		String userId = getSession(User.LOGIN_USERID).toString();
		String params = getParameter("params");
		String firstDegreeDate = getParameter("firstDegreeDate");
		String waitDegree = getParameter("waitDegree");
		try {
			result = lessonDegreeService.autoDegreeSave(params,firstDegreeDate,waitDegree,userId,model);
		} catch (Exception e) {
			result.setMessage("自动排课失败");
			logger.error("自动排课失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 清除排课
	 * @param
	 * @Create: 2013-3-13 下午12:30:10
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void clear(){
		ServiceResult result = new ServiceResult(false);
		String idStr = getParameter("idStr");
		String lessonStr = getParameter("lessonStr");
		try {
			result = lessonDegreeService.clear(idStr,lessonStr,model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException||
					e instanceof org.hibernate.exception.ConstraintViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除排课课次失败");
				logger.error("删除排课课次失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 查询课程总表（周）
	 * @param
	 * @Create: 2013-3-18 下午03:41:41
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void weekTableQuery(){
		ServiceResult result = new ServiceResult(false);
		String toSunday = getParameter("toSunday");
		String toWeekOfToday = getParameter("toWeekOfToday");
		Integer schoolId = null;
		if(StringUtils.isNotEmpty(getParameter("schoolId"))){
			schoolId = Integer.parseInt(getParameter("schoolId"));
		}
		Integer queryParam = Integer.parseInt(getParameter("queryParam"));
		try {
			result = lessonDegreeService.weekTableQuery(toSunday,toWeekOfToday,model,schoolId,queryParam);
		} catch (Exception e) {
			result.setMessage("查询课程总表（周）失败");
			logger.error("查询课程总表（周）失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 查询课程总表（周）
	 * @param
	 * @Create: 2013-3-18 下午03:41:41
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryByTeacher(){
		ServiceResult result = new ServiceResult(false);
		String toSunday = getParameter("toSunday");
		String toWeekOfToday = getParameter("toWeekOfToday");
		Integer schoolId = null;
		try {
			Integer userId=(Integer) getSession(User.LOGIN_USERID);
			User user=new User();
			user.setUserId(userId);
			model.setTeacher(user);
			result = lessonDegreeService.weekTableQuery(toSunday,toWeekOfToday,model,schoolId,0);
		} catch (Exception e) {
			result.setMessage("查询课程总表（周）失败");
			logger.error("查询课程总表（周）失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	public void queryByStu(){
		
		ServiceResult result = new ServiceResult(false);
		try {
			String studentId=getSession().get(Student.LOGIN_STUDENTID).toString();
			result = lessonDegreeService.findByStu(Integer.parseInt(studentId), page, rows);
		} catch (Exception e) {
			result.setMessage("查询上课信息失败");
			logger.error("查询上课信息失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	public void getTotalCountByStu(){
		ServiceResult result = new ServiceResult(false);
		try {
			String studentId=getSession().get(Student.LOGIN_STUDENTID).toString();
			result = lessonDegreeService.getTotalCountByStu(Integer.parseInt(studentId));
		} catch (Exception e) {
			result.setMessage("统计排课课次失败");
			logger.error("统计排课课次失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
