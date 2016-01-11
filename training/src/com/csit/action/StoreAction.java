package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Store;
import com.csit.service.StoreService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:库存Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Controller
@Scope("prototype")
public class StoreAction extends BaseAction implements ModelDriven<Store> {
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(StoreAction.class);
	private Store model = new Store();
	
	@Resource
	private StoreService storeService;
	
	public Store getModel() {
		return model;
	}
	
	/**
	 * 
	 * @Description: 分页查询库存
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void query(){
		
		ServiceResult result = new ServiceResult(false);
		try {
			result = storeService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询库存失败");
			logger.error("查询库存失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计库存
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = storeService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计库存失败");
			logger.error("统计库存失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询库存不为空调拨单
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void queryNotEmpty(){
		
		ServiceResult result = new ServiceResult(false);
		try {
			result = storeService.queryNotEmpty(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询调拨单失败");
			logger.error("查询调拨单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 统计库存
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCountNotEmpty(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = storeService.getTotalCountNotEmpty(model);
		} catch (Exception e) {
			result.setMessage("统计库存失败");
			logger.error("统计库存失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

}
