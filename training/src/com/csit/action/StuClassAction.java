package com.csit.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.StuClass;
import com.csit.service.StuClassService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description: 选班表Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-14
 * @author yk
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class StuClassAction extends BaseAction implements ModelDriven<StuClass> {
	
	private static final long serialVersionUID = 7092810517793747633L;
	private static final Logger logger = Logger.getLogger(StuClassAction.class);
	
	private StuClass model = new StuClass();
	
	@Resource
	private StuClassService stuClassService;
	@Override
	public StuClass getModel() {
		return model;
	}
	/**
	 * 
	 * @Description: 保存选班 （选择已选班级、选择一对多班级）
	 * @Create: 2013-3-14 上午11:12:07
	 * @author yk
	 * @update logs
	 * @return
	 */
	public void oneToManySave(){
		ServiceResult result = new ServiceResult(false);
		Integer updateParam = Integer.parseInt(getParameter("updateParam"));
		Integer billDetailId = null;
		if(getParameter("billDetailId")!=null){
			billDetailId = Integer.parseInt(getParameter("billDetailId"));
		}
		try {
			result = stuClassService.oneToManySave(model,updateParam,billDetailId);
		} catch (Exception e) {
			result.setMessage("保存一对多选班失败");
			logger.error("保存一对多选班失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 保存开班、选班（开设一对一班级）
	 * @Create: 2013-3-26 上午10:50:06
	 * @author yk
	 * @update logs
	 */
	public void oneToOneSave(){
		ServiceResult result = new ServiceResult(false);
		Integer billDetailId = Integer.parseInt(getParameter("billDetailId"));
		try {
			result = stuClassService.oneToOneSave(model,billDetailId);
		} catch (Exception e) {
			result.setMessage("保存一对一开班失败");
			logger.error("保存一对一开班失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 删除选班 
	 * @Create: 2013-3-14 上午11:12:20
	 * @author yk
	 * @update logs
	 * @return
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = stuClassService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException||
					e instanceof org.hibernate.exception.ConstraintViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除选班失败");
				logger.error("删除选班失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询选班 
	 * @Create: 2013-3-14 上午11:12:28
	 * @author yk
	 * @update logs
	 * @return
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			if(page==null||rows==null){
				result = stuClassService.query(model);
			}else{
				result = stuClassService.query(model,page,rows);
			}
		} catch (Exception e) {
			result.setMessage("查询选班失败");
			logger.error("查询选班失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计选班 
	 * @Create: 2013-3-14 上午11:12:43
	 * @author yk
	 * @update logs
	 * @return
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = stuClassService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计选班失败");
			logger.error("统计选班失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: combobox查询学员已选班级
	 * @Create: 2013-3-28 下午05:20:00
	 * @author yk
	 * @update logs
	 */
	public void querySelectedClassCombobox(){
		try {
			String jsonString ;
			if(model.getClazz()==null){
				 jsonString = stuClassService.querySelectedClassCombobox(
						null,model.getStudent().getStudentId(),3);
			}else{
				 jsonString = stuClassService.querySelectedClassCombobox(
						model.getClazz().getCourse().getCourseId(),
						model.getStudent().getStudentId(),
						model.getScStatus()
				);
			}
			
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description: 验证学员是否已选班
	 * @Create: 2013-4-7 上午09:28:30
	 * @author yk
	 * @update logs
	 */
	public void selectedValidate(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = stuClassService.selectedValidate(model);
		} catch (Exception e) {
			result.setMessage("验证是否选班失败");
			logger.error("验证是否选班失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 获取学员列表tree
	 * @Create: 2013-4-8 上午11:22:42
	 * @author yk
	 * @update logs
	 */
	public void getStudentTree(){
		ServiceResult result = new ServiceResult(false);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lessonDegreeDateStr = getParameter("lessonDegreeDate");
		Integer lessonDegreeId = Integer.parseInt(getParameter("lessonDegreeId"));
		try {
			Date lessonDegreeDate = sdf.parse(lessonDegreeDateStr);
			result = stuClassService.getStudentTree(model,lessonDegreeDate,lessonDegreeId);
		} catch (Exception e) {
			result.setMessage("获取学员列表失败");
			logger.error("获取学员列表失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
