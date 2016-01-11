package com.csit.service;
import java.util.List;

import com.csit.model.Right;
import com.csit.vo.ServiceResult;
/**
 * @Description: 系统权限Service
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-14
 * @author lys
 * @vesion 1.0
 */
public interface RightService extends BaseService<Right, Integer> {
	/**
	 * @Description: 选择权限的跟节点
	 * @Create: 2012-10-14 下午10:26:18
	 * @author lys
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	String selectRoot();
	/**
	 * @Description:添加权限 
	 * @Create: 2012-10-26 下午10:59:56
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ServiceResult add(Right model);
	/**
	 * @Description: 查询权限列表
	 * @Create: 2012-10-27 上午9:47:32
	 * @author lys
	 * @update logs
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 * @throws Exception
	 */
	String query(int page, int rows, Right model);
	/**
	 * @Description: 修改系统权限
	 * @Create: 2012-10-27 上午11:17:42
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ServiceResult update(Right model);
	/**
	 * @Description: 批量删除
	 * @Create: 2012-10-27 下午12:01:37
	 * @author lys
	 * @update logs
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	ServiceResult mulDelete(String ids);
	/**
	 * @Description: 单击树节点，取得单击节点的子节点
	 * @Create: 2012-10-27 下午3:23:23
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<Right> selectTreeNode(Right model);
	/**
	 * @Description: 保存权限
	 * @Create: 2013-1-22 上午10:34:09
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(Right model);
	/**
	 * @Description: 更新排序
	 * @Create: 2013-1-29 上午9:44:45
	 * @author lys
	 * @update logs
	 * @param rightId
	 * @param updateRightId
	 * @return
	 */
	ServiceResult updateArray(Integer rightId, Integer updateRightId);

}
