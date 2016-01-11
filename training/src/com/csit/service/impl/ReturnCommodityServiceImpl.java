package com.csit.service.impl;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.CommodityDao;
import com.csit.dao.ReturnCommodityDao;
import com.csit.dao.ReturnCommodityDetailDao;
import com.csit.dao.StoreDao;
import com.csit.dao.UseCommodityDetailDao;
import com.csit.dao.UserDAO;
import com.csit.dao.WarehouseDao;
import com.csit.model.Commodity;
import com.csit.model.ReturnCommodity;
import com.csit.model.ReturnCommodityDetail;
import com.csit.model.Store;
import com.csit.model.UseCommodityDetail;
import com.csit.model.User;
import com.csit.model.Warehouse;
import com.csit.service.ReturnCommodityService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;

@Service
public class ReturnCommodityServiceImpl implements ReturnCommodityService {
	
	@Resource
	private ReturnCommodityDao returnCommodityDao;
	@Resource
	private ReturnCommodityDetailDao returnCommodityDetailDao;
	@Resource
	private StoreDao storeDao; 
	@Resource
	private WarehouseDao warehouseDao; 
	@Resource
	private UseCommodityDetailDao useCommodityDetailDao; 
	@Resource
	private CommodityDao commodityDao; 
	@Resource
	private UserDAO userDao; 
	
	public ServiceResult save(ReturnCommodity model, String useCommodityDetailIds, String delReturnCommodityDetailIds, String returnCommodityDetailIds, String warehouseDetailIds, String qtys) throws ParseException {
		ServiceResult result = new ServiceResult(false);

		if (model == null) {
			result.setMessage("请填写入库单信息");
			return result;
		}
		if (model.getReturnDate() == null) {
			result.setMessage("请填写归还日期");
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
			result.setMessage("请选择归还人");
			return result;
		}
		User Returnr=userDao.get(model.getUser().getUserId());
		if( Returnr==null){
			result.setMessage("请选择已有的归还人");
			return result;
		}
		
		String[] returnCommodityDetailIdArr = StringUtil.split(returnCommodityDetailIds);
		String[] warehouseDetailIdArr = StringUtil.split(warehouseDetailIds);
		String[] qtysArr = StringUtil.split(qtys);
		String[] useCommodityDetailIdArr = StringUtil.split(useCommodityDetailIds);
		String[] delReturnCommodityDetailIdArr = StringUtil.split(delReturnCommodityDetailIds);
		for(int i=0;i<useCommodityDetailIdArr.length;i++){
			
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
		if(model.getReturnCommodityId()==null){
			returnCommodityDao.save(model);
		}else{
			ReturnCommodity oldModel =returnCommodityDao.load(model.getReturnCommodityId());
			if (oldModel == null) {
				result.setMessage("该领用单已不存在");
				return result;
			}
			oldModel.setReturnDate(model.getReturnDate());
			oldModel.setUser(model.getUser());
			oldModel.setHandler(model.getHandler());
			oldModel.setQtyTotal(model.getQtyTotal());
			oldModel.setNote(model.getNote());
			
			if(delReturnCommodityDetailIdArr.length!=0){
				for(String detailId:delReturnCommodityDetailIdArr){
					if(StringUtils.isNotEmpty(detailId)){
						ReturnCommodityDetail returnCommodityDetail=returnCommodityDetailDao.load(Integer.parseInt(detailId));
						UseCommodityDetail useCommodityDetail=returnCommodityDetail.getUseCommodityDetail();
						Commodity commodity=returnCommodityDetail.getUseCommodityDetail().getCommodity();
						Warehouse warehouse=returnCommodityDetail.getWarehouse();
						Store store = storeDao.query(commodity, warehouse);
						store.setQtyStore(store.getQtyStore()-returnCommodityDetail.getQty());
						commodity.setQtyStore(commodity.getQtyStore()-returnCommodityDetail.getQty());
						returnCommodityDetailDao.delete(returnCommodityDetail.getReturnCommodityDetailId());
						if(useCommodityDetail.getUnReturnQty()+returnCommodityDetail.getQty()==useCommodityDetail.getQty()){
							useCommodityDetail.setReturnStatus(1);
						}else{
							useCommodityDetail.setReturnStatus(0);
						}
					}
				}
			}
			isAdd=false;
		}
		for(int i=0;i<useCommodityDetailIdArr.length;i++){
			
			String useCommodityDetailId=useCommodityDetailIdArr[i];
			UseCommodityDetail useCommodityDetail = useCommodityDetailDao.load(Integer.parseInt(useCommodityDetailId));
			Commodity commodity=commodityDao.load(useCommodityDetail.getCommodity().getCommodityId());
			Warehouse warehouse=warehouseDao.get(Integer.parseInt(warehouseDetailIdArr[i]));
			
			Store store = storeDao.query(commodity, warehouse);
			
			if(isAdd){
				//增加
				if(Integer.parseInt(qtysArr[i])>useCommodityDetail.getUnReturnQty()){
					throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+useCommodityDetail.getWarehouse().getWarehouseName()+"未还数量为"+useCommodityDetail.getUnReturnQty()+",归还数量不能高于未还数量");
				}
				
				ReturnCommodityDetail returnCommodityDetail=new ReturnCommodityDetail();
				returnCommodityDetail.setUseCommodityDetail(useCommodityDetail);
				returnCommodityDetail.setQty(Integer.parseInt(qtysArr[i]));
				returnCommodityDetail.setWarehouse(warehouse);
				returnCommodityDetail.setReturnCommodity(model);
				returnCommodityDetailDao.save(returnCommodityDetail);
				
				if(useCommodityDetail.getUnReturnQty().equals(Integer.parseInt(qtysArr[i]))){
					
					useCommodityDetail.setReturnStatus(0);
				}else if(useCommodityDetail.getQty().equals(0)){
					useCommodityDetail.setReturnStatus(1);
				}else{
					useCommodityDetail.setReturnStatus(2);
				}
				
				if(store==null){
					//添加库存
					store=new Store();
					store.setCommodity(useCommodityDetail.getCommodity());
					store.setWarehouse(warehouse);
					store.setQtyStore(Integer.parseInt(qtysArr[i]));
					storeDao.save(store);
				}else{
					store.setQtyStore(store.getQtyStore()+Integer.parseInt(qtysArr[i]));
				}
				commodity.setQtyStore(commodity.getQtyStore()+Integer.parseInt(qtysArr[i]));
			}else{
				//修改
				ReturnCommodityDetail oldDetail =new ReturnCommodityDetail();
				Integer subtractQty=0;
				if(StringUtils.isNotEmpty(returnCommodityDetailIdArr[i])){
					oldDetail = returnCommodityDetailDao.load(Integer.parseInt(returnCommodityDetailIdArr[i]));
					//修改库存
					subtractQty=(Integer.parseInt(qtysArr[i]))-oldDetail.getQty().intValue();
					
					if(subtractQty>useCommodityDetail.getUnReturnQty()){
						throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+useCommodityDetail.getWarehouse().getWarehouseName()+"未还数量为"+useCommodityDetail.getUnReturnQty()+",不能进行操作");
					}
					if(subtractQty==useCommodityDetail.getUnReturnQty()){
						useCommodityDetail.setReturnStatus(0);
					}else{
						useCommodityDetail.setReturnStatus(2);
					}
					
					if(!(warehouse.getWarehouseId().equals(oldDetail.getWarehouse().getWarehouseId()))){
						//修改仓库时，先把旧仓库的库存减去，再加上新仓库库存
						Store oldStore=storeDao.query(commodity, oldDetail.getWarehouse());
						if((oldStore.getQtyStore()-oldDetail.getQty())<0){
							throw new RuntimeException("商品"+oldStore.getCommodity().getCommodityName()+"数量不足，无法进行操作");
						}
						oldStore.setQtyStore(oldStore.getQtyStore()-oldDetail.getQty());
						subtractQty=Integer.parseInt(qtysArr[i]);
						commodity.setQtyStore(commodity.getQtyStore()-oldDetail.getQty());
					}else{
						if(store==null||(store.getQtyStore()+subtractQty)<0){
							throw new RuntimeException("商品"+store.getCommodity().getCommodityName()+"在仓库"+store.getWarehouse().getWarehouseName()+"数量不足");
						}
					}
				}else{
					oldDetail.setUseCommodityDetail(useCommodityDetail);
					oldDetail.setReturnCommodity(model);
					subtractQty=Integer.parseInt(qtysArr[i]);
					if(subtractQty>useCommodityDetail.getUnReturnQty()){
						throw new RuntimeException("商品"+commodity.getCommodityName()+"在仓库"+useCommodityDetail.getWarehouse().getWarehouseName()+"未还数量为"+useCommodityDetail.getUnReturnQty()+",不能进行操作");
					}
					if(subtractQty==useCommodityDetail.getUnReturnQty()){
						useCommodityDetail.setReturnStatus(0);
					}else{
						useCommodityDetail.setReturnStatus(2);
					}
				}
				
				//修改详细单
				oldDetail.setQty(Integer.parseInt(qtysArr[i]));
				oldDetail.setWarehouse(warehouse);
				
				store.setQtyStore(store.getQtyStore()+subtractQty);
				commodity.setQtyStore(commodity.getQtyStore()+subtractQty);
				if(oldDetail.getReturnCommodityDetailId()==null){
					returnCommodityDetailDao.save(oldDetail);
				}
			}
		}
		result.setIsSuccess(true);
		result.addData("returnCommodityId", model.getReturnCommodityId());
		return result;
	}

	public ServiceResult query(ReturnCommodity model, String beginDate, String endDate, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		List<ReturnCommodity> list = returnCommodityDao.query(model,beginDate,endDate, page, rows);
		String[] properties = { "returnCommodityId","user.userName", "user.userId","handleName","handleId", "returnDate", "qtyTotal","note" };
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(ReturnCommodity model, String beginDate, String endDate) {
		ServiceResult result = new ServiceResult(false);
		Long data = returnCommodityDao.getTotalCount(model,beginDate,endDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(String returnCommodityIds) {
		ServiceResult result = new ServiceResult(false);
		if (StringUtils.isEmpty(returnCommodityIds)) {
			result.setMessage("请选择要删除的单子");
			return result;
		}
		String[] returnCommodityArr=StringUtil.split(returnCommodityIds);
		for(String returnCommodityId:returnCommodityArr){
			ReturnCommodity oldModel = returnCommodityDao.load(Integer.parseInt(returnCommodityId));
			if (oldModel == null) {
				result.setMessage("该单子已不存在");
				return result;
			} else {
				List<ReturnCommodityDetail> detailList = returnCommodityDetailDao.query("returnCommodity",oldModel);
				for(ReturnCommodityDetail returnCommodityDetail:detailList){
					UseCommodityDetail useCommodityDetail=returnCommodityDetail.getUseCommodityDetail();
					Store store=storeDao.query(useCommodityDetail.getCommodity(),returnCommodityDetail.getWarehouse());
					Commodity commodity=useCommodityDetail.getCommodity();
					if(store.getQtyStore()+useCommodityDetail.getUnReturnQty()==useCommodityDetail.getQty()){
						useCommodityDetail.setReturnStatus(1);
					}else{
						useCommodityDetail.setReturnStatus(2);
					}
					store.setQtyStore(store.getQtyStore()-returnCommodityDetail.getQty());
					commodity.setQtyStore(commodity.getQtyStore()-returnCommodityDetail.getQty());
					returnCommodityDetailDao.delete(returnCommodityDetail);
				}
				returnCommodityDao.delete(oldModel);
			}
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult queryDetail(ReturnCommodity model) {
		ServiceResult result = new ServiceResult(false);
		List<ReturnCommodityDetail> list = returnCommodityDetailDao.query("returnCommodity",model);
		String[] properties = { "returnCommodityDetailId","useCommodityDetail.useCommodityDetailId","commodityName","qty", "warehouse.warehouseId", "commodityTypeName", "unitName"};
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

}
