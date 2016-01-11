package com.csit.dao;

import java.util.List;

import com.csit.model.RoleRight;
import com.csit.model.RoleRightId;

/**
 * @Description: 角色权限DAO
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-27
 * @author lys
 * @vesion 1.0
 */
public interface RoleRightDAO extends BaseDAO<RoleRight, RoleRightId>{
	/**
	 * @Description:  查询角色第一层权限
	 * @Create: 2012-10-27 下午9:13:29
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<RoleRight> queryRoot(RoleRight model);
	/**
	 * @Description: 查询树某节点下的子节点
	 * @Create: 2012-10-27 下午9:38:15
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<RoleRight> queryChildren(RoleRight model);
	/**
	 * @Description: 更新角色roleId权限rightId的状态
	 * @Create: 2012-10-27 下午9:53:50
	 * @author lys
	 * @update logs
	 * @param roleId
	 * @param rightId
	 * @param state
	 * @throws Exception
	 */
	void updateState(Integer roleId, Integer rightId, Integer state);
	/**
	 * @Description: 统计和角色权限model相同父亲的节点下状态时state的孩子个数
	 * @Create: 2012-10-27 下午10:05:28
	 * @author lys
	 * @update logs
	 * @param model
	 * @param b
	 * @return
	 * @throws Exception
	 */
	Integer countChildrenStateSameParent(RoleRight model, Integer state);
}
