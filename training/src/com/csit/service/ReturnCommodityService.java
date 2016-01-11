package com.csit.service;

import java.text.ParseException;

import com.csit.model.ReturnCommodity;
import com.csit.vo.ServiceResult;
/**
 * @Description:归还单Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface ReturnCommodityService extends BaseService<ReturnCommodity,Integer>{
	/**
	 * @param qtys 
	 * @param warehouseDetailIds 
	 * @param returnCommodityDetailIds 
	 * @param delReturnCommodityDetailIds 
	 * @param useCommodityDetailIds 
	 * @param model 
	 * @Description: 保存入库单
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @param qtys 
	 * @param warehoReturnDetailIds 
	 * @param ReturnCommodityDetailIds 
	 * @param delReturnCommodityDetailIds 
	 * @param commodityIds 
	 * @param totalPrices 
	 * @param purchasePrices 
	 * @param qtys 
	 * @param warehoReturnDetailIds 
	 * @param commodityIds 
	 * @param isNeedReturns 
	 * @param returnStatus 
	 * @param returnDates 
	 * @param isNeedReturns 
	 * @param receiveDetailIds 
	 * @param delReceiveDetailIds 
	 * @return
	 */
	ServiceResult save(ReturnCommodity model, String useCommodityDetailIds, String delReturnCommodityDetailIds, String returnCommodityDetailIds, String warehouseDetailIds, String qtys) throws ParseException;

	/**
	 * @Description: 分页查询归还单
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
	ServiceResult query(ReturnCommodity model, String beginDate, String endDate, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计归还单
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(ReturnCommodity model, String beginDate, String endDate);

	/**
	 * 
	 * @Description: 批量删除归还单
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param returnCommodityIds
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(String returnCommodityIds);

	/**
	 * 
	 * @Description: 查询归还单详细
	 * @param
	 * @Create: 2013-3-6 下午4:56:22
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult queryDetail(ReturnCommodity model);

}
