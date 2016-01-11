package com.csit.dao;

import java.util.List;

import com.csit.model.Potential;
/**
 * 
 * @Description:咨询信息Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author jcf
 * @vesion 1.0
 */
public interface PotentialDAO extends BaseDAO<Potential,Integer>{

	/**
	 * 
	 * @Description: 分页查询咨询信息
	 * @Create: 2013-3-1 上午11:28:57
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Potential> query(Potential model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计咨询信息
	 * @Create: 2013-3-1 上午11:29:09
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Potential model);
}
