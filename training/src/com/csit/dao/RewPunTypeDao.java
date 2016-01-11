package com.csit.dao;

import java.util.List;

import com.csit.model.RewPunType;
/**
 * @Description:奖惩分类DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface RewPunTypeDao extends BaseDAO<RewPunType,Integer>{

	
	/**
	 * 
	 * @Description: 分页查询奖惩分类
	 * @Create: 2013-2-28 下午1:40:05
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @throws Exception
	 */
	List<RewPunType> query(RewPunType model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 
	 * @Create: 2013-2-28 下午1:43:43
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(RewPunType model);

}
