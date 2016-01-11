package com.csit.dao;

import java.util.List;

import com.csit.model.Media;
/**
 * 
 * @Description:媒体Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
public interface MediaDAO extends BaseDAO<Media,Integer>{

	/**
	 * 
	 * @Description: 分页查询媒体
	 * @Create: 2013-2-28 上午09:32:21
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Media> query(Media model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计媒体
	 * @Create: 2013-2-28 上午09:32:50
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Media model);
}
