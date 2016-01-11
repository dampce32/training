package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.SupplierDao;
import com.csit.model.Supplier;
import com.csit.service.SupplierService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

@Service
public class SupplierServiceImpl implements SupplierService {
	@Resource
	private SupplierDao supplierDao;
	public ServiceResult save(Supplier model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写供应商信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getSupplierName())){
			result.setMessage("请填写供应商");
			return result;
		}
		if(model.getSupplierId()==null){//新增
			supplierDao.save(model);
		}else{
			Supplier oldModel = supplierDao.load(model.getSupplierId());
			if(oldModel==null){
				result.setMessage("该供应商已不存在");
				return result;
			}
			oldModel.setSupplierName(model.getSupplierName());
			oldModel.setLinkMan(model.getLinkMan());
			oldModel.setTel(model.getTel());
			oldModel.setAddress(model.getAddress());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("supplierId", model.getSupplierId());
		return result;
	}
	public ServiceResult query(Supplier model, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		
		List<Supplier> list = supplierDao.query(model,page,rows);
		
		String[] properties = {"supplierId","supplierName","linkMan","tel","address","status"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	public ServiceResult getTotalCount(Supplier model) {
		ServiceResult result = new ServiceResult(false);
		Long data = supplierDao.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(Supplier model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getSupplierId()==null){
			result.setMessage("请选择要删除的供应商");
			return result;
		}
		Supplier oldModel = supplierDao.load(model.getSupplierId());
		if(oldModel==null){
			result.setMessage("该供应商已不存在");
			return result;
		}else{
			supplierDao.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	public String queryCombobox() {
		List<Supplier> list = supplierDao.query("status",1);
		String[] properties = {"supplierId","supplierName","linkMan","tel","address"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	public ServiceResult updateStatus(Supplier model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请选择供应商信息");
			return result;
		}
		Supplier oldModel = supplierDao.load(model.getSupplierId());
		if(oldModel==null){
			result.setMessage("该供应商已不存在");
			return result;
		}
		oldModel.setStatus(model.getStatus());
		result.setIsSuccess(true);
		return result;
	}
}
