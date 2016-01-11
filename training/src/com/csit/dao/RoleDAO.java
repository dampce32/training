package com.csit.dao;

import java.util.List;

import com.csit.model.Role;
/**
 * @Description: 角色DAO
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-27
 * @author lys
 * @vesion 1.0
 */
public interface RoleDAO extends BaseDAO<Role, Integer>{
	/**
	 * @Description: 查询角色列表
	 * @Create: 2012-10-27 下午7:53:22
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<Role> query(Role model);
	/**
	 * @Description: 查询所有的角色
	 * @Create: 2012-10-28 下午10:49:10
	 * @author lys
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	List<Role> queryAll();

}
