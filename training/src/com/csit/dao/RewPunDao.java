package com.csit.dao;

import java.util.List;

import com.csit.model.RewPun;
/**
 * @Description:奖惩DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface RewPunDao extends BaseDAO<RewPun,Integer>{

	
	/**
	 * 
	 * @Description: 分页查询奖惩
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
	List<RewPun> query(RewPun model, String beginDate, String endDate, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 
	 * @Create: 2013-2-28 下午1:43:43
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param endDate 
	 * @param beginDate 
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(RewPun model, String beginDate, String endDate);

}
