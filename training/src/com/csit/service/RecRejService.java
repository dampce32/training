package com.csit.service;

import com.csit.model.RecRej;
import com.csit.vo.ServiceResult;
/**
 * @Description:入库出库单Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface RecRejService extends BaseService<RecRej,Integer>{
	/**
	 * @Description: 保存入库单
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @param totalPrices 
	 * @param purchasePrices 
	 * @param qtys 
	 * @param warehouseDetailIds 
	 * @param commodityIds 
	 * @param receiveDetailIds 
	 * @param delReceiveDetailIds 
	 * @return
	 */
	ServiceResult saveReceive(RecRej model, String receiveDetailIds, String commodityIds, String warehouseDetailIds, String qtys, String purchasePrices, String totalPrices, String delReceiveDetailIds);

	/**
	 * @Description: 分页查询入库出库单
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
	ServiceResult query(RecRej model, String beginDate, String endDate, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计入库出库单
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(RecRej model, String beginDate, String endDate);

	/**
	 * 
	 * @Description: 批量删除入库出库单
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param recrRejIds
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(String recrRejIds);

	/**
	 * 
	 * @Description: 初始化入库单编号
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	ServiceResult initCode();

	/**
	 * 
	 * @Description: 查询入库出库单详细
	 * @param
	 * @Create: 2013-3-6 下午4:56:22
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult queryDetail(RecRej model);

	/**
	 * @Description: 保存出库单
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @param totalPrices 
	 * @param purchasePrices 
	 * @param qtys 
	 * @param warehouseDetailIds 
	 * @param commodityIds 
	 * @param receiveDetailIds 
	 * @param delReceiveDetailIds 
	 * @return
	 */
	ServiceResult saveReject(RecRej model, String receiveDetailIds,
			String commodityIds, String warehouseDetailIds, String qtys,
			String purchasePrices, String totalPrices,
			String delReceiveDetailIds);
}
