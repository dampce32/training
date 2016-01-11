package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.RecRej;
import com.csit.service.RecRejService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:入库出库单Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Controller
@Scope("prototype")
public class RecRejAction extends BaseAction implements ModelDriven<RecRej> {
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(RecRejAction.class);
	private RecRej model = new RecRej();
	
	@Resource
	private RecRejService recRejService;
	
	public RecRej getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存入库出库单
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		String commodityIds=getParameter("commodityIds");
		String delReceiveDetailIds=getParameter("delReceiveDetailIds");
		String receiveDetailIds=getParameter("receiveDetailIds");
		String warehouseDetailIds=getParameter("warehouseDetailIds");
		String qtys=getParameter("qtys");
		String purchasePrices=getParameter("purchasePrices");
		String totalPrices=getParameter("totalPrices");
		try {
			if(model.getRecRejType().equals(1)){
				result = recRejService.saveReceive(model,receiveDetailIds,commodityIds,warehouseDetailIds,qtys,purchasePrices,totalPrices,delReceiveDetailIds);
			}else if(model.getRecRejType().equals(-1)){
				result = recRejService.saveReject(model,receiveDetailIds,commodityIds,warehouseDetailIds,qtys,purchasePrices,totalPrices,delReceiveDetailIds);
			}
		}catch (Exception e) {
			result.setMessage(e.getMessage());
			logger.error("保存入库出库单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 初始化入库单编号
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void initCode(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = recRejService.initCode();
		} catch (Exception e) {
			result.setMessage("初始化入库单编号失败");
			logger.error("初始化入库单编号失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询入库出库单
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
			result = recRejService.query(model,beginDate,endDate,page,rows);
		} catch (Exception e) {
			result.setMessage("查询入库出库单失败");
			logger.error("查询入库出库单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计入库出库单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		String beginDate=getParameter("beginDate");
		String endDate=getParameter("endDate");
		try {
			result = recRejService.getTotalCount(model,beginDate,endDate);
		} catch (Exception e) {
			result.setMessage("统计入库出库单失败");
			logger.error("统计入库出库单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 查询入库出库单详细
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void queryDetail(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = recRejService.queryDetail(model);
		} catch (Exception e) {
			result.setMessage("查询入库出库单详细失败");
			logger.error("查询入库出库单详细失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 批量删除入库出库单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		String recrRejIds=getParameter("recrRejIds");
		try {
			result = recRejService.delete(recrRejIds);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage(e.getMessage());
				logger.error("删除入库出库单失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
