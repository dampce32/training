package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.CommodityDao;
import com.csit.dao.ScrappedDao;
import com.csit.dao.ScrappedDetailDao;
import com.csit.dao.StoreDao;
import com.csit.dao.SupplierDao;
import com.csit.dao.UserDAO;
import com.csit.dao.WarehouseDao;
import com.csit.model.Commodity;
import com.csit.model.Scrapped;
import com.csit.model.ScrappedDetail;
import com.csit.model.Store;
import com.csit.model.User;
import com.csit.model.Warehouse;
import com.csit.service.ScrappedService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;

@Service
public class ScrappedServiceImpl implements ScrappedService {
	
	@Resource
	private ScrappedDao scrappedDao; 
	@Resource
	private StoreDao storeDao; 
	@Resource
	private ScrappedDetailDao scrappedDetailDao; 
	@Resource
	private WarehouseDao warehouseDao; 
	@Resource
	private CommodityDao commodityDao; 
	@Resource
	private SupplierDao supplierDao; 
	@Resource
	private UserDAO userDao; 

	public ServiceResult save(Scrapped model, String scrappedDetailIds, String commodityIds, String warehouseDetailIds, String qtys,  String delScrappedIds) {
		ServiceResult result = new ServiceResult(false);
		
		if (model == null) {
			result.setMessage("请填写损溢单信息");
			return result;
		}
		if (model.getScrappedId()==null&&model.getScrappedType() == null) {
			result.setMessage("请选择损溢类型");
			return result;
		}
		if (model.getScrappedDate() == null) {
			result.setMessage("请填写日期");
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
		String[] scrappedDetailIdArr = StringUtil.split(scrappedDetailIds);
		String[] delScrappedIdArr = StringUtil.split(delScrappedIds);
		for(int i=0;i<commodityIdArr.length;i++){
			
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
		Scrapped oldModel = null;
		boolean isAdd=true;
		if(model.getScrappedId()==null){
			scrappedDao.save(model);
		}else{
			//修改
			oldModel =scrappedDao.load(model.getScrappedId());
			if (oldModel == null) {
				result.setMessage("该损溢单已不存在");
				return result;
			}
			oldModel.setScrappedDate(model.getScrappedDate());
			oldModel.setUser(model.getUser());
			oldModel.setQtyTotal(model.getQtyTotal());
			oldModel.setNote(model.getNote());
			
			if(delScrappedIdArr.length!=0){
				for(String detailId:delScrappedIdArr){
					if(StringUtils.isNotEmpty(detailId)){
						ScrappedDetail scrappedDetail=scrappedDetailDao.load(Integer.parseInt(detailId));
						Commodity commodity=scrappedDetail.getCommodity();
						Warehouse warehouse=scrappedDetail.getWarehouse();
						
						Store store = storeDao.query(commodity, warehouse);
						if(oldModel.getScrappedType().equals(0)){
							
							store.setQtyStore(store.getQtyStore()+scrappedDetail.getQty());
							commodity.setQtyStore(commodity.getQtyStore()+scrappedDetail.getQty());
						}else{
							if(store.getQtyStore()-scrappedDetail.getQty()<0){
								throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+warehouse.getWarehouseName()+"数量不足，无法进行修改");
							}
							store.setQtyStore(store.getQtyStore()-scrappedDetail.getQty());
							commodity.setQtyStore(commodity.getQtyStore()-scrappedDetail.getQty());
						}
						
						scrappedDetailDao.delete(scrappedDetail.getScrappedDetailId());
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
				ScrappedDetail scrappedDetail=new ScrappedDetail();
				scrappedDetail.setCommodity(commodity);
				scrappedDetail.setWarehouse(warehouse);
				scrappedDetail.setQty(Integer.parseInt(qtysArr[i]));
				scrappedDetail.setScrapped(model);
				scrappedDetailDao.save(scrappedDetail);
				if(store==null){
					//添加库存
					if(model.getScrappedType().equals(0)){
						throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+warehouse.getWarehouseName()+"数量不足，无法进行报废");
					}else{
						store=new Store();
						store.setCommodity(commodity);
						store.setWarehouse(warehouse);
						store.setQtyStore(Integer.parseInt(qtysArr[i]));
						storeDao.save(store);
					}
				}else{
					if(model.getScrappedType().equals(0)){
						if((store.getQtyStore()-Integer.parseInt(qtysArr[i]))<0){
							throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+warehouse.getWarehouseName()+"数量不足，无法进行报废");
						}
						store.setQtyStore(store.getQtyStore()-Integer.parseInt(qtysArr[i]));
					}else{
						store.setQtyStore(store.getQtyStore()+Integer.parseInt(qtysArr[i]));
					}
				}
				if(model.getScrappedType().equals(0)){
					
					commodity.setQtyStore(commodity.getQtyStore()-Integer.parseInt(qtysArr[i]));
				}else{
					commodity.setQtyStore(commodity.getQtyStore()+Integer.parseInt(qtysArr[i]));
				}
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
					ScrappedDetail oldDetail =new ScrappedDetail();
					Integer subtractQty=0;
					if(StringUtils.isNotEmpty(scrappedDetailIdArr[i])){
						oldDetail = scrappedDetailDao.load(Integer.parseInt(scrappedDetailIdArr[i]));
						//修改库存
						subtractQty=(Integer.parseInt(qtysArr[i]))-oldDetail.getQty().intValue();
						
						if(!(warehouse.getWarehouseId().equals(oldDetail.getWarehouse().getWarehouseId()))){
							//修改仓库时，若是报废单，则先把旧仓库数量加上，在减去新仓库数量，若是已出单，则先把旧仓库数量减去，再加上新仓库数量
							Store oldStore=storeDao.query(oldDetail.getCommodity(), oldDetail.getWarehouse());
							if(oldModel.getScrappedType().equals(0)){
								oldStore.setQtyStore(oldStore.getQtyStore()+oldDetail.getQty());
								commodity.setQtyStore(commodity.getQtyStore()+oldDetail.getQty());
							}else{
								oldStore.setQtyStore(oldStore.getQtyStore()-oldDetail.getQty());
								commodity.setQtyStore(commodity.getQtyStore()-oldDetail.getQty());
							}
							subtractQty=Integer.parseInt(qtysArr[i]);
						}else{
							if(oldModel.getScrappedType().equals(0)){
								if((store.getQtyStore()-subtractQty)<0){
									throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+warehouse.getWarehouseName()+"数量不足，无法进行操作");
								}
							}else{
								if((store.getQtyStore()+subtractQty)<0){
									throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+warehouse.getWarehouseName()+"数量不足，无法进行操作");
								}
							}
						}
					}else{
						oldDetail.setCommodity(commodity);
						oldDetail.setScrapped(model);
						subtractQty=Integer.parseInt(qtysArr[i]);
					}
					
					//修改详细单
					oldDetail.setQty(Integer.parseInt(qtysArr[i]));
					oldDetail.setWarehouse(warehouse);
					
					if(oldModel.getScrappedType().equals(0)){
						if(store.getQtyStore()-subtractQty<0){
							throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+warehouse.getWarehouseName()+"数量不足，无法进行操作");
						}
						store.setQtyStore(store.getQtyStore()-subtractQty);
						commodity.setQtyStore(commodity.getQtyStore()-subtractQty);
					}else{
						if(store.getQtyStore()+subtractQty<0){
							throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+warehouse.getWarehouseName()+"数量不足，无法进行操作");
						}
						store.setQtyStore(store.getQtyStore()+subtractQty);
						commodity.setQtyStore(commodity.getQtyStore()+subtractQty);
					}
					
					if(oldDetail.getScrappedDetailId()==null){
						scrappedDetailDao.save(oldDetail);
					}
				}
			}
		}
		result.setIsSuccess(true);
		result.addData("scrappedId", model.getScrappedId());
		return result;
	}

	public ServiceResult query(Scrapped model, String beginDate, String endDate, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		List<Scrapped> list = scrappedDao.query(model,beginDate,endDate, page, rows);
		String[] properties = { "scrappedId", "scrappedDate",
				"user.userName","user.userId", "qtyTotal","scrappedType"};
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(Scrapped model, String beginDate, String endDate) {
		ServiceResult result = new ServiceResult(false);
		Long data = scrappedDao.getTotalCount(model,beginDate,endDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(String scrappedIds) {
		ServiceResult result = new ServiceResult(false);
		if (StringUtils.isEmpty(scrappedIds)) {
			result.setMessage("请选择要删除的单子");
			return result;
		}
		String[] scrappedArr=StringUtil.split(scrappedIds);
		for(String scrappedId:scrappedArr){
			Scrapped oldModel = scrappedDao.load(Integer.parseInt(scrappedId));
			if (oldModel == null) {
				result.setMessage("该单子已不存在");
				return result;
			} else {
				List<ScrappedDetail> detailList=scrappedDetailDao.query("scrapped", oldModel);
				for(ScrappedDetail scrappedDetail:detailList){
					Store store=storeDao.query(scrappedDetail.getCommodity(), scrappedDetail.getWarehouse());
					Commodity commodity=commodityDao.load(scrappedDetail.getCommodity().getCommodityId());
					if(oldModel.getScrappedType().equals(1)){
						if(store.getQtyStore()-scrappedDetail.getQty()<0){
							throw new RuntimeException("商品"+scrappedDetail.getCommodity().getCommodityName()+"数量不足，无法进行操作");
						}
						store.setQtyStore(store.getQtyStore()-scrappedDetail.getQty());
						commodity.setQtyStore(commodity.getQtyStore()-scrappedDetail.getQty());
					}else{
						store.setQtyStore(store.getQtyStore()+scrappedDetail.getQty());
						commodity.setQtyStore(commodity.getQtyStore()+scrappedDetail.getQty());
					}
					scrappedDetailDao.delete(scrappedDetail);
				}
				scrappedDao.delete(oldModel);
			}
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult queryDetail(Scrapped model) {
		ServiceResult result = new ServiceResult(false);
		List<ScrappedDetail> list = scrappedDetailDao.query("scrapped",model);
		String[] properties = { "scrappedDetailId","commodity.commodityId","commodity.commodityName",  "qty", "warehouse.warehouseId", "commodityTypeName", "unitName"
				};
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

}
