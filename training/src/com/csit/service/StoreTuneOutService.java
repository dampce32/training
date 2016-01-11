package com.csit.service;

import java.text.ParseException;

import com.csit.model.StoreTuneOut;
import com.csit.vo.ServiceResult;
/**
 * @Description:调拨单Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface StoreTuneOutService extends BaseService<StoreTuneOut,Integer>{
	/**
	 * @Description: 保存调拨单
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @param delReceiveDetailIds 
	 * @return
	 */
	ServiceResult save(StoreTuneOut model) throws ParseException;

	/**
	 * @Description: 分页查询调拨单
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
	ServiceResult query(StoreTuneOut model, String beginDate, String endDate, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计调拨单
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(StoreTuneOut model, String beginDate, String endDate);

	/**
	 * 
	 * @Description: 批量删除调拨单
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param storeTuneOutIds
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(String storeTuneOutIds);

}
