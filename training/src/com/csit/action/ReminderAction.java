package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Reminder;
import com.csit.model.User;
import com.csit.service.ReminderService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:系统提醒Action
 * @Copyreminder: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Controller
@Scope("prototype")
public class ReminderAction extends BaseAction implements ModelDriven<Reminder> {

	private static final long serialVersionUID = -4729283587857281475L;
	private static final Logger logger = Logger.getLogger(ReminderAction.class);
	private Reminder model = new Reminder();
	
	@Resource
	private ReminderService reminderService;
	
	public Reminder getModel() {
		return model;
	}
	
	/**
	 * @Description:分页查询系统提醒 
	 * @Create: 2012-12-18 下午10:58:02
	 * @author lys
	 * @update logs
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询系统提醒失败");
			logger.error("查询系统提醒失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 统计系统提醒
	 * @Create: 2012-12-18 下午10:59:26
	 * @author lys
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计系统提醒失败");
			logger.error("统计系统提醒失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description:保存系统提醒单
	 * @Create: 2013-1-3 上午10:22:16
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			String delReminderDetailIds = getParameter("delReminderDetailIds");
			String reminderDetailIds = getParameter("reminderDetailIds");
			String reminderItemIds = getParameter("reminderItemIds");
			
			result = reminderService.save(model,delReminderDetailIds,reminderDetailIds,reminderItemIds);
		} catch (Exception e) {
			result.setMessage("保存系统提醒失败");
			logger.error("保存系统提醒失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 打开初始化
	 * @Create: 2013-1-8 下午10:28:15
	 * @author lys
	 * @update logs
	 */
	public void init(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderService.init(model);
		} catch (Exception e) {
			result.setMessage("打开初始化失败");
			logger.error("打开初始化失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 批量删除系统提醒单
	 * @Create: 2013-1-8 下午10:28:15
	 * @author lys
	 * @update logs
	 */
	public void mulDelete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderService.mulDelete(ids);
		} catch (Exception e) {
			result.setMessage("批量删除系统提醒单");
			logger.error("批量删除系统提醒单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 更新系统提醒单状态
	 * @Create: 2013-1-14 下午9:24:13
	 * @author lys
	 * @update logs
	 */
	public void mulUpdateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderService.mulUpdateStatus(ids,model);
		} catch (Exception e) {
			result.setMessage("更新系统提醒单状态失败");
			logger.error("更新系统提醒单状态失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 首页显示当前用户的系统提醒
	 * @Created Time: 2013-3-5 下午4:22:50
	 * @Author lys
	 */
	public void getCurrentUser(){
		ServiceResult result = new ServiceResult(false);
		String userId = getSession(User.LOGIN_USERID).toString();
		String schoolId = getSession(User.LOGIN_SCHOOLID).toString();
		String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
		try {
			result = reminderService.getCurrentUser(userId,schoolId,schoolCode);
		} catch (Exception e) {
			result.setMessage("首页显示当前用户的系统提醒失败");
			logger.error("首页显示当前用户的系统提醒失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 更新系统提醒顺序
	 * @Created Time: 2013-3-6 下午4:26:22
	 * @Author lys
	 */
	public void updateArray(){
		String reminderId = getParameter("reminderId");
		String updateReminderId = getParameter("updateReminderId");
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderService.updateArray(Integer.parseInt(reminderId),Integer.parseInt(updateReminderId));
		} catch (Exception e) {
			result.setMessage("更新系统提醒顺序失败");
			logger.error("更新系统提醒顺序失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
}
