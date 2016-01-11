package com.csit.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Attend;
import com.csit.service.AttendService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description: 学员出勤表Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-7
 * @author yk
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class AttendAction extends BaseAction implements ModelDriven<Attend> {
	
	private static final long serialVersionUID = -7752361779754841650L;
	private static final Logger logger = Logger.getLogger(AttendAction.class);
	private Attend model = new Attend();
	@Resource
	private AttendService attendService;
	public Attend getModel() {
		return model;
	}
	/**
	 * 
	 * @Description: 保存学员出勤表
	 * @Create: 2013-4-11 下午04:49:08
	 * @author yk
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = attendService.save(model);
		} catch (Throwable e) {
			result.setMessage("保存学员出勤表失败");
			logger.error("保存学员出勤表失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 删除学员出勤表
	 * @Create: 2013-4-7 下午09:45:03
	 * @author yk
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = attendService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除学员出勤表失败");
				logger.error("删除学员出勤表失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询学员出勤表
	 * @Create: 2013-4-7 下午09:45:13
	 * @author yk
	 * @update logs
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = attendService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询学员出勤表失败");
			logger.error("查询学员出勤表失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计学员出勤表
	 * @Create: 2013-4-7 下午09:45:28
	 * @author yk
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = attendService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计学员出勤表失败");
			logger.error("统计学员出勤表失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 批量添加学员出勤表
	 * @Create: 2013-4-7 下午09:48:02
	 * @author yk
	 * @update logs
	 */
	public void batchSave(){
		ServiceResult result = new ServiceResult(false);
		String idStr = getParameter("idStr");
		try {
			result = attendService.batchSave(model,idStr);
		} catch (Exception e) {
			result.setMessage("保存学员出勤表失败");
			logger.error("保存学员出勤表失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 查询今日签到
	 * @Create: 2013-4-18 下午05:02:57
	 * @author yk
	 * @update logs
	 */
	public void todayQuery(){
		ServiceResult result = new ServiceResult(false);
		
		Integer studentId = null;
		if(StringUtils.isNotEmpty(getParameter("studentId"))){
			studentId = Integer.parseInt(getParameter("studentId"));
		}
		Integer schoolId = null;
		if(StringUtils.isNotEmpty(getParameter("schoolId"))){
			schoolId = Integer.parseInt(getParameter("schoolId"));
		}
		Date lessonDegreeDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(StringUtils.isNotEmpty(getParameter("lessonDegreeDate"))){
				lessonDegreeDate = sdf.parse(getParameter("lessonDegreeDate"));
			}
			
			result = attendService.todayAttendQuery(studentId,schoolId,lessonDegreeDate,page,rows);
		} catch (Exception e) {
			result.setMessage("查询今日签到失败");
			logger.error("查询今日签到失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计今日签到
	 * @Create: 2013-4-18 下午05:03:12
	 * @author yk
	 * @update logs
	 */
	public void todayTotal(){
		ServiceResult result = new ServiceResult(false);
		
		Integer studentId = null;
		if(StringUtils.isNotEmpty(getParameter("studentId"))){
			studentId = Integer.parseInt(getParameter("studentId"));
		}
		Integer schoolId = null;
		if(StringUtils.isNotEmpty(getParameter("schoolId"))){
			schoolId = Integer.parseInt(getParameter("schoolId"));
		}
		Date lessonDegreeDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(StringUtils.isNotEmpty(getParameter("lessonDegreeDate"))){
				lessonDegreeDate = sdf.parse(getParameter("lessonDegreeDate"));
			}
			
			result = attendService.todayAttendTotal(studentId,schoolId,lessonDegreeDate);
		} catch (Exception e) {
			result.setMessage("统计今日签到失败");
			logger.error("统计今日签到失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 批量添加出勤学员
	 * @Create: 2013-4-25 下午04:10:09
	 * @author yk
	 * @update logs
	 */
	public void batchFact(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = attendService.batchFact(model);
		} catch (Throwable e) {
			result.setMessage("保存学员出勤表失败");
			logger.error("保存学员出勤表失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
