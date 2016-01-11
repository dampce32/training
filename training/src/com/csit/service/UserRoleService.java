package com.csit.service;

import com.csit.model.UserRole;
import com.csit.vo.ServiceResult;

/**
 * @Description: 用户角色Service
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-28
 * @author lys
 * @vesion 1.0
 */
public interface UserRoleService {
	/**
	 * @Description: 查询用户的角色列表
	 * @Create: 2012-10-28 下午9:51:27
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	String queryRole(UserRole model);
	/**
	 * @Description: 更新用户角色
	 * @Create: 2012-10-28 下午11:15:57
	 * @author lys
	 * @update logs
	 * @param model
	 * @param ids
	 * @param oldIds
	 * @return
	 * @throws Exception
	 */
	ServiceResult updateRole(UserRole model, String ids, String oldIds);

}
