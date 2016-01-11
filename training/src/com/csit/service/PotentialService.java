package com.csit.service;

import com.csit.model.Potential;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description:咨询信息Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author jcf
 * @vesion 1.0
 */
public interface PotentialService extends BaseService<Potential, Integer> {

	/**
	 * 
	 * @Description: 保存咨询信息表
	 * @Create: 2013-3-1 下午01:57:19
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(Potential model);
	/**
	 * 
	 * @Description: 删除咨询信息表
	 * @Create: 2013-3-1 下午01:57:44
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(Potential model);
	/**
	 * 
	 * @Description: 分页查询咨询信息表
	 * @Create: 2013-3-1 下午01:58:03
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Potential model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计咨询信息表
	 * @Create: 2013-3-1 下午01:58:17
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Potential model);
	
}
