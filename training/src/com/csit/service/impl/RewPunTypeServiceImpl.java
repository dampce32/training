package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.RewPunTypeDao;
import com.csit.model.RewPunType;
import com.csit.service.RewPunTypeService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

@Service
public class RewPunTypeServiceImpl implements RewPunTypeService {
	@Resource
	private RewPunTypeDao rewPunTypeDao;
	public ServiceResult save(RewPunType model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写奖惩分类信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getRewPunTypeName())){
			result.setMessage("请填写奖惩分类");
			return result;
		}
		if(model.getRewPunTypeId()==null){//新增
			rewPunTypeDao.save(model);
		}else{
			RewPunType oldModel = rewPunTypeDao.load(model.getRewPunTypeId());
			if(oldModel==null){
				result.setMessage("该奖惩分类已不存在");
				return result;
			}
			oldModel.setRewPunTypeName(model.getRewPunTypeName());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("rewPunTypeId", model.getRewPunTypeId());
		return result;
	}
	public ServiceResult query(RewPunType model, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		
		List<RewPunType> list = rewPunTypeDao.query(model,page,rows);
		
		String[] properties = {"rewPunTypeId","rewPunTypeName","status"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	public ServiceResult getTotalCount(RewPunType model) {
		ServiceResult result = new ServiceResult(false);
		Long data = rewPunTypeDao.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(RewPunType model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getRewPunTypeId()==null){
			result.setMessage("请选择要删除的奖惩类型");
			return result;
		}
		RewPunType oldModel = rewPunTypeDao.load(model.getRewPunTypeId());
		if(oldModel==null){
			result.setMessage("该奖惩分类已不存在");
			return result;
		}else{
			rewPunTypeDao.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	public String queryCombobox() {
		List<RewPunType> list = rewPunTypeDao.query("status",1);
		String[] properties = {"rewPunTypeId","rewPunTypeName","status"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	public ServiceResult updateStatus(RewPunType model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请选择奖惩单位信息");
			return result;
		}
		RewPunType oldModel = rewPunTypeDao.load(model.getRewPunTypeId());
		if(oldModel==null){
			result.setMessage("该奖惩已不存在");
			return result;
		}
		oldModel.setStatus(model.getStatus());
		result.setIsSuccess(true);
		return result;
	}
}
