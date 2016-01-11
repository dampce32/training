package com.csit.service;

import com.csit.model.Scrapped;
import com.csit.vo.ServiceResult;
/**
 * @Description:损溢单Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface ScrappedService extends BaseService<Scrapped,Integer>{
	/**
	 * @Description: 保存损溢单
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @param qtys 
	 * @param warehouseDetailIds 
	 * @param commodityIds 
	 * @param scrappedDetailIds 
	 * @param delScrappedIds 
	 * @return
	 */
	ServiceResult save(Scrapped model, String scrappedDetailIds, String commodityIds, String warehouseDetailIds, String qtys, String delScrappedIds);

	/**
	 * @Description: 分页查询损溢单
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
	ServiceResult query(Scrapped model, String beginDate, String endDate, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计损溢单
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Scrapped model, String beginDate, String endDate);

	/**
	 * 
	 * @Description: 批量删除损溢单
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param scrappedIds
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(String scrappedIds);

	/**
	 * 
	 * @Description: 查询损溢单详细
	 * @param
	 * @Create: 2013-3-6 下午4:56:22
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult queryDetail(Scrapped model);

}
