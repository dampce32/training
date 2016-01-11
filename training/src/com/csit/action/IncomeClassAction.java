package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.IncomeClass;
import com.csit.service.IncomeClassService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @Description:帐目分类表Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class IncomeClassAction extends BaseAction implements ModelDriven<IncomeClass> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(IncomeClassAction.class);
	private IncomeClass model = new IncomeClass();
	@Resource
	private IncomeClassService incomeClassService;

	@Override
	public IncomeClass getModel() {
		return model;
	}
	/**
	 * @Description: 保存帐目分类表
	 * @Create: 2013-3-28 下午04:05:00
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = incomeClassService.save(model);
		} catch (Exception e) {
			result.setMessage("保存帐目分类表失败");
			logger.error("保存帐目分类表失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 删除帐目分类表
	 * @Create: 2013-3-28 下午04:05:09
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = incomeClassService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除帐目分类表失败");
				logger.error("删除帐目分类表失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 分页查询帐目分类表
	 * @Create: 2013-3-28 下午04:05:21
	 * @author jcf
	 * @update logs
	 */
	public void query() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = incomeClassService.query(model, page, rows);
		} catch (Exception e) {
			result.setMessage("查询帐目分类表失败");
			logger.error("查询帐目分类表失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 统计帐目分类表
	 * @Create: 2013-3-28 下午04:05:28
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = incomeClassService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计帐目分类表失败");
			logger.error("统计帐目分类表失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: combobox查询
	 * @Create: 2013-3-28 下午04:05:37
	 * @author jcf
	 * @update logs
	 */
	public void queryCombobox() {
		model.setStatus(1);
		String jsonString = incomeClassService.queryCombobox(model);
		ajaxJson(jsonString);
	}
	
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = incomeClassService.updateStatus(model.getIncomeClassId(), model.getStatus());
		} catch (Exception e) {
			result.setMessage("修改帐目分类表状态失败");
			logger.error("修改帐目分类表状态失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

}
