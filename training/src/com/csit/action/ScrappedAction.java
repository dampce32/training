package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Scrapped;
import com.csit.service.ScrappedService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:损溢单Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Controller
@Scope("prototype")
public class ScrappedAction extends BaseAction implements ModelDriven<Scrapped> {
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(ScrappedAction.class);
	private Scrapped model = new Scrapped();
	
	@Resource
	private ScrappedService scrappedService;
	
	public Scrapped getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存损溢单
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		String commodityIds=getParameter("commodityIds");
		String delScrappedIds=getParameter("delScrappedIds");
		String scrappedDetailIds=getParameter("scrappedDetailIds");
		String warehouseDetailIds=getParameter("warehouseDetailIds");
		String qtys=getParameter("qtys");
		try {
			result = scrappedService.save(model,scrappedDetailIds,commodityIds,warehouseDetailIds,qtys,delScrappedIds);
		}catch (Exception e) {
			result.setMessage(e.getMessage());
			logger.error("保存损溢单失败", e);
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
//		ServiceResult result = new ServiceResult(false);
//		try {
//			result = ScrappedService.initCode();
//		} catch (Exception e) {
//			result.setMessage("初始化入库单编号失败");
//			logger.error("初始化入库单编号失败", e);
//			result.setIsSuccess(false);
//		}
//		String jsonString = result.toJSON();
//		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询损溢单
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
			result = scrappedService.query(model,beginDate,endDate,page,rows);
		} catch (Exception e) {
			result.setMessage("查询损溢单失败");
			logger.error("查询损溢单失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计损溢单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		String beginDate=getParameter("beginDate");
		String endDate=getParameter("endDate");
		try {
			result = scrappedService.getTotalCount(model,beginDate,endDate);
		} catch (RuntimeException e) {
			result.setMessage("统计损溢单失败");
			logger.error("统计损溢单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 查询损溢单详细
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void queryDetail(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = scrappedService.queryDetail(model);
		} catch (Exception e) {
			result.setMessage("查询损溢单详细失败");
			logger.error("查询损溢单详细失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 批量删除损溢单
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		String scrappedIds=getParameter("scrappedIds");
		try {
			result = scrappedService.delete(scrappedIds);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除损溢单失败");
				logger.error("删除损溢单失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
