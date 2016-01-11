package com.csit.service.impl;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.CommodityDao;
import com.csit.dao.StoreDao;
import com.csit.dao.StoreTuneOutDao;
import com.csit.dao.UserDAO;
import com.csit.dao.WarehouseDao;
import com.csit.model.Store;
import com.csit.model.StoreTuneOut;
import com.csit.model.User;
import com.csit.model.Warehouse;
import com.csit.service.StoreTuneOutService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;

@Service
public class StoreTuneOutServiceImpl implements StoreTuneOutService {
	
	@Resource
	private StoreTuneOutDao storeTuneOutDao;
	@Resource
	private UserDAO userDao;
	@Resource
	private StoreDao storeDao; 
	@Resource
	private WarehouseDao warehouseDao; 
	@Resource
	private CommodityDao commodityDao; 
	
	public ServiceResult save(StoreTuneOut model)  {
		ServiceResult result = new ServiceResult(false);
		if(model.getCommodity()==null){
			result.setMessage("请选择所要调拨的商品");
			return result;
		}
		if(model.getTuneInWarehouse()==null){
			result.setMessage("请选择所要调入的仓库");
			return result;
		}
		Warehouse tuneInWarehouse=warehouseDao.load(model.getTuneInWarehouse().getWarehouseId());
		if(tuneInWarehouse==null){
			result.setMessage("请选择已有的所要调入的仓库");
			return result;
		}
		if(model.getTuneOutWarehouse()==null){
			result.setMessage("请选择所要调出的仓库");
			return result;
		}
		Warehouse tuneOutWarehouse=warehouseDao.load(model.getTuneOutWarehouse().getWarehouseId());
		if(tuneOutWarehouse==null){
			result.setMessage("请选择已有的所要调出的仓库");
			return result;
		}
		if(tuneInWarehouse.getWarehouseId().equals(tuneOutWarehouse.getWarehouseId())){
			result.setMessage("调入仓库不能和调出仓库一样");
			return result;
		}
		if(model.getTuneOutDate()==null){
			result.setMessage("请填写调拨日期");
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
		Store oldStore;
		Store newStore;
		if(model.getStoreTuneOutId()==null){
			//添加
			storeTuneOutDao.save(model);
			oldStore=storeDao.query(model.getCommodity(), tuneOutWarehouse);
			newStore=storeDao.query(model.getCommodity(), tuneInWarehouse);
			if(oldStore.getQtyStore()-model.getQty()<0){
				throw new RuntimeException("商品"+oldStore.getCommodity().getCommodityName()+"在仓库"+oldStore.getWarehouse().getWarehouseName()+"数量不足,无法进行调拨");
			}
			oldStore.setQtyStore(oldStore.getQtyStore()-model.getQty());
			
			if(newStore==null){
				newStore=new Store();
				newStore.setCommodity(model.getCommodity());
				newStore.setQtyStore(model.getQty());
				newStore.setWarehouse(tuneInWarehouse);
				storeDao.save(newStore);
			}else{
				newStore.setQtyStore(newStore.getQtyStore()+model.getQty());
			}
		}else{
			//修改
			StoreTuneOut oldTuneOut=storeTuneOutDao.load(model.getStoreTuneOutId());
			
			if(model.getTuneInWarehouse().getWarehouseId().equals(oldTuneOut.getTuneInWarehouse().getWarehouseId())){
				Store tuneInStore=storeDao.query(oldTuneOut.getCommodity(),oldTuneOut.getTuneInWarehouse());
				Store tuneOutStore=storeDao.query(oldTuneOut.getCommodity(),oldTuneOut.getTuneOutWarehouse());
				if((tuneInStore.getQtyStore()+(model.getQty()-oldTuneOut.getQty()))<0){
					throw new RuntimeException("商品"+tuneInStore.getCommodity().getCommodityName()+"在仓库"+tuneInStore.getWarehouse().getWarehouseName()+"数量不足");
				}
				if((tuneOutStore.getQtyStore()-(model.getQty()-oldTuneOut.getQty()))<0){
					throw new RuntimeException("商品"+tuneInStore.getCommodity().getCommodityName()+"在仓库"+tuneOutStore.getWarehouse().getWarehouseName()+"数量不足");
				}
				tuneInStore.setQtyStore(tuneInStore.getQtyStore()+(model.getQty()-oldTuneOut.getQty()));
				tuneOutStore.setQtyStore(tuneOutStore.getQtyStore()-(model.getQty()-oldTuneOut.getQty()));
			}else{
				//修改所要调入的仓库
				oldStore=storeDao.query(oldTuneOut.getCommodity(),oldTuneOut.getTuneInWarehouse());
				newStore=storeDao.query(oldTuneOut.getCommodity(), model.getTuneInWarehouse());
				
				oldStore.setQtyStore(oldStore.getQtyStore()-oldTuneOut.getQty());
				
				if(newStore==null){
					newStore=new Store();
					newStore.setCommodity(model.getCommodity());
					newStore.setQtyStore(model.getQty());
					newStore.setWarehouse(tuneInWarehouse);
					storeDao.save(newStore);
				}else{
					if((newStore.getQtyStore()+(model.getQty()-oldTuneOut.getQty()))<0){
						throw new RuntimeException("商品"+newStore.getCommodity().getCommodityName()+"在仓库"+newStore.getWarehouse().getWarehouseName()+"数量不足,无法进行调拨");
					}
					newStore.setQtyStore(newStore.getQtyStore()+(model.getQty()-oldTuneOut.getQty()));
				}
				oldTuneOut.setTuneInWarehouse(model.getTuneInWarehouse());
			}
			oldTuneOut.setQty(model.getQty());
			oldTuneOut.setNote(model.getNote());
			oldTuneOut.setTuneOutDate(model.getTuneOutDate());
			oldTuneOut.setUser(model.getUser());
		}
		result.setIsSuccess(true);
		result.addData("StoreTuneOutId", model.getStoreTuneOutId());
		return result;
	}

	public ServiceResult query(StoreTuneOut model, String beginDate, String endDate, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		List<StoreTuneOut> list = storeTuneOutDao.query(model,beginDate,endDate, page, rows);
		String[] properties = {"storeTuneOutId", "commodity.commodityName", "commodity.commodityId","tuneOutWarehouseName","tuneOutWarehouseId","tuneInWarehouseName","tuneInWarehouseId","user.userName","user.userId","tuneOutDate","qty","note" };
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(StoreTuneOut model, String beginDate, String endDate) {
		ServiceResult result = new ServiceResult(false);
		Long data = storeTuneOutDao.getTotalCount(model,beginDate,endDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(String storeTuneOutIds) {
		ServiceResult result = new ServiceResult(false);
		if (StringUtils.isEmpty(storeTuneOutIds)) {
			result.setMessage("请选择要删除的单子");
			return result;
		}
		String[] storeTuneOutIdArr=StringUtil.split(storeTuneOutIds);
		for(String storeTuneOutId:storeTuneOutIdArr){
			StoreTuneOut oldModel = storeTuneOutDao.load(Integer.parseInt(storeTuneOutId));
			if (oldModel == null) {
				result.setMessage("该单子已不存在");
				return result;
			} else {
				Store tuneOutStore=storeDao.query(oldModel.getCommodity(), oldModel.getTuneOutWarehouse());
				Store tuneInStore=storeDao.query(oldModel.getCommodity(), oldModel.getTuneInWarehouse());
				
				tuneOutStore.setQtyStore(tuneOutStore.getQtyStore()+oldModel.getQty());
				if(tuneInStore.getQtyStore()-oldModel.getQty()<0){
					throw new RuntimeException("商品"+tuneInStore.getCommodity().getCommodityName()+"在仓库"+tuneInStore.getWarehouse().getWarehouseName()+"数量不足,无法进行删除");
				}
				tuneInStore.setQtyStore(tuneInStore.getQtyStore()-oldModel.getQty());
				
				storeTuneOutDao.delete(oldModel.getStoreTuneOutId());
			}
		}
		result.setIsSuccess(true);
		return result;
	}

}
