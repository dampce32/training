package com.csit.service;
import com.csit.model.RoleRight;
import com.csit.model.RoleRightId;
import com.csit.vo.ServiceResult;

/**
 * @Description: 角色权限Service
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-27
 * @author lys
 * @vesion 1.0
 */
public interface RoleRightService extends BaseService<RoleRight, RoleRightId>{
	/**
	 * @Description: 查询角色第一层权限
	 * @Create: 2012-10-27 下午9:09:28
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	String queryRoot(RoleRight model);
	/**
	 * @Description: 查询树某节点下的子节点
	 * @Create: 2012-10-27 下午9:35:47
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	String queryChildren(RoleRight model);
	/**
	 * @Description: 更新用户角色权限状态
	 * @Create: 2012-10-27 下午9:46:30
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ServiceResult updateState(RoleRight model);

}
