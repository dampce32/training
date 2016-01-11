package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.UseCommodity;
import com.csit.service.UseCommodityService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:领用单Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Controller
@Scope("prototype")
public class UseCommodityAction extends BaseAction implements ModelDriven<UseCommodity> {
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(UseCommodityAction.class);
	private UseCommodity model = new UseCommodity();
	
	@Resource
	private UseCommodityService useCommodityService;
	
	public UseCommodity getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存领用单
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		String isNeedReturns=getParameter("isNeedReturns");
		String returnDates=getParameter("returnDates");
		String returnStatus=getParameter("returnStatus");
		String commodityIds=getParameter("commodityIds");
		String delUseCommodityDetailIds=getParameter("delUseCommodityDetailIds");
		String useCommodityDetailIds=getParameter("useCommodityDetailIds");
		String warehouseDetailIds=getParameter("warehouseDetailIds");
		String qtys=getParameter("qtys");
		try {
			result = useCommodityService.save(model,commodityIds,delUseCommodityDetailIds
					,useCommodityDetailIds,warehouseDetailIds,qtys,
					isNeedReturns,returnDates,returnStatus);
		}catch (Exception e) {
			result.setMessage(e.getMessage());
			logger.error("保存入库出库单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询领用单
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
			result = useCommodityService.query(model,beginDate,endDate,page,rows);
		} catch (Exception e) {
			result.setMessage("查询入库出库单失败");
			logger.error("查询入库出库单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计领用单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		String beginDate=getParameter("beginDate");
		String endDate=getParameter("endDate");
		try {
			result = useCommodityService.getTotalCount(model,beginDate,endDate);
		} catch (Exception e) {
			result.setMessage("统计领用单失败");
			logger.error("统计领用单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 查询领用单详细
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void queryDetail(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = useCommodityService.queryDetail(model);
		} catch (Exception e) {
			result.setMessage("查询领用单详细失败");
			logger.error("查询领用单详细失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 查询需要归还的领用单详细
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void queryNeedReturn(){
		ServiceResult result = new ServiceResult(false);
		String commodityName=getParameter("commodityName");
		String userName=getParameter("userName");
		String useCommodityDetailIds=getParameter("useCommodityDetailIds");
		try {
			result = useCommodityService.queryNeedReturn(model,commodityName,userName,rows,page,useCommodityDetailIds);
		} catch (Exception e) {
			result.setMessage("查询领用单详细失败");
			logger.error("查询领用单详细失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 统计领用单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCountNeedReturn(){
		ServiceResult result = new ServiceResult(false);
		String commodityName=getParameter("commodityName");
		String userName=getParameter("userName");
		String useCommodityDetailIds=getParameter("useCommodityDetailIds");
		try {
			result = useCommodityService.getTotalCountNeedReturn(model,commodityName,userName,useCommodityDetailIds);
		} catch (Exception e) {
			result.setMessage("统计领用单失败");
			logger.error("统计领用单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}


	/**
	 * @Description: 批量删除领用单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		String useCommodityIds=getParameter("useCommodityIds");
		try {
			result = useCommodityService.delete(useCommodityIds);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除入库出库单失败");
				logger.error("删除入库出库单失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
