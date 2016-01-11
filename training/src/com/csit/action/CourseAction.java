package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Course;
import com.csit.service.CourseService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description: 课程Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class CourseAction extends BaseAction implements ModelDriven<Course> {
	
	private static final long serialVersionUID = 4015884017822611486L;
	private static final Logger logger = Logger.getLogger(CourseAction.class);
	private Course model = new Course();
	@Resource
	private CourseService courseService;
	public Course getModel() {
		return model;
	}
	/**
	 * 
	 * @Description: 保存课程
	 * @param
	 * @Create: 2013-2-28 上午10:26:29
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = courseService.save(model);
		} catch (Exception e) {
			result.setMessage("保存课程失败");
			logger.error("保存课程失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 删除课程
	 * @param
	 * @Create: 2013-2-28 上午10:26:43
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = courseService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除课程失败");
				logger.error("删除课程失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询课程
	 * @param
	 * @Create: 2013-2-28 上午10:26:53
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			String courseIds=getParameter("courseIds");
			result = courseService.query(model,page,rows,courseIds);
		} catch (Exception e) {
			result.setMessage("查询课程失败");
			logger.error("查询课程失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计课程
	 * @param
	 * @Create: 2013-2-28 上午10:27:08
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			String courseIds=getParameter("courseIds");
			result = courseService.getTotalCount(model,courseIds);
		} catch (Exception e) {
			result.setMessage("统计课程失败");
			logger.error("统计课程失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-2-28 上午10:27:19
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryCombobox() {
		String jsonString = courseService.queryCombobox();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 修改课程状态
	 * @param
	 * @Create: 2013-3-1 下午04:37:19
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = courseService.updateStatus(model);
		} catch (Exception e) {
			result.setMessage("修改课程状态失败");
			logger.error("修改课程状态失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: combobox查询指定类型的课程
	 * @param
	 * @Create: 2013-3-5 上午09:31:24
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryIsTypeCombobox(){
		Integer courseTypeId = null;
		if(model.getCourseType()!=null&&model.getCourseType().getCourseTypeId()!=null){
			courseTypeId = model.getCourseType().getCourseTypeId();
		}
		try {
			String jsonString = courseService.queryIsTypeCombobox(courseTypeId);
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
