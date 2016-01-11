package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.ReturnCommodity;
import com.csit.service.ReturnCommodityService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:归还单Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Controller
@Scope("prototype")
public class ReturnCommodityAction extends BaseAction implements ModelDriven<ReturnCommodity> {
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(ReturnCommodityAction.class);
	private ReturnCommodity model = new ReturnCommodity();
	
	@Resource
	private ReturnCommodityService returnCommodityService;
	
	public ReturnCommodity getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存归还单
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		String useCommodityDetailIds=getParameter("useCommodityDetailIds");
		String delReturnCommodityDetailIds=getParameter("delReturnCommodityDetailIds");
		String returnCommodityDetailIds=getParameter("returnCommodityDetailIds");
		String warehouseDetailIds=getParameter("warehouseDetailIds");
		String qtys=getParameter("qtys");
		try {
			result = returnCommodityService.save(model,useCommodityDetailIds,
					delReturnCommodityDetailIds,returnCommodityDetailIds,warehouseDetailIds,qtys);
		}catch (Exception e) {
			result.setMessage(e.getMessage());
			logger.error("保存入库出库单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询归还单
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
			result = returnCommodityService.query(model,beginDate,endDate,page,rows);
		} catch (Exception e) {
			result.setMessage("查询归还单失败");
			logger.error("查询归还单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计归还单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		String beginDate=getParameter("beginDate");
		String endDate=getParameter("endDate");
		try {
			result = returnCommodityService.getTotalCount(model,beginDate,endDate);
		} catch (Exception e) {
			result.setMessage("统计归还单失败");
			logger.error("统计归还单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 查询归还单详细
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void queryDetail(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = returnCommodityService.queryDetail(model);
		} catch (Exception e) {
			result.setMessage("查询归还单详细失败");
			logger.error("查询归还单详细失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 批量删除归还单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		String returnCommodityIds=getParameter("returnCommodityIds");
		try {
			result = returnCommodityService.delete(returnCommodityIds);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除归还单失败");
				logger.error("删除归还单失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
