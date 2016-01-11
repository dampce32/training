package com.csit.dao;

import java.util.List;

import com.csit.model.RecRej;
/**
 * @Description:入库出库单DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface RecRejDao extends BaseDAO<RecRej,Integer>{

	
	/**
	 * 
	 * @Description: 分页查询入库出库单
	 * @Create: 2013-2-28 下午1:40:05
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param endDate 
	 * @param beginDate 
	 * @param page
	 * @param rows
	 * @throws Exception
	 */
	List<RecRej> query(RecRej model, String beginDate, String endDate, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计入库出库单
	 * @Create: 2013-2-28 下午1:43:43
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(RecRej model, String beginDate, String endDate);
	
	/**
	 * 
	 * @Description: 初始化入库出库单号
	 * @param
	 * @Create: 2013-3-6 下午4:03:59
	 * @author cjp
	 * @update logs
	 * @param date
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long initCode(String date);

}
