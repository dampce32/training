package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.UnitDao;
import com.csit.model.Unit;
import com.csit.service.UnitService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

@Service
public class UnitServiceImpl implements UnitService {
	@Resource
	private UnitDao unitDao;
	public ServiceResult save(Unit model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写商品单位信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getUnitName())){
			result.setMessage("请填写商品单位");
			return result;
		}
		if(model.getUnitId()==null){//新增
			unitDao.save(model);
		}else{
			Unit oldModel = unitDao.load(model.getUnitId());
			if(oldModel==null){
				result.setMessage("该商品单位已不存在");
				return result;
			}
			oldModel.setUnitName(model.getUnitName());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("unitId", model.getUnitId());
		return result;
	}
	public ServiceResult query(Unit model, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		
		List<Unit> list = unitDao.query(model,page,rows);
		
		String[] properties = {"unitId","unitName","status"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	public ServiceResult getTotalCount(Unit model) {
		ServiceResult result = new ServiceResult(false);
		Long data = unitDao.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(Unit model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getUnitId()==null){
			result.setMessage("请选择要删除的课程类型");
			return result;
		}
		Unit oldModel = unitDao.load(model.getUnitId());
		if(oldModel==null){
			result.setMessage("该商品单位已不存在");
			return result;
		}else{
			unitDao.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	public String queryCombobox() {
		List<Unit> list = unitDao.query("status",1);
		String[] properties = {"unitId","unitName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	public ServiceResult updateStatus(Unit model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请选择商品单位信息");
			return result;
		}
		Unit oldModel = unitDao.load(model.getUnitId());
		if(oldModel==null){
			result.setMessage("该商品已不存在");
			return result;
		}
		oldModel.setStatus(model.getStatus());
		result.setIsSuccess(true);
		return result;
	}
}
