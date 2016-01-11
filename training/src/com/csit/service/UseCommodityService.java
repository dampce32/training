package com.csit.service;

import java.text.ParseException;

import com.csit.model.UseCommodity;
import com.csit.vo.ServiceResult;
/**
 * @Description:领用单Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface UseCommodityService extends BaseService<UseCommodity,Integer>{
	/**
	 * @Description: 保存入库单
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @param qtys 
	 * @param warehouseDetailIds 
	 * @param useCommodityDetailIds 
	 * @param deluseCommodityDetailIds 
	 * @param commodityIds 
	 * @param totalPrices 
	 * @param purchasePrices 
	 * @param qtys 
	 * @param warehouseDetailIds 
	 * @param commodityIds 
	 * @param isNeedReturns 
	 * @param returnStatus 
	 * @param returnDates 
	 * @param isNeedReturns 
	 * @param receiveDetailIds 
	 * @param delReceiveDetailIds 
	 * @return
	 */
	ServiceResult save(UseCommodity model, String commodityIds, String deluseCommodityDetailIds, String useCommodityDetailIds, String warehouseDetailIds, String qtys, String isNeedReturns, String returnDates, String returnStatus) throws ParseException;

	/**
	 * @Description: 分页查询领用单
	 * @Create: 2013-2-28 上午11:28:25
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param endDate 
	 * @param beginDate 
	 * @param rows 
	 * @param page 
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(UseCommodity model, String beginDate, String endDate, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计领用单
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(UseCommodity model, String beginDate, String endDate);

	/**
	 * 
	 * @Description: 批量删除领用单
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param useCommodityIds
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(String useCommodityIds);

	/**
	 * 
	 * @Description: 查询领用单详细
	 * @param
	 * @Create: 2013-3-6 下午4:56:22
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult queryDetail(UseCommodity model);
	/**
	 * 
	 * @Description: 查询需要归还的领用单详细
	 * @param
	 * @Create: 2013-3-25 下午2:57:10
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param commodityName
	 * @param userName
	 * @param page 
	 * @param rows 
	 * @param useCommodityDetailIds 
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult queryNeedReturn(UseCommodity model, String commodityName,
			String userName, Integer rows, Integer page, String useCommodityDetailIds);
	/**
	 * 
	 * @Description: 统计需要归还的领用单详细
	 * @param
	 * @Create: 2013-3-25 下午2:57:10
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param commodityName
	 * @param userName
	 * @param useCommodityDetailIds 
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCountNeedReturn(UseCommodity model,
			String commodityName, String userName,String useCommodityDetailIds);

}
