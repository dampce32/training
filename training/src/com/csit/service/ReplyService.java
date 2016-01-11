package com.csit.service;

import com.csit.model.Reply;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description:回访Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @author jcf
 * @vesion 1.0
 */
public interface ReplyService extends BaseService<Reply, Integer> {

	/**
	 * 
	 * @Description: 保存回访信息
	 * @Create: 2013-3-4 上午11:19:10
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(Reply model);
	/**
	 * 
	 * @Description: 删除回访信息
	 * @Create: 2013-3-4 上午11:19:48
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(Reply model);
	/**
	 * 
	 * @Description: 分页查询回访信息
	 * @Create: 2013-3-4 上午11:20:19
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Reply model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计保存回访信息
	 * @Create: 2013-3-4 上午11:19:26
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Reply model);
}
