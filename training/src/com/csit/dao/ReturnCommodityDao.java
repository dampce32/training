package com.csit.dao;

import java.util.List;

import com.csit.model.ReturnCommodity;
/**
 * @Description:领用单DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface ReturnCommodityDao extends BaseDAO<ReturnCommodity,Integer>{

	/**
	 * 
	 * @Description: 分页查询归还单
	 * @param
	 * @Create: 2013-3-26 上午9:32:47
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param beginDate
	 * @param endDate
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<ReturnCommodity> query(ReturnCommodity model, String beginDate,
			String endDate, Integer page, Integer rows);
	
	/**
	 * 
	 * @Description: 统计归还单
	 * @param
	 * @Create: 2013-3-26 上午11:22:47
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(ReturnCommodity model, String beginDate, String endDate);



	


	
	

}
