package com.csit.dao;

import java.util.List;

import com.csit.model.Scrapped;
/**
 * @Description:损溢单DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface ScrappedDao extends BaseDAO<Scrapped,Integer>{

	
	/**
	 * 
	 * @Description: 分页查询损溢单
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
	List<Scrapped> query(Scrapped model, String beginDate, String endDate, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计损溢单
	 * @Create: 2013-2-28 下午1:43:43
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(Scrapped model, String beginDate, String endDate);
	
}
