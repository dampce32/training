package com.csit.service;

import com.csit.model.IncomeClass;
import com.csit.vo.ServiceResult;
/**
 * @Description:帐目分类表Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
public interface IncomeClassService extends BaseService<IncomeClass, Integer> {

	/**
	 * @Description: 保存帐目分类表
	 * @Create: 2013-3-28 下午03:54:02
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(IncomeClass model);
	/**
	 * @Description: 删除帐目分类表
	 * @Create: 2013-3-28 下午03:54:11
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(IncomeClass model);
	/**
	 * @Description: 分页查询帐目分类表
	 * @Create: 2013-3-28 下午03:54:20
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(IncomeClass model, Integer page, Integer rows);
	/**
	 * @Description: 统计帐目分类表
	 * @Create: 2013-3-28 下午03:54:29
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(IncomeClass model);
	/**
	 * @Description: combobox查询
	 * @Create: 2013-3-28 下午03:54:38
	 * @author jcf
	 * @update logs
	 * @return
	 */
	String queryCombobox(IncomeClass model);
	/**
	 * @Description: 修改状态
	 * @Create: 2013-3-28 下午03:54:49
	 * @author jcf
	 * @update logs
	 * @param IncomeClassId
	 * @param state
	 * @return
	 */
	ServiceResult updateStatus(Integer incomeClassId,Integer state);
}
