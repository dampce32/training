package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.User;
import com.csit.model.Warehouse;
import com.csit.service.WarehouseService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:仓库Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Controller
@Scope("prototype")
public class WarehouseAction extends BaseAction implements ModelDriven<Warehouse> {
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(WarehouseAction.class);
	private Warehouse model = new Warehouse();
	
	@Resource
	private WarehouseService warehouseService;
	
	public Warehouse getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存仓库
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = warehouseService.save(model);
		} catch (Exception e) {
			result.setMessage("保存仓库失败");
			logger.error("保存仓库失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询仓库
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = warehouseService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询仓库失败");
			logger.error("查询仓库失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计仓库
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = warehouseService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计仓库失败");
			logger.error("统计仓库失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 删除仓库
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = warehouseService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除仓库失败");
				logger.error("删除仓库失败", e);
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
		String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
		String jsonString = warehouseService.queryCombobox(schoolCode);
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 修改仓库状态
	 * @Created Time: 2013-2-28 下午10:57:47
	 * @Author cjp
	 */
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = warehouseService.updateStatus(model);
		} catch (Exception e) {
			result.setMessage("修改仓库状态失败");
			logger.error("修改仓库状态失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
