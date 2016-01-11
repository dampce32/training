package com.csit.dao;

import java.util.List;

import com.csit.model.Reply;
/**
 * 
 * @Description:回访Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @author jcf
 * @vesion 1.0
 */
public interface ReplyDAO extends BaseDAO<Reply,Integer>{

	/**
	 * 
	 * @Description: 分页查询回访信息
	 * @Create: 2013-3-4 上午11:11:33
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Reply> query(Reply model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计回访信息
	 * @Create: 2013-3-4 上午11:11:47
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Reply model);
	/**
	 * 
	 * @Description: 得到当前最大Id
	 * @Create: 2013-3-5 下午03:44:08
	 * @author jcf
	 * @update logs
	 * @param potentialId
	 * @return
	 */
	Integer MaxId(Integer potentialId);
}
