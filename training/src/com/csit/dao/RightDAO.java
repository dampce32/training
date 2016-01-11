package com.csit.dao;

import java.util.List;

import com.csit.model.Right;
/**
 * @Description: 权限DAO
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-5
 * @author lys
 * @vesion 1.0
 */
public interface RightDAO extends BaseDAO<Right, Integer> {
	/**
	 * @Description: 选择权限的跟节点
	 * @Create: 2012-10-14 下午10:28:00
	 * @author lys
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	List<Right> selectRoot();
	/**
	 * @Description: 查询权限列表
	 * @Create: 2012-10-27 上午9:54:20
	 * @author lys
	 * @update logs
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<Right> query(int page, int rows, Right model);
	/**
	 * @Description: 统计权限列表
	 * @Create: 2012-10-27 上午10:15:22
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	Long count(Right model);
	/**
	 * @Description: 更新节点是否叶子节点状态
	 * @Create: 2012-10-27 下午2:32:36
	 * @author lys
	 * @update logs
	 * @param rightId
	 * @param b
	 * @throws Exception
	 */
	void updateIsLeaf(Integer rightId, boolean isLeaf);
	/**
	 * @Description: 统计该节点下的孩子节点数
	 * @Create: 2012-10-27 下午2:37:27
	 * @author lys
	 * @update logs
	 * @param parentID
	 * @return
	 * @throws Exception
	 */
	Long countChildren(Integer rightId);
	/**
	 * @Description: 取得孩子节点
	 * @Create: 2012-10-27 下午3:25:28
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<Right> getChildren(Right model);
	/**
	 * @Description: 取得权限rightId的父节点
	 * @Create: 2012-10-27 下午10:22:21
	 * @author lys
	 * @update logs
	 * @param rightId
	 * @return
	 * @throws Exception
	 */
	Right getParentRight(Integer rightId);
	/**
	 * @Description: 查找该父权限下的子权限排序最大值
	 * @Create: 2013-1-22 上午10:40:17
	 * @author lys
	 * @update logs
	 * @param rightId
	 * @return
	 */
	Integer getMaxArray(Integer rightId);
	/**
	 * @Description: 取得节点下有权限的权限子节点
	 * @Created Time: 2013-3-26 下午2:52:34
	 * @Author lys
	 * @param model
	 * @return
	 */
	List<Right> getCheckChildren(Right model);

}
