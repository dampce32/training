package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Unit;
import com.csit.service.UnitService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:商品单位Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author lys
 */
@Controller
@Scope("prototype")
public class UnitAction extends BaseAction implements ModelDriven<Unit> {
	
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(UnitAction.class);
	private Unit model = new Unit();
	
	@Resource
	private UnitService unitService;
	
	public Unit getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存课程类型
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = unitService.save(model);
		} catch (Exception e) {
			result.setMessage("保存商品单位失败");
			logger.error("保存商品单位失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询商品单位
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = unitService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询商品单位失败");
			logger.error("查询商品单位失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计商品单位
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = unitService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计商品单位失败");
			logger.error("统计商品单位失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 删除商品单位
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = unitService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除商品单位失败");
				logger.error("删除商品单位失败", e);
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
		String jsonString = unitService.queryCombobox();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 修改商品单位状态
	 * @Created Time: 2013-2-28 下午10:57:47
	 * @Author cjp
	 */
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = unitService.updateStatus(model);
		} catch (Exception e) {
			result.setMessage("修改商品单位状态失败");
			logger.error("修改商品单位状态失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
}
