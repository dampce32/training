package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Holiday;
import com.csit.service.HolidayService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description: 假期表Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class HolidayAction extends BaseAction implements ModelDriven<Holiday> {

	private static final long serialVersionUID = -2871487418750641928L;
	private static final Logger logger = Logger.getLogger(HolidayAction.class);
	private Holiday model = new Holiday();
	@Resource
	private HolidayService holidayService;
	public Holiday getModel() {
		return model;
	}
	/**
	 * 
	 * @Description: 保存假期表
	 * @param
	 * @Create: 2013-2-28 下午11:47:38
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = holidayService.save(model);
		} catch (Exception e) {
			result.setMessage("保存假期失败");
			logger.error("保存假期失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 删除假期表
	 * @param
	 * @Create: 2013-2-28 下午11:47:30
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = holidayService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除假期失败");
				logger.error("删除假期失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询假期表
	 * @param
	 * @Create: 2013-2-28 下午11:47:19
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = holidayService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询假期失败");
			logger.error("查询假期失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计假期表
	 * @param
	 * @Create: 2013-2-28 下午11:47:08
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = holidayService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计假期失败");
			logger.error("统计假期失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-2-28 下午11:45:52
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryCombobox() {
		String jsonString = holidayService.queryCombobox();
		ajaxJson(jsonString);
	}
}
