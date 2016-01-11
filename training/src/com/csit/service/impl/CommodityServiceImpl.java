package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.CommodityDao;
import com.csit.dao.CommodityTypeDao;
import com.csit.dao.UnitDao;
import com.csit.model.Commodity;
import com.csit.model.CommodityType;
import com.csit.model.Unit;
import com.csit.service.CommodityService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;

@Service
public class CommodityServiceImpl implements CommodityService {
	@Resource
	private CommodityDao commodityDao;
	@Resource
	private CommodityTypeDao commodityTypeDao;
	@Resource
	private UnitDao unitDao;

	public ServiceResult save(Commodity model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写商品信息");
			return result;
		}
		if (StringUtils.isEmpty(model.getCommodityName())) {
			result.setMessage("请填写商品名称");
			return result;
		}
		if (model.getCommodityType() == null) {
			result.setMessage("请填写商品分类");
			return result;
		}
		if (model.getUnit() == null) {
			result.setMessage("请填写商品单位");
			return result;
		}
		if (model.getPurchasePrice() == null) {
			result.setMessage("请填写商品进价");
			return result;
		}
		if (model.getSalePrice() == null) {
			result.setMessage("请填写商品售价");
			return result;
		}
		if (model.getStatus() == null) {
			result.setMessage("请填写商品状态");
			return result;
		}
		if (model.getQtyWarn() == null) {
			result.setMessage("请填写商品预警数量");
			return result;
		}
		
		CommodityType commodityType = commodityTypeDao.get(model.getCommodityType().getCommodityTypeId());
		
		if(commodityType==null){
			result.setMessage("请选择已有的商品分类");
			return result;
		}
		Unit unit = unitDao.get(model.getUnit().getUnitId());
		
		if(unit==null){
			result.setMessage("请选择已有的商品单位");
			return result;
		}
		
		if (model.getCommodityId() == null) {// 新增
			model.setQtyStore(0);
			commodityDao.save(model);
		} else {
			Commodity oldModel = commodityDao.load(model.getCommodityId());
			if (oldModel == null) {
				result.setMessage("该商品已不存在");
				return result;
			}
			oldModel.setCommodityName(model.getCommodityName());
			oldModel.setStatus(model.getStatus());
			oldModel.setCommodityType(model.getCommodityType());
			oldModel.setUnit(model.getUnit());
			oldModel.setPurchasePrice(model.getPurchasePrice());
			oldModel.setSalePrice(model.getSalePrice());
			oldModel.setSize(model.getSize());
			oldModel.setQtyWarn(model.getQtyWarn());
			oldModel.setNote(model.getNote());
		}
		result.setIsSuccess(true);
		result.addData("commodityId", model.getCommodityId());
		return result;
	}

	public ServiceResult query(Commodity model, Integer page, Integer rows, String commodityIds) {

		ServiceResult result = new ServiceResult(false);
		Integer[] commodityIdArr;
		if(StringUtils.isNotEmpty(commodityIds)){
			String[] s = StringUtil.split(commodityIds);
			commodityIdArr=new Integer[s.length];
			for(int i=0;i<s.length;i++){
				commodityIdArr[i]=Integer.parseInt(s[i]);
			}
		}else{
			commodityIdArr=null;
		}
		List<Commodity> list = commodityDao.query(model, page, rows,commodityIdArr);

		String[] properties = { "commodityId", "commodityName",
				"commodityType.commodityTypeName",
				"commodityType.commodityTypeId", "unit.unitName",
				"unit.unitId", "status", "purchasePrice", "salePrice", "size",
				"qtyStore", "note" ,"qtyWarn"};

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(Commodity model, String commodityIds) {
		ServiceResult result = new ServiceResult(false);
		Integer[] commodityIdArr;
		if(StringUtils.isNotEmpty(commodityIds)){
			String[] s = StringUtil.split(commodityIds);
			commodityIdArr=new Integer[s.length];
			for(int i=0;i<s.length;i++){
				commodityIdArr[i]=Integer.parseInt(s[i]);
			}
		}else{
			commodityIdArr=null;
		}
		Long data = commodityDao.getTotalCount(model,commodityIdArr);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(Commodity model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getCommodityId() == null) {
			result.setMessage("请选择要删除的商品");
			return result;
		}
		Commodity oldModel = commodityDao.load(model.getCommodityId());
		if (oldModel == null) {
			result.setMessage("该商品已不存在");
			return result;
		} else {
			commodityDao.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public String queryCombobox() {
		List<Commodity> list = commodityDao.query("status", 1);
		String[] properties = { "CommodityId", "CommodityName", "linkMan",
				"tel", "address" };
		String jsonString = JSONUtil.toJsonWithoutRows(list, properties);
		return jsonString;
	}

	public ServiceResult updateStatus(Commodity model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请现则商品信息");
			return result;
		}
		Commodity oldModel = commodityDao.load(model.getCommodityId());
		if (oldModel == null) {
			result.setMessage("该商品已不存在");
			return result;
		}
		oldModel.setStatus(model.getStatus());
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult queryQtyWarm(Commodity model,Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<Commodity> list = commodityDao.queryQtyWarm(model,page, rows);

		String[] properties = { "commodityId", "commodityName",
				"commodityType.commodityTypeName","unit.unitName",
				"qtyStore","qtyWarn"};

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCountQtyWarm(Commodity model) {
		ServiceResult result = new ServiceResult(false);
		
		Long data = commodityDao.getTotalCountQtyWarm(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
}
