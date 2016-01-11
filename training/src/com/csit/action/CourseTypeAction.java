package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.CourseType;
import com.csit.service.CourseTypeService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:课程类型Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author lys
 */
@Controller
@Scope("prototype")
public class CourseTypeAction extends BaseAction implements
		ModelDriven<CourseType> {
	private static final long serialVersionUID = 4621188145109535862L;
	private static final Logger logger = Logger.getLogger(CourseTypeAction.class);
	private CourseType model = new CourseType();
	@Resource
	private CourseTypeService courseTypeService;
	public CourseType getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存课程类型
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = courseTypeService.save(model);
		} catch (Throwable e) {
			result.setMessage("保存课程类型失败");
			logger.error("保存课程类型失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 删除课程类型
	 * @Create: 2012-12-18 下午10:56:44
	 * @author lys
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = courseTypeService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除课程类型失败");
				logger.error("删除课程类型失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description:分页查询课程类型 
	 * @Create: 2012-12-18 下午10:58:02
	 * @author lys
	 * @update logs
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = courseTypeService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询课程类型失败");
			logger.error("查询课程类型失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 统计课程类型
	 * @Create: 2012-12-18 下午10:59:26
	 * @author lys
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = courseTypeService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计课程类型失败");
			logger.error("统计课程类型失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: combobox查询
	 * @Create: 2012-12-29 下午11:18:19
	 * @author lys
	 * @update logs
	 */
	public void queryCombobox() {
		try {
			String jsonString = courseTypeService.queryCombobox();
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
