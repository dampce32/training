package com.csit.service;

import com.csit.model.PotentialStuStatus;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description:潜在生源状态Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
public interface PotentialStuStatusService extends BaseService<PotentialStuStatus, Integer> {

	/**
	 * 
	 * @Description: 保存潜在生源状态
	 * @Create: 2013-2-28 上午09:41:19
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(PotentialStuStatus model);
	/**
	 * 
	 * @Description: 删除潜在生源状态
	 * @Create: 2013-2-28 上午09:41:24
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(PotentialStuStatus model);
	/**
	 * 
	 * @Description: 分页查询潜在生源状态
	 * @Create: 2013-2-28 上午09:41:28
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(PotentialStuStatus model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计潜在生源状态
	 * @Create: 2013-2-28 上午09:41:32
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(PotentialStuStatus model);
	/**
	 * 
	 * @Description: combobox查询
	 * @Create: 2013-2-28 上午09:41:36
	 * @author jcf
	 * @update logs
	 * @return
	 */
	String queryCombobox();
	/**
	 * 
	 * @Description: 修改状态
	 * @Create: 2013-2-28 下午04:03:20
	 * @author jcf
	 * @update logs
	 * @param potentialStuStatusId
	 * @param state
	 * @return
	 */
	ServiceResult updateStatus(Integer potentialStuStatusId,Integer state);
}
