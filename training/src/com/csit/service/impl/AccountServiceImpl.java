package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.AccountDAO;
import com.csit.model.Account;
import com.csit.service.AccountService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

/**
 * @Description:帐户表Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Service
public class AccountServiceImpl extends BaseServiceImpl<Account, Integer> implements AccountService {

	@Resource
	private AccountDAO accountDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#delete(com.csit.model.Media)
	 */
	public ServiceResult delete(Account model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getAccountId() == null) {
			result.setMessage("请选择要删除的帐户表");
			return result;
		}
		Account oldModel = accountDAO.load(model.getAccountId());
		if (oldModel == null) {
			result.setMessage("该帐户表已不存在");
			return result;
		} else {
			accountDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(Account model) {
		ServiceResult result = new ServiceResult(false);
		Long data = accountDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#query(com.csit.model.Media, java.lang.Integer, java.lang.Integer)
	 */
	public ServiceResult query(Account model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<Account> list = accountDAO.query(model, page, rows);

		String[] properties = { "accountId", "accountName","status","note" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public String queryCombobox() {
		List<Account> list = accountDAO.query("status", 1);
		String[] properties = {"accountId","accountName","status","note"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}

	public ServiceResult save(Account model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写帐户表信息");
			return result;
		}
		if (StringUtils.isEmpty(model.getAccountName())) {
			result.setMessage("请填写帐户表名称");
			return result;
		}
		if (model.getAccountId() == null) {// 新增
			accountDAO.save(model);
		} else {
			Account oldModel = accountDAO.load(model.getAccountId());
			if (oldModel == null) {
				result.setMessage("该帐户表已不存在");
				return result;
			}
			oldModel.setAccountName(model.getAccountName());
			oldModel.setNote(model.getNote());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("accountId", model.getAccountId());
		return result;
	}

	@Override
	public ServiceResult updateStatus(Integer accountId,Integer state) {
		ServiceResult result = new ServiceResult(false);
		if (accountId == null) {
			result.setMessage("请选择要修改的帐户表信息");
			return result;
		}
		if (state == null) {
			result.setMessage("请选择要修改的状态");
			return result;
		}
		Account oldModel=accountDAO.load(accountId);
		if(oldModel==null){
			result.setMessage("对不起，你要修改的帐户表信息已不存在");
			return result;
		}
		if(oldModel.getStatus().intValue()==state.intValue()){
			result.setMessage("没有可修改的帐户表信息");
			return result;
		}
		else {
			oldModel.setStatus(state);
		}
		result.setIsSuccess(true);
		return result;
	}

}
