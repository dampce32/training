package com.csit.service;
import com.csit.model.Role;
import com.csit.vo.ServiceResult;
/**
 * @Description:
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-27
 * @author lys
 * @vesion 1.0
 */
public interface RoleService extends BaseService<Role, Integer>{
	/**
	 * @Description: 添加角色
	 * @Create: 2012-10-27 下午6:18:36
	 * @author lys
	 * @update logs
	 * @param model
	 * @param userId 
	 * @return
	 * @throws Exception
	 */
	ServiceResult add(Role model, Integer userId);
	/**
	 * @Description: 修改角色
	 * @Create: 2012-10-27 下午6:23:25
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ServiceResult update(Role model);
	/**
	 * @Description: 删除角色
	 * @Create: 2012-10-27 下午7:43:34
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Role model);
	/**
	 * @Description: 查询角色列表
	 * @Create: 2012-10-27 下午7:51:30
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	String query(Role model);

}
