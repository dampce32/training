package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.StoreDao;
import com.csit.model.RecRej;
import com.csit.model.Store;
import com.csit.model.StoreTuneOut;
import com.csit.service.StoreService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

@Service
public class StoreServiceImpl implements StoreService {
	
	@Resource
	private StoreDao storeDao; 

	public ServiceResult query(Store model, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		List<RecRej> list = storeDao.query(model, page, rows);
		String[] properties = { "commodity.commodityName","commodity.commodityId","qtyStore", "warehouse.warehouseName", "warehouse.warehouseId", "commodity.qtyWarn",
				 "unitName", "commodityTypeName", "qtyTotal" };
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(Store model) {
		
		ServiceResult result = new ServiceResult(false);
		Long data = storeDao.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult queryNotEmpty(Store model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		List<Store> list = storeDao.queryNotEmpty(model,page, rows);
		String[] properties = { "commodity.commodityName","commodity.commodityId","qtyStore", "warehouse.warehouseName", "warehouse.warehouseId", "commodity.qtyWarn",
				 "unitName", "commodityTypeName", "qtyTotal" };
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCountNotEmpty(Store model) {
		ServiceResult result = new ServiceResult(false);
		Long data = storeDao.getTotalCountNotEmpty(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

}
