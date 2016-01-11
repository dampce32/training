package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Supplier;
import com.csit.service.SupplierService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:供应商Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Controller
@Scope("prototype")
public class SupplierAction extends BaseAction implements ModelDriven<Supplier> {
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(SupplierAction.class);
	private Supplier model = new Supplier();
	
	@Resource
	private SupplierService supplierService;
	
	public Supplier getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存供应商
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = supplierService.save(model);
		} catch (Exception e) {
			result.setMessage("保存供应商失败");
			logger.error("保存供应商失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询供应商
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = supplierService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询供应商失败");
			logger.error("查询供应商失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计供应商
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = supplierService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计供应商失败");
			logger.error("统计供应商失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 删除供应商
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = supplierService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除供应商失败");
				logger.error("删除供应商失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: combobox查询
	 * @Create: 2012-12-29 下午11:18:19
	 * @author cjp
	 * @update logs
	 */
	public void queryCombobox() {
		String jsonString = supplierService.queryCombobox();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 修改供应商状态
	 * @Created Time: 2013-2-28 下午10:57:47
	 * @Author cjp
	 */
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = supplierService.updateStatus(model);
		} catch (Exception e) {
			result.setMessage("修改供应商状态失败");
			logger.error("修改供应商状态失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
