package com.csit.dao;

import java.util.List;

import com.csit.model.PotentialStuStatus;
/**
 * 
 * @Description:潜在生源状态Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
public interface PotentialStuStatusDAO extends BaseDAO<PotentialStuStatus,Integer>{

	/**
	 * 
	 * @Description: 分页查询潜在生源状态
	 * @Create: 2013-2-28 上午09:32:21
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<PotentialStuStatus> query(PotentialStuStatus model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计潜在生源状态
	 * @Create: 2013-2-28 上午09:32:50
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(PotentialStuStatus model);
}
