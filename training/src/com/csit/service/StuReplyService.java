package com.csit.service;

import com.csit.model.StuReply;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description:学员关怀Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-6
 * @author jcf
 * @vesion 1.0
 */
public interface StuReplyService extends BaseService<StuReply, Integer> {

	/**
	 * 
	 * @Description: 保存学员关怀
	 * @Create: 2013-3-6 上午10:43:01
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(StuReply model);
	/**
	 * 
	 * @Description: 删除学员关怀
	 * @Create: 2013-3-6 上午10:43:06
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(StuReply model);
	/**
	 * 
	 * @Description: 分页查询学员关怀
	 * @Create: 2013-3-6 上午10:43:11
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(StuReply model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计学员关怀
	 * @Create: 2013-3-6 上午10:43:18
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(StuReply model);
}
