package com.csit.dao;

import java.util.List;

import com.csit.model.StuReply;
/**
 * 
 * @Description:学员关怀Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-6
 * @author jcf
 * @vesion 1.0
 */
public interface StuReplyDAO extends BaseDAO<StuReply,Integer>{

	/**
	 * 
	 * @Description: 分页查询学员关怀记录
	 * @Create: 2013-3-6 上午10:30:40
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<StuReply> query(StuReply model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计学员关怀记录
	 * @Create: 2013-3-6 上午10:30:46
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(StuReply model);
}
