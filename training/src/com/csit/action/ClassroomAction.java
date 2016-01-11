package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Classroom;
import com.csit.model.User;
import com.csit.service.ClassroomService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description: 教室Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author yk
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class ClassroomAction extends BaseAction implements ModelDriven<Classroom> {

	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(ClassroomAction.class);
	private Classroom model = new Classroom();
	
	@Resource
	private ClassroomService classroomService;
	
	public Classroom getModel() {
		return model;
	}
	/**
	 * 
	 * @Description: 保存教室
	 * @param
	 * @Create: 2013-3-1 下午01:52:55
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = classroomService.save(model);
		} catch (Throwable e) {
			result.setMessage("保存教室失败");
			logger.error("保存教室失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 	
	 * @Description: 删除教室
	 * @param
	 * @Create: 2013-3-1 下午01:53:04
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = classroomService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除教室失败");
				logger.error("删除教室失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询教室
	 * @param
	 * @Create: 2013-3-1 下午01:53:16
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = classroomService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询教室失败");
			logger.error("查询教室失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计教室
	 * @param
	 * @Create: 2013-3-1 下午01:53:27
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = classroomService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计教室失败");
			logger.error("统计教室失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-3-1 下午01:53:40
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryCombobox() {
		try {
			String jsonString = classroomService.queryCombobox();
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description: 修改教室状态
	 * @param
	 * @Create: 2013-3-1 下午04:07:30
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = classroomService.updateStatus(model);
		} catch (Exception e) {
			result.setMessage("修改教室状态失败");
			logger.error("修改教室状态失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: combobox查询指定校区的教室
	 * @param
	 * @Create: 2013-3-5 下午01:47:06
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryInSchoolCombobox() {
		try {
			String jsonString = classroomService.queryInSchoolCombobox(model.getSchool().getSchoolId());
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
