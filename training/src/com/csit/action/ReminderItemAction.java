package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.ReminderItem;
import com.csit.service.ReminderItemService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:系统提醒项Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Controller
@Scope("prototype")
public class ReminderItemAction extends BaseAction implements
		ModelDriven<ReminderItem> {
	
	private static final long serialVersionUID = 7344706006377847786L;
	private static final Logger logger = Logger.getLogger(ReminderItemAction.class);
	private ReminderItem model = new ReminderItem();
	
	@Resource
	private ReminderItemService reminderItemService;
	
	public ReminderItem getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存系统提醒项
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderItemService.save(model);
		} catch (Throwable e) {
			result.setMessage("保存系统提醒项失败");
			logger.error("保存系统提醒项失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 删除系统提醒项
	 * @Create: 2012-12-18 下午10:56:44
	 * @author lys
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderItemService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除系统提醒项失败");
				logger.error("删除系统提醒项失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description:分页查询系统提醒项 
	 * @Create: 2012-12-18 下午10:58:02
	 * @author lys
	 * @update logs
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderItemService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询系统提醒项失败");
			logger.error("查询系统提醒项失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 统计系统提醒项
	 * @Create: 2012-12-18 下午10:59:26
	 * @author lys
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reminderItemService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计系统提醒项失败");
			logger.error("统计系统提醒项失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 系统提醒时分页选择查询系统提醒项
	 * @Created Time: 2013-3-4 下午10:05:48
	 * @Author lys
	 */
	public void selectQuery(){
		try {
			String jsonString = reminderItemService.selectQuery(ids,model,page,rows);
			ajaxJson(jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
