package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.SchoolDAO;
import com.csit.dao.WarehouseDao;
import com.csit.model.School;
import com.csit.model.Warehouse;
import com.csit.service.WarehouseService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

@Service
public class WarehouseServiceImpl implements WarehouseService {
	@Resource
	private WarehouseDao warehouseDao;
	
	@Resource
	private SchoolDAO schoolDao;
	
	public ServiceResult save(Warehouse model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写仓库信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getWarehouseName())){
			result.setMessage("请填写仓库");
			return result;
		}
		if(model.getSchool()==null){
			result.setMessage("请填写仓库所属校区");
			return result;
		}
		if(model.getStatus()==null){
			result.setMessage("请填写仓库状态");
			return result;
		}
		
		School school=schoolDao.get(model.getSchool().getSchoolId());
		
		if(school==null){
			result.setMessage("请选择已有校区");
			return result;
		}
		
		if(model.getWarehouseId()==null){//新增
			warehouseDao.save(model);
		}else{
			Warehouse oldModel = warehouseDao.load(model.getWarehouseId());
			if(oldModel==null){
				result.setMessage("该仓库已不存在");
				return result;
			}
			oldModel.setWarehouseName(model.getWarehouseName());
			oldModel.setTel(model.getTel());
			oldModel.setAddress(model.getAddress());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("warehouseId", model.getWarehouseId());
		return result;
	}
	public ServiceResult query(Warehouse model, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		
		List<Warehouse> list = warehouseDao.query(model,page,rows);
		
		String[] properties = {"warehouseId","warehouseName","tel","address","status","school.schoolName","school.schoolId"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	public ServiceResult getTotalCount(Warehouse model) {
		ServiceResult result = new ServiceResult(false);
		Long data = warehouseDao.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(Warehouse model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getWarehouseId()==null){
			result.setMessage("请选择要删除的仓库");
			return result;
		}
		Warehouse oldModel = warehouseDao.load(model.getWarehouseId());
		if(oldModel==null){
			result.setMessage("该仓库已不存在");
			return result;
		}else{
			warehouseDao.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	public String queryCombobox() {
		List<Warehouse> list = warehouseDao.query("status",1);
		String[] properties = {"warehouseId","warehouseName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	public ServiceResult updateStatus(Warehouse model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请选择仓库信息");
			return result;
		}
		Warehouse oldModel = warehouseDao.load(model.getWarehouseId());
		if(oldModel==null){
			result.setMessage("该仓库已不存在");
			return result;
		}
		oldModel.setStatus(model.getStatus());
		result.setIsSuccess(true);
		return result;
	}
	@Override
	public String queryCombobox(String schoolCode) {

		List<Warehouse> list = warehouseDao.querySelfCombobox(schoolCode);
		String[] properties = {"warehouseId","warehouseName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
}
