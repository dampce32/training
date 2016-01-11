package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Account;
import com.csit.service.AccountService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @Description:帐户表Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class AccountAction extends BaseAction implements ModelDriven<Account> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(AccountAction.class);
	private Account model = new Account();
	@Resource
	private AccountService accountService;

	@Override
	public Account getModel() {
		return model;
	}
	/**
	 * @Description: 保存帐户表
	 * @Create: 2013-3-28 下午04:05:00
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = accountService.save(model);
		} catch (Exception e) {
			result.setMessage("保存帐户表失败");
			logger.error("保存帐户表失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 删除帐户表
	 * @Create: 2013-3-28 下午04:05:09
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = accountService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除帐户表失败");
				logger.error("删除帐户表失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 分页查询帐户表
	 * @Create: 2013-3-28 下午04:05:21
	 * @author jcf
	 * @update logs
	 */
	public void query() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = accountService.query(model, page, rows);
		} catch (Exception e) {
			result.setMessage("查询帐户表失败");
			logger.error("查询帐户表失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 统计帐户表
	 * @Create: 2013-3-28 下午04:05:28
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = accountService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计帐户表失败");
			logger.error("统计帐户表失败", e);
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
		String jsonString = accountService.queryCombobox();
		ajaxJson(jsonString);
	}
	
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = accountService.updateStatus(model.getAccountId(), model.getStatus());
		} catch (Exception e) {
			result.setMessage("修改帐户表状态失败");
			logger.error("修改帐户表状态失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

}
