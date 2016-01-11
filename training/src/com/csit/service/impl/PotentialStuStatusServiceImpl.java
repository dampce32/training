package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.PotentialStuStatusDAO;
import com.csit.model.PotentialStuStatus;
import com.csit.service.PotentialStuStatusService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

/**
 * 
 * @Description:潜在生源状态Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
@Service
public class PotentialStuStatusServiceImpl extends BaseServiceImpl<PotentialStuStatus, Integer> implements PotentialStuStatusService {

	@Resource
	private PotentialStuStatusDAO potentialStuStatusDAO;

	public ServiceResult delete(PotentialStuStatus model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getPotentialStuStatusId() == null) {
			result.setMessage("请选择要删除的潜在生源状态");
			return result;
		}
		PotentialStuStatus oldModel = potentialStuStatusDAO.load(model.getPotentialStuStatusId());
		if (oldModel == null) {
			result.setMessage("该潜在生源状态已不存在");
			return result;
		} else {
			potentialStuStatusDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(PotentialStuStatus model) {
		ServiceResult result = new ServiceResult(false);
		Long data = potentialStuStatusDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult query(PotentialStuStatus model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<PotentialStuStatus> list = potentialStuStatusDAO.query(model, page, rows);

		String[] properties = { "potentialStuStatusId", "potentialStuStatusName","status" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public String queryCombobox() {
		List<PotentialStuStatus> list = potentialStuStatusDAO.query("status", 1);
		String[] properties = {"potentialStuStatusId","potentialStuStatusName","status"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}

	public ServiceResult save(PotentialStuStatus model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写潜在生源状态信息");
			return result;
		}
		if (StringUtils.isEmpty(model.getPotentialStuStatusName())) {
			result.setMessage("请填写潜在生源状态名称");
			return result;
		}
		if (model.getPotentialStuStatusId() == null) {// 新增
			potentialStuStatusDAO.save(model);
		} else {
			PotentialStuStatus oldModel = potentialStuStatusDAO.load(model.getPotentialStuStatusId());
			if (oldModel == null) {
				result.setMessage("该潜在生源状态已不存在");
				return result;
			}
			oldModel.setPotentialStuStatusName(model.getPotentialStuStatusName());
		}
		result.setIsSuccess(true);
		result.addData("potentialStuStatusId", model.getPotentialStuStatusId());
		return result;
	}

	@Override
	public ServiceResult updateStatus(Integer potentialStuStatusId, Integer state) {
		ServiceResult result = new ServiceResult(false);
		if (potentialStuStatusId == null) {
			result.setMessage("请选择要修改的潜在生源状态");
			return result;
		}
		if (state == null) {
			result.setMessage("请选择要修改的状态");
			return result;
		}
		PotentialStuStatus oldModel=potentialStuStatusDAO.load(potentialStuStatusId);
		if(oldModel==null){
			result.setMessage("对不起，你要修改的媒体信息已不存在");
			return result;
		}
		if(oldModel.getStatus().intValue()==state.intValue()){
			result.setMessage("没有可修改的潜在生源状态信息");
			return result;
		}
		else {
			oldModel.setStatus(state);
			potentialStuStatusDAO.update(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

}
