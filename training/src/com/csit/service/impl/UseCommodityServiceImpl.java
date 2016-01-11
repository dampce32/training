package com.csit.service.impl;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.CommodityDao;
import com.csit.dao.StoreDao;
import com.csit.dao.UseCommodityDao;
import com.csit.dao.UseCommodityDetailDao;
import com.csit.dao.UserDAO;
import com.csit.dao.WarehouseDao;
import com.csit.model.Commodity;
import com.csit.model.Store;
import com.csit.model.UseCommodity;
import com.csit.model.UseCommodityDetail;
import com.csit.model.User;
import com.csit.model.Warehouse;
import com.csit.service.UseCommodityService;
import com.csit.util.DateUtil;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;

@Service
public class UseCommodityServiceImpl implements UseCommodityService {
	
	@Resource
	private UseCommodityDao useCommodityDao;
	@Resource
	private UseCommodityDetailDao useCommodityDetailDao;
	@Resource
	private StoreDao storeDao; 
	@Resource
	private WarehouseDao warehouseDao; 
	@Resource
	private CommodityDao commodityDao; 
	@Resource
	private UserDAO userDao; 
	
	public ServiceResult save(UseCommodity model, String commodityIds, String delUseCommodityDetailIds, String useCommodityDetailIds, String warehouseDetailIds, String qtys, String isNeedReturns, String returnDates, String returnStatus) throws ParseException {
		ServiceResult result = new ServiceResult(false);

		if (model == null) {
			result.setMessage("请填写入库单信息");
			return result;
		}
		if (model.getUseDate() == null) {
			result.setMessage("请填写领用日期");
			return result;
		}
		if (model.getHandler()== null) {
			result.setMessage("请选择经办人");
			return result;
		}
		User handle=userDao.get(model.getHandler().getUserId());
		if(handle==null){
			result.setMessage("请选择已有的经办人");
			return result;
		}
		if(model.getUser()==null){
			result.setMessage("请选择领用人");
			return result;
		}
		User user=userDao.get(model.getUser().getUserId());
		if( user==null){
			result.setMessage("请选择已有的领用人");
			return result;
		}
		
		String[] isNeedReturnArr = StringUtil.split(isNeedReturns);
		String[] returnDateArr = StringUtil.split(returnDates);
		String[] returnStatusArr = StringUtil.split(returnStatus);
		String[] commodityIdArr = StringUtil.split(commodityIds);
		String[] warehouseDetailIdArr = StringUtil.split(warehouseDetailIds);
		String[] qtysArr = StringUtil.split(qtys);
		String[] useCommodityDetailIdArr = StringUtil.split(useCommodityDetailIds);
		String[] delUseCommodityDetailIdArr = StringUtil.split(delUseCommodityDetailIds);
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
			
			if("1".equals(isNeedReturnArr[i])){
				if(StringUtils.isEmpty(returnDateArr[i])){
					result.setMessage("第"+(i+1)+"行数据没有选择填写归还日期");
					return result;
				}
			}
		}
		
		boolean isAdd=true;
		if(model.getUseCommodityId()==null){
			useCommodityDao.save(model);
		}else{
			UseCommodity oldModel =useCommodityDao.load(model.getUseCommodityId());
			if (oldModel == null) {
				result.setMessage("该领用单已不存在");
				return result;
			}
			oldModel.setUseDate(model.getUseDate());
			oldModel.setUser(model.getUser());
			oldModel.setHandler(model.getHandler());
			oldModel.setQtyTotal(model.getQtyTotal());
			oldModel.setNote(model.getNote());
			
			if(delUseCommodityDetailIdArr.length!=0){
				for(String detailId:delUseCommodityDetailIdArr){
					if(StringUtils.isNotEmpty(detailId)){
						UseCommodityDetail useCommodityDetail=useCommodityDetailDao.load(Integer.parseInt(detailId));
						Commodity commodity=useCommodityDetail.getCommodity();
						Warehouse warehouse=useCommodityDetail.getWarehouse();
						Store store = storeDao.query(commodity, warehouse);
						store.setQtyStore(store.getQtyStore()+useCommodityDetail.getQty());
						commodity.setQtyStore(commodity.getQtyStore()+useCommodityDetail.getQty());
						useCommodityDetailDao.delete(useCommodityDetail.getUseCommodityDetailId());
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
				UseCommodityDetail useCommodityDetail=new UseCommodityDetail();
				useCommodityDetail.setCommodity(commodity);
				useCommodityDetail.setQty(Integer.parseInt(qtysArr[i]));
				useCommodityDetail.setIsNeedReturn(Integer.parseInt(isNeedReturnArr[i]));
				useCommodityDetail.setWarehouse(warehouse);
				if(StringUtils.isEmpty(returnDateArr[i])){
					useCommodityDetail.setReturnDate(null);
				}else{
					useCommodityDetail.setReturnDate(DateUtil.toDate(returnDateArr[i]));
				}
				useCommodityDetail.setUseCommoidty(model);
				useCommodityDetail.setReturnStatus(Integer.parseInt(returnStatusArr[i]));
				useCommodityDetailDao.save(useCommodityDetail);
				if(store.getQtyStore()-Integer.parseInt(qtysArr[i])<0){
					throw new RuntimeException("商品"+store.getCommodity().getCommodityName()+"在仓库"+store.getWarehouse().getWarehouseName()+"数量不足");
				}
				store.setQtyStore(store.getQtyStore()-Integer.parseInt(qtysArr[i]));
				commodity.setQtyStore(commodity.getQtyStore()-Integer.parseInt(qtysArr[i]));
			}else{
				//修改
				UseCommodityDetail oldDetail =new UseCommodityDetail();
				Integer subtractQty=0;
				if(StringUtils.isNotEmpty(useCommodityDetailIdArr[i])){
					oldDetail = useCommodityDetailDao.load(Integer.parseInt(useCommodityDetailIdArr[i]));
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
					oldDetail.setUseCommoidty(model);
					subtractQty=Integer.parseInt(qtysArr[i]);
				}
				
				//修改详细单
				oldDetail.setQty(Integer.parseInt(qtysArr[i]));
				oldDetail.setWarehouse(warehouse);
				oldDetail.setIsNeedReturn(Integer.parseInt(isNeedReturnArr[i]));
				if(StringUtils.isEmpty(returnDateArr[i])){
					oldDetail.setReturnDate(null);
				}else{
					oldDetail.setReturnDate(DateUtil.toDate(returnDateArr[i]));
				}
				oldDetail.setReturnStatus(Integer.parseInt(returnStatusArr[i]));
				if(store==null||(store.getQtyStore()-subtractQty)<0){
					throw new RuntimeException("商品"+store.getCommodity().getCommodityName()+"在仓库"+store.getWarehouse().getWarehouseName()+"数量不足");
				}
				store.setQtyStore(store.getQtyStore()-subtractQty);
				commodity.setQtyStore(commodity.getQtyStore()-subtractQty);
				if(oldDetail.getUseCommodityDetailId()==null){
					useCommodityDetailDao.save(oldDetail);
				}
			}
		}
		result.setIsSuccess(true);
		result.addData("useCommodityId", model.getUseCommodityId());
		return result;
	}

	public ServiceResult query(UseCommodity model, String beginDate, String endDate, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		List<UseCommodity> list = useCommodityDao.query(model,beginDate,endDate, page, rows);
		String[] properties = { "useCommodityId","user.userName", "user.userId","handleName","handleId", "useDate", "qtyTotal","note" };
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(UseCommodity model, String beginDate, String endDate) {
		ServiceResult result = new ServiceResult(false);
		Long data = useCommodityDao.getTotalCount(model,beginDate,endDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(String useCommodityIds) {
		ServiceResult result = new ServiceResult(false);
		if (StringUtils.isEmpty(useCommodityIds)) {
			result.setMessage("请选择要删除的单子");
			return result;
		}
		String[] useCommodityArr=StringUtil.split(useCommodityIds);
		for(String useCommodityId:useCommodityArr){
			UseCommodity oldModel = useCommodityDao.load(Integer.parseInt(useCommodityId));
			if (oldModel == null) {
				result.setMessage("该单子已不存在");
				return result;
			} else {
				List<UseCommodityDetail> detailList = useCommodityDetailDao.query("useCommoidty",oldModel);
				for(UseCommodityDetail useCommodityDetail:detailList){
					Store store=storeDao.query(useCommodityDetail.getCommodity(), useCommodityDetail.getWarehouse());
					Commodity commodity=commodityDao.load(useCommodityDetail.getCommodity().getCommodityId());
					
					store.setQtyStore(store.getQtyStore()+useCommodityDetail.getQty());
					commodity.setQtyStore(commodity.getQtyStore()+useCommodityDetail.getQty());
					
					useCommodityDetailDao.delete(useCommodityDetail);
				}
				useCommodityDao.delete(oldModel);
			}
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult queryDetail(UseCommodity model) {
		ServiceResult result = new ServiceResult(false);
		List<UseCommodityDetail> list = useCommodityDetailDao.query("useCommoidty",model);
		String[] properties = { "useCommodityDetailId","commodity.commodityId","commodity.commodityName","qty", "warehouse.warehouseId", "commodityTypeName", "unitName",
				"isNeedReturn","returnDate","returnStatus"
				};
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult queryNeedReturn(UseCommodity model,
			String commodityName, String userName, Integer rows, Integer page,String useCommodityDetailIds) {
		ServiceResult result = new ServiceResult(false);
		
		Integer[] useCommodityDetailIdArr;
		if(StringUtils.isNotEmpty(useCommodityDetailIds)){
			String[] s = StringUtil.split(useCommodityDetailIds);
			useCommodityDetailIdArr=new Integer[s.length];
			for(int i=0;i<s.length;i++){
				useCommodityDetailIdArr[i]=Integer.parseInt(s[i]);
			}
		}else{
			useCommodityDetailIdArr=null;
		}
		
		List<UseCommodityDetail> list = useCommodityDetailDao.queryNeedReturn(model,commodityName,userName,rows,page,useCommodityDetailIdArr);
		String[] properties = { "useCommodityDetailId","commodity.commodityId","commodity.commodityName","qty", "warehouse.warehouseId", "warehouse.warehouseName", "commodityTypeName", "unitName",
				"returnDate","returnStatus","userName","returnQty","unReturnQty"
				};
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCountNeedReturn(UseCommodity model,
			String commodityName, String userName,String useCommodityDetailIds) {
		ServiceResult result = new ServiceResult(false);
		Integer[] useCommodityDetailIdArr;
		if(StringUtils.isNotEmpty(useCommodityDetailIds)){
			String[] s = StringUtil.split(useCommodityDetailIds);
			useCommodityDetailIdArr=new Integer[s.length];
			for(int i=0;i<s.length;i++){
				useCommodityDetailIdArr[i]=Integer.parseInt(s[i]);
			}
		}else{
			useCommodityDetailIdArr=null;
		}
		
		Long data = useCommodityDetailDao.getTotalCountNeedReturn(model,commodityName,userName,useCommodityDetailIdArr);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}


}
