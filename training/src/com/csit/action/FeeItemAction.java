package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.FeeItem;
import com.csit.service.FeeItemService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description: 消费项Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class FeeItemAction extends BaseAction implements ModelDriven<FeeItem> {

	private static final long serialVersionUID = -3754469406865956652L;
	private static final Logger logger = Logger.getLogger(FeeItemAction.class);
	private FeeItem model = new FeeItem();
	@Resource
	private FeeItemService feeItemService;
	public FeeItem getModel() {
		return model;
	}
	/**
	 * 
	 * @Description: 保存消费项
	 * @param
	 * @Create: 2013-2-28 下午04:19:17
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = feeItemService.save(model);
		} catch (Exception e) {
			result.setMessage("保存消费项失败");
			logger.error("保存消费项失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 删除消费项
	 * @param
	 * @Create: 2013-2-28 下午04:19:55
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = feeItemService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除消费项失败");
				logger.error("删除消费项失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询消费项
	 * @param
	 * @Create: 2013-2-28 下午04:20:04
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			String feeItemIds=getParameter("feeItemIds");
			result = feeItemService.query(model,page,rows,feeItemIds);
		} catch (Exception e) {
			result.setMessage("查询消费项失败");
			logger.error("查询消费项失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计消费项
	 * @param
	 * @Create: 2013-2-28 下午04:20:17
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			String feeItemIds=getParameter("feeItemIds");
			result = feeItemService.getTotalCount(model,feeItemIds);
		} catch (Exception e) {
			result.setMessage("统计消费项失败");
			logger.error("统计消费项失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-2-28 下午04:20:34
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryCombobox() {
		String jsonString = feeItemService.queryCombobox();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 修改消费项状态
	 * @param
	 * @Create: 2013-3-1 下午04:36:38
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = feeItemService.updateStatus(model);
		} catch (Exception e) {
			result.setMessage("修改消费项状态失败");
			logger.error("修改消费项状态失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
