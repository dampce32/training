package com.csit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.IncomeDAO;
import com.csit.model.Income;
import com.csit.service.IncomeService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

/**
 * @Description:记帐表Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-29
 * @author jcf
 * @vesion 1.0
 */
@Service
public class IncomeServiceImpl extends BaseServiceImpl<Income, Integer> implements IncomeService {

	@Resource
	private IncomeDAO incomeDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.IncomeService#delete(com.csit.model.Income)
	 */
	public ServiceResult delete(Income model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getIncomeId() == null) {
			result.setMessage("请选择要删除的记帐表");
			return result;
		}
		Income oldModel = incomeDAO.load(model.getIncomeId());
		if (oldModel == null) {
			result.setMessage("该记帐表已不存在");
			return result;
		} else {
			incomeDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.IncomeService#getTotalCount(com.csit.model.Income)
	 */
	public ServiceResult getTotalCount(Income model,Date accountDateBegin,Date accountDateEnd) {
		ServiceResult result = new ServiceResult(false);
		Long data = incomeDAO.getTotalCount(model,accountDateBegin,accountDateEnd);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.IncomeService#query(com.csit.model.Income, java.lang.Integer, java.lang.Integer)
	 */
	public ServiceResult query(Income model,Date accountDateBegin,Date accountDateEnd, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<Income> list = incomeDAO.query(model,accountDateBegin,accountDateEnd, page, rows);

		String[] properties = { "incomeId", "school.schoolName","school.schoolId","account.accountName","account.accountId","user.userName","user.userId","incomeClass.incomeClassName","incomeClass.incomeClassId"
				,"accountDate","price","note","incomeClass.type" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}


	public ServiceResult save(Income model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写记帐表信息");
			return result;
		}
		if(model.getAccountDate()==null){
			result.setMessage("请填写日期");
			return result;
		}
		if(model.getSchool()==null&&model.getSchool().getSchoolId()==null){
			result.setMessage("请选择校区");
			return result;
		}
		if(model.getUser()==null&&model.getUser().getUserId()==null){
			result.setMessage("请选择经办人");
			return result;
		}
		if(model.getIncomeClass()==null&&model.getIncomeClass().getIncomeClassId()==null){
			result.setMessage("请选择项目分类");
			return result;
		}
		if(model.getPrice()==null){
			result.setMessage("请填写金额");
			return result;
		}
		if(model.getAccount()==null&&model.getAccount().getAccountId()==null){
			result.setMessage("请选择资金账户");
			return result;
		}
		if (model.getIncomeId() == null) {// 新增
			model.setInsertDate(new Date());
			incomeDAO.save(model);
		} else {
			Income oldModel = incomeDAO.load(model.getIncomeId());
			if (oldModel == null) {
				result.setMessage("该记帐表已不存在");
				return result;
			}
			oldModel.setAccountDate(model.getAccountDate());
			oldModel.setAccount(model.getAccount());
			oldModel.setIncomeClass(model.getIncomeClass());
			oldModel.setNote(model.getNote());
			oldModel.setPrice(model.getPrice());
			oldModel.setSchool(model.getSchool());
			oldModel.setUser(model.getUser());
		}
		result.setIsSuccess(true);
		result.addData("incomeId", model.getIncomeId());
		return result;
	}

}
