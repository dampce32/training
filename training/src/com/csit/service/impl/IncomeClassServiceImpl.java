package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.IncomeClassDAO;
import com.csit.model.IncomeClass;
import com.csit.service.IncomeClassService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

/**
 * @Description:帐目分类表Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Service
public class IncomeClassServiceImpl extends BaseServiceImpl<IncomeClass, Integer> implements IncomeClassService {

	@Resource
	private IncomeClassDAO incomeClassDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#delete(com.csit.model.Media)
	 */
	public ServiceResult delete(IncomeClass model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getIncomeClassId() == null) {
			result.setMessage("请选择要删除的帐目分类表");
			return result;
		}
		IncomeClass oldModel = incomeClassDAO.load(model.getIncomeClassId());
		if (oldModel == null) {
			result.setMessage("该帐目分类表已不存在");
			return result;
		} else {
			incomeClassDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(IncomeClass model) {
		ServiceResult result = new ServiceResult(false);
		Long data = incomeClassDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#query(com.csit.model.Media, java.lang.Integer, java.lang.Integer)
	 */
	public ServiceResult query(IncomeClass model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<IncomeClass> list = incomeClassDAO.query(model, page, rows);

		String[] properties = { "incomeClassId", "incomeClassName","status","type" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public String queryCombobox(IncomeClass model) {
		List<IncomeClass> list = incomeClassDAO.queryByType(model);
		String[] properties = {"incomeClassId","incomeClassName","status","type"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}

	public ServiceResult save(IncomeClass model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写帐目分类表信息");
			return result;
		}
		if (StringUtils.isEmpty(model.getIncomeClassName())) {
			result.setMessage("请填写帐目分类表名称");
			return result;
		}
		if (model.getIncomeClassId() == null) {// 新增
			incomeClassDAO.save(model);
		} else {
			IncomeClass oldModel = incomeClassDAO.load(model.getIncomeClassId());
			if (oldModel == null) {
				result.setMessage("该帐目分类表已不存在");
				return result;
			}
			oldModel.setIncomeClassName(model.getIncomeClassName());
			oldModel.setStatus(model.getStatus());
			oldModel.setType(model.getType());
		}
		result.setIsSuccess(true);
		result.addData("incomeClassId", model.getIncomeClassId());
		return result;
	}

	@Override
	public ServiceResult updateStatus(Integer incomeClassId,Integer state) {
		ServiceResult result = new ServiceResult(false);
		if (incomeClassId == null) {
			result.setMessage("请选择要修改的帐目分类表信息");
			return result;
		}
		if (state == null) {
			result.setMessage("请选择要修改的状态");
			return result;
		}
		IncomeClass oldModel=incomeClassDAO.load(incomeClassId);
		if(oldModel==null){
			result.setMessage("对不起，你要修改的帐目分类表信息已不存在");
			return result;
		}
		if(oldModel.getStatus().intValue()==state.intValue()){
			result.setMessage("没有可修改的帐目分类表信息");
			return result;
		}
		else {
			oldModel.setStatus(state);
		}
		result.setIsSuccess(true);
		return result;
	}

}
