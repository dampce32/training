package com.csit.dao;

import java.util.List;

import com.csit.model.UserRole;
import com.csit.model.UserRoleId;
/**
 * @Description:用户角色DAO
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-28
 * @author lys
 * @vesion 1.0
 */
public interface UserRoleDAO extends BaseDAO<UserRole,UserRoleId>{
	/**
	 * @Description: 取得用户model的用户角色选择情况
	 * @Create: 2012-10-28 下午9:55:47
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<UserRole> queryRole(UserRole model);

}
