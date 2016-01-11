package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.CommodityTypeDao;
import com.csit.model.CommodityType;
import com.csit.service.CommodityTypeService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

@Service
public class CommodityTypeServiceImpl implements CommodityTypeService {
	@Resource
	private CommodityTypeDao commodityTypeDao;
	public ServiceResult save(CommodityType model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写商品分类信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getCommodityTypeName())){
			result.setMessage("请填写商品分类");
			return result;
		}
		if(model.getCommodityTypeId()==null){//新增
			commodityTypeDao.save(model);
		}else{
			CommodityType oldModel = commodityTypeDao.load(model.getCommodityTypeId());
			if(oldModel==null){
				result.setMessage("该商品分类已不存在");
				return result;
			}
			oldModel.setCommodityTypeName(model.getCommodityTypeName());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("commodityTypeId", model.getCommodityTypeId());
		return result;
	}
	public ServiceResult query(CommodityType model, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		
		List<CommodityType> list = commodityTypeDao.query(model,page,rows);
		
		String[] properties = {"commodityTypeId","commodityTypeName","status"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	public ServiceResult getTotalCount(CommodityType model) {
		ServiceResult result = new ServiceResult(false);
		Long data = commodityTypeDao.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(CommodityType model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getCommodityTypeId()==null){
			result.setMessage("请选择要删除的课程类型");
			return result;
		}
		CommodityType oldModel = commodityTypeDao.load(model.getCommodityTypeId());
		if(oldModel==null){
			result.setMessage("该商品分类已不存在");
			return result;
		}else{
			commodityTypeDao.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	public String queryCombobox() {
		List<CommodityType> list = commodityTypeDao.query("status",1);
		String[] properties = {"commodityTypeId","commodityTypeName","status"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	public ServiceResult updateStatus(CommodityType model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请选择商品单位信息");
			return result;
		}
		CommodityType oldModel = commodityTypeDao.load(model.getCommodityTypeId());
		if(oldModel==null){
			result.setMessage("该商品已不存在");
			return result;
		}
		oldModel.setStatus(model.getStatus());
		result.setIsSuccess(true);
		return result;
	}
}
