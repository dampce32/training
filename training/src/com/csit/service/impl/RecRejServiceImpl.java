package com.csit.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.CommodityDao;
import com.csit.dao.RecRejDao;
import com.csit.dao.RecRejDetailDao;
import com.csit.dao.StoreDao;
import com.csit.dao.SupplierDao;
import com.csit.dao.UserDAO;
import com.csit.dao.WarehouseDao;
import com.csit.model.Commodity;
import com.csit.model.RecRej;
import com.csit.model.RecRejDetail;
import com.csit.model.Store;
import com.csit.model.Supplier;
import com.csit.model.User;
import com.csit.model.Warehouse;
import com.csit.service.RecRejService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;

@Service
public class RecRejServiceImpl implements RecRejService {
	
	@Resource
	private RecRejDao recRejDao; 
	@Resource
	private StoreDao storeDao; 
	@Resource
	private RecRejDetailDao recRejDetailDao; 
	@Resource
	private WarehouseDao warehouseDao; 
	@Resource
	private CommodityDao commodityDao; 
	@Resource
	private SupplierDao supplierDao; 
	@Resource
	private UserDAO userDao; 

	public ServiceResult saveReceive(RecRej model, String receiveDetailIds, String commodityIds, String warehouseDetailIds, String qtys, String purchasePrices, String totalPrices, String delReceiveDetailIds) {
		ServiceResult result = new ServiceResult(false);
		
		if (model == null) {
			result.setMessage("请填写入库单信息");
			return result;
		}
		if (model.getRecRejDate() == null) {
			result.setMessage("请填写日期");
			return result;
		}
		if (model.getSupplier() == null) {
			result.setMessage("请选择供应商");
			return result;
		}
		Supplier supplier=supplierDao.get(model.getSupplier().getSupplierId());
		if(supplier==null){
			result.setMessage("请选择已有的供应商");
			return result;
		}
		if(model.getUser()==null){
			result.setMessage("请选择经办人");
			return result;
		}
		User user=userDao.get(model.getUser().getUserId());
		if( user==null){
			result.setMessage("请选择已有的经办人");
			return result;
		}
		
		String[] commodityIdArr = StringUtil.split(commodityIds);
		String[] warehouseDetailIdArr = StringUtil.split(warehouseDetailIds);
		String[] qtysArr = StringUtil.split(qtys);
		String[] purchasePricesArr = StringUtil.split(purchasePrices);
		String[] receiveDetailIdArr = StringUtil.split(receiveDetailIds);
		String[] delReceiveDetailIdArr = StringUtil.split(delReceiveDetailIds);
		for(int i=0;i<commodityIdArr.length;i++){
			
			if(StringUtils.isEmpty(purchasePricesArr[i])){
				result.setMessage("第"+(i+1)+"行数据没有填写单价");
				return result;
			}
			if(StringUtils.isEmpty(qtysArr[i])){
				result.setMessage("第"+(i+1)+"行数据没有填写数量");
				return result;
			}
			if(StringUtils.isEmpty(warehouseDetailIdArr[i])){
				result.setMessage("第"+(i+1)+"行数据没有选择仓库");
				return result;
			}
			Warehouse warehouse=warehouseDao.get(Integer.parseInt(warehouseDetailIdArr[i]));
			if(warehouse==null){
				result.setMessage("第"+(i+1)+"行数据没有选择已有仓库");
				return result;
			}
		}
		boolean isAdd=true;
		if(model.getRecRejId()==null){
			recRejDao.save(model);
		}else{
			RecRej oldModel =recRejDao.load(model.getRecRejId());
			if (oldModel == null) {
				result.setMessage("该入库单已不存在");
				return result;
			}
			oldModel.setRecRejDate(model.getRecRejDate());
			oldModel.setSupplier(model.getSupplier());
			oldModel.setUser(model.getUser());
			oldModel.setQtyTotal(model.getQtyTotal());
			oldModel.setAmountTotal(model.getAmountTotal());
			oldModel.setNote(model.getNote());
			
			if(delReceiveDetailIdArr.length!=0){
				for(String detailId:delReceiveDetailIdArr){
					if(StringUtils.isNotEmpty(detailId)){
						RecRejDetail recRejDetail=recRejDetailDao.load(Integer.parseInt(detailId));
						Commodity commodity=recRejDetail.getCommodity();
						Warehouse warehouse=recRejDetail.getWarehouse();
						
						Store store = storeDao.query(commodity, warehouse);
						store.setQtyStore(store.getQtyStore()-recRejDetail.getQty());
						commodity.setQtyStore(commodity.getQtyStore()-recRejDetail.getQty());
						
						recRejDetailDao.delete(recRejDetail.getRecRejDetailId());
					}
				}
			}
			isAdd=false;
		}
		for(int i=0;i<commodityIdArr.length;i++){
			
			String commodityId=commodityIdArr[i];
			Commodity commodity = commodityDao.load(Integer.parseInt(commodityId));
			Warehouse warehouse=warehouseDao.get(Integer.parseInt(warehouseDetailIdArr[i]));
			
			Store store = storeDao.query(commodity, warehouse);
			
			if(isAdd){
				//增加
				RecRejDetail recRejDetail=new RecRejDetail();
				recRejDetail.setCommodity(commodity);
				recRejDetail.setWarehouse(warehouse);
				recRejDetail.setPrice(Double.parseDouble(purchasePricesArr[i]));
				recRejDetail.setQty(Integer.parseInt(qtysArr[i]));
				recRejDetail.setRecRej(model);
				recRejDetailDao.save(recRejDetail);
				if(store==null){
					//添加库存
					store=new Store();
					store.setCommodity(commodity);
					store.setWarehouse(warehouse);
					store.setQtyStore(Integer.parseInt(qtysArr[i]));
					storeDao.save(store);
				}else{
					store.setQtyStore(store.getQtyStore()+Integer.parseInt(qtysArr[i]));
				}
				commodity.setQtyStore(commodity.getQtyStore()+Integer.parseInt(qtysArr[i]));
			}else{
				//修改
				if(store==null){
					//添加库存
					store=new Store();
					store.setCommodity(commodity);
					store.setWarehouse(warehouse);
					store.setQtyStore(Integer.parseInt(qtysArr[i]));
					storeDao.save(store);
				}else{
					RecRejDetail oldDetail =new RecRejDetail();
					Integer subtractQty=0;
					if(StringUtils.isNotEmpty(receiveDetailIdArr[i])){
						oldDetail = recRejDetailDao.load(Integer.parseInt(receiveDetailIdArr[i]));
						//修改库存
						subtractQty=(Integer.parseInt(qtysArr[i]))-oldDetail.getQty().intValue();
						
						if(!(warehouse.getWarehouseId().equals(oldDetail.getWarehouse().getWarehouseId()))){
							//修改仓库时，先把旧仓库的库存减去，再加上新仓库库存
							Store oldStore=storeDao.query(oldDetail.getCommodity(), oldDetail.getWarehouse());
							if((oldStore.getQtyStore()-oldDetail.getQty())<0){
								throw new RuntimeException("商品"+oldStore.getCommodity().getCommodityName()+"数量溢出");
							}
							oldStore.setQtyStore(oldStore.getQtyStore()-oldDetail.getQty());
							subtractQty=Integer.parseInt(qtysArr[i]);
							commodity.setQtyStore(commodity.getQtyStore()-oldDetail.getQty());
						}else{
							if((store.getQtyStore()+subtractQty)<0){
								throw new RuntimeException("商品"+store.getCommodity().getCommodityName()+"数量溢出");
							}
						}
					}else{
						oldDetail.setCommodity(commodity);
						oldDetail.setRecRej(model);
						subtractQty=Integer.parseInt(qtysArr[i]);
					}
					
					//修改详细单
					oldDetail.setQty(Integer.parseInt(qtysArr[i]));
					oldDetail.setPrice(Double.parseDouble(purchasePricesArr[i]));
					oldDetail.setWarehouse(warehouse);
					
					store.setQtyStore(store.getQtyStore()+subtractQty);
					commodity.setQtyStore(commodity.getQtyStore()+subtractQty);
					if(oldDetail.getRecRejDetailId()==null){
						recRejDetailDao.save(oldDetail);
					}
				}
			}
		}
		result.setIsSuccess(true);
		result.addData("recRejId", model.getRecRejId());
		return result;
	}

	public ServiceResult query(RecRej model, String beginDate, String endDate, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		List<RecRej> list = recRejDao.query(model,beginDate,endDate, page, rows);
		String[] properties = { "recRejId","recRejCode", "recRejDate", "supplier.supplierName","supplier.supplierId",
				"user.userName","user.userId","recRejCode", "qtyTotal", "amountTotal", "note" };
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(RecRej model, String beginDate, String endDate) {
		ServiceResult result = new ServiceResult(false);
		Long data = recRejDao.getTotalCount(model,beginDate,endDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(String recrRejIds) {
		ServiceResult result = new ServiceResult(false);
		if (StringUtils.isEmpty(recrRejIds)) {
			result.setMessage("请选择要删除的单子");
			return result;
		}
		String[] recRejArr=StringUtil.split(recrRejIds);
		for(String recRejId:recRejArr){
			RecRej oldModel = recRejDao.load(Integer.parseInt(recRejId));
			if (oldModel == null) {
				result.setMessage("该单子已不存在");
				return result;
			} else {
				List<RecRejDetail> detailList=recRejDetailDao.query("recRej", oldModel);
				for(RecRejDetail recRejDetail:detailList){
					Store store=storeDao.query(recRejDetail.getCommodity(), recRejDetail.getWarehouse());
					Commodity commodity=commodityDao.load(recRejDetail.getCommodity().getCommodityId());
					if(oldModel.getRecRejType().equals(1)){
						if(store.getQtyStore()-recRejDetail.getQty()<0){
							throw new RuntimeException("商品"+recRejDetail.getCommodity().getCommodityName()+"数量溢出");
						}
						store.setQtyStore(store.getQtyStore()-recRejDetail.getQty());
						commodity.setQtyStore(commodity.getQtyStore()-recRejDetail.getQty());
					}else{
						store.setQtyStore(store.getQtyStore()+recRejDetail.getQty());
						commodity.setQtyStore(commodity.getQtyStore()+recRejDetail.getQty());
					}
					recRejDetailDao.delete(recRejDetail);
				}
				recRejDao.delete(oldModel);
			}
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult initCode() {
		ServiceResult result = new ServiceResult(false);
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		
		Long data = recRejDao.initCode(date);
		result.addData("recRejCode", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult queryDetail(RecRej model) {
		ServiceResult result = new ServiceResult(false);
		List<RecRejDetail> list = recRejDetailDao.query("recRej",model);
		String[] properties = { "recRejDetailId","commodity.commodityId","commodity.commodityName", "price","totalPrice", "qty", "warehouse.warehouseId", "commodityTypeName", "unitName"
				};
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult saveReject(RecRej model, String receiveDetailIds,
			String commodityIds, String warehouseDetailIds, String qtys,
			String purchasePrices, String totalPrices,
			String delReceiveDetailIds) {
		ServiceResult result = new ServiceResult(false);
		
		if (model == null) {
			result.setMessage("请填写入库单信息");
			return result;
		}
		if (model.getRecRejDate() == null) {
			result.setMessage("请填写日期");
			return result;
		}
		if (model.getSupplier() == null) {
			result.setMessage("请选择供应商");
			return result;
		}
		Supplier supplier=supplierDao.get(model.getSupplier().getSupplierId());
		if(supplier==null){
			result.setMessage("请选择已有的供应商");
			return result;
		}
		if(model.getUser()==null){
			result.setMessage("请选择经办人");
			return result;
		}
		User user=userDao.get(model.getUser().getUserId());
		if( user==null){
			result.setMessage("请选择已有的经办人");
			return result;
		}
		
		String[] commodityIdArr = StringUtil.split(commodityIds);
		String[] warehouseDetailIdArr = StringUtil.split(warehouseDetailIds);
		String[] qtysArr = StringUtil.split(qtys);
		String[] purchasePricesArr = StringUtil.split(purchasePrices);
		String[] receiveDetailIdArr = StringUtil.split(receiveDetailIds);
		String[] delReceiveDetailIdArr = StringUtil.split(delReceiveDetailIds);
		for(int i=0;i<commodityIdArr.length;i++){
			
			if(StringUtils.isEmpty(purchasePricesArr[i])){
				result.setMessage("第"+(i+1)+"行数据没有填写单价");
				return result;
			}
			if(StringUtils.isEmpty(qtysArr[i])){
				result.setMessage("第"+(i+1)+"行数据没有填写数量");
				return result;
			}
			if(StringUtils.isEmpty(warehouseDetailIdArr[i])){
				result.setMessage("第"+(i+1)+"行数据没有选择仓库");
				return result;
			}
			Warehouse warehouse=warehouseDao.get(Integer.parseInt(warehouseDetailIdArr[i]));
			if(warehouse==null){
				result.setMessage("第"+(i+1)+"行数据没有选择已有仓库");
				return result;
			}
		}
		
		boolean isAdd=true;
		if(model.getRecRejId()==null){
			recRejDao.save(model);
		}else{
			RecRej oldModel =recRejDao.load(model.getRecRejId());
			if (oldModel == null) {
				result.setMessage("该出库单已不存在");
				return result;
			}
			oldModel.setRecRejDate(model.getRecRejDate());
			oldModel.setSupplier(model.getSupplier());
			oldModel.setUser(model.getUser());
			oldModel.setQtyTotal(model.getQtyTotal());
			oldModel.setAmountTotal(model.getAmountTotal());
			oldModel.setNote(model.getNote());
			
			if(delReceiveDetailIdArr.length!=0){
				for(String detailId:delReceiveDetailIdArr){
					if(StringUtils.isNotEmpty(detailId)){
						RecRejDetail recRejDetail=recRejDetailDao.load(Integer.parseInt(detailId));
						Commodity commodity=recRejDetail.getCommodity();
						Warehouse warehouse=recRejDetail.getWarehouse();
						
						Store store = storeDao.query(commodity, warehouse);
						store.setQtyStore(store.getQtyStore()+recRejDetail.getQty());
						commodity.setQtyStore(commodity.getQtyStore()+recRejDetail.getQty());
						
						recRejDetailDao.delete(recRejDetail.getRecRejDetailId());
					}
				}
			}
			isAdd=false;
		}
		for(int i=0;i<commodityIdArr.length;i++){
			
			String commodityId=commodityIdArr[i];
			Commodity commodity = commodityDao.load(Integer.parseInt(commodityId));
			Warehouse warehouse=warehouseDao.get(Integer.parseInt(warehouseDetailIdArr[i]));
			
			Store store = storeDao.query(commodity, warehouse);
			
			if(isAdd){
				//增加
				RecRejDetail recRejDetail=new RecRejDetail();
				recRejDetail.setCommodity(commodity);
				recRejDetail.setWarehouse(warehouse);
				recRejDetail.setPrice(Double.parseDouble(purchasePricesArr[i]));
				recRejDetail.setQty(Integer.parseInt(qtysArr[i]));
				recRejDetail.setRecRej(model);
				recRejDetailDao.save(recRejDetail);
				if(store.getQtyStore()-Integer.parseInt(qtysArr[i])<0){
					throw new RuntimeException("商品"+store.getCommodity().getCommodityName()+"在仓库"+store.getWarehouse().getWarehouseName()+"数量不足");
				}
				store.setQtyStore(store.getQtyStore()-Integer.parseInt(qtysArr[i]));
				commodity.setQtyStore(commodity.getQtyStore()-Integer.parseInt(qtysArr[i]));
			}else{
				//修改
				RecRejDetail oldDetail =new RecRejDetail();
				Integer subtractQty=0;
				if(StringUtils.isNotEmpty(receiveDetailIdArr[i])){
					oldDetail = recRejDetailDao.load(Integer.parseInt(receiveDetailIdArr[i]));
					//修改库存
					subtractQty=(Integer.parseInt(qtysArr[i]))-oldDetail.getQty().intValue();
					
					if(!(warehouse.getWarehouseId().equals(oldDetail.getWarehouse().getWarehouseId()))){
						//修改仓库时，先把旧仓库的库存加上，再减去新仓库库存
						Store oldStore=storeDao.query(oldDetail.getCommodity(), oldDetail.getWarehouse());
						oldStore.setQtyStore(oldStore.getQtyStore()+oldDetail.getQty());
						subtractQty=Integer.parseInt(qtysArr[i]);
						
						commodity.setQtyStore(commodity.getQtyStore()+oldDetail.getQty());
					}else{
						if(store==null||(store.getQtyStore()-subtractQty)<0){
							throw new RuntimeException("商品"+store.getCommodity().getCommodityName()+"在仓库"+store.getWarehouse().getWarehouseName()+"数量不足");
						}
					}
				}else{
					oldDetail.setCommodity(commodity);
					oldDetail.setRecRej(model);
					subtractQty=Integer.parseInt(qtysArr[i]);
				}
				
				//修改详细单
				oldDetail.setQty(Integer.parseInt(qtysArr[i]));
				oldDetail.setPrice(Double.parseDouble(purchasePricesArr[i]));
				oldDetail.setWarehouse(warehouse);
				if((store.getQtyStore()-subtractQty)<0){
					throw new RuntimeException("商品"+store.getCommodity().getCommodityName()+"在仓库"+store.getWarehouse().getWarehouseName()+"数量不足");
				}
				store.setQtyStore(store.getQtyStore()-subtractQty);
				commodity.setQtyStore(commodity.getQtyStore()-subtractQty);
				if(oldDetail.getRecRejDetailId()==null){
					recRejDetailDao.save(oldDetail);
				}
			}
		}
		result.setIsSuccess(true);
		result.addData("recRejId", model.getRecRejId());
		return result;
	}
}
