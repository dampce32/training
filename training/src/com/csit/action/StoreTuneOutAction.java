package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.StoreTuneOut;
import com.csit.service.StoreTuneOutService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:库存调拨Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Controller
@Scope("prototype")
public class StoreTuneOutAction extends BaseAction implements ModelDriven<StoreTuneOut> {
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(StoreTuneOutAction.class);
	private StoreTuneOut model = new StoreTuneOut();
	
	@Resource
	private StoreTuneOutService storeTuneOutService;
	
	public StoreTuneOut getModel() {
		return model;
	}
	
	/**
	 * 
	 * @Description: 保存调拨单
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = storeTuneOutService.save(model);
		}catch (Exception e) {
			result.setMessage(e.getMessage());
			logger.error("保存调拨单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询调拨单
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void query(){
		
		ServiceResult result = new ServiceResult(false);
		String beginDate=getParameter("beginDate");
		String endDate=getParameter("endDate");
		try {
			result = storeTuneOutService.query(model,beginDate,endDate,page,rows);
		} catch (Exception e) {
			result.setMessage("查询调拨单失败");
			logger.error("查询调拨单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计调拨单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		String beginDate=getParameter("beginDate");
		String endDate=getParameter("endDate");
		try {
			result = storeTuneOutService.getTotalCount(model, beginDate, endDate);
		} catch (Exception e) {
			result.setMessage("统计调拨单失败");
			logger.error("统计调拨单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 删除调拨单
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		String storeTuneOutIds=getParameter("storeTuneOutIds");
		try {
			result = storeTuneOutService.delete(storeTuneOutIds);
		}catch (Exception e) {
			result.setMessage(e.getMessage());
			logger.error("删除调拨单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
