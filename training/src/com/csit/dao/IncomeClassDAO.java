package com.csit.dao;

import java.util.List;

import com.csit.model.IncomeClass;

/**
 * @Description:帐目分类表Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
public interface IncomeClassDAO extends BaseDAO<IncomeClass,Integer>{
	/**
	 * @Description: 分页查询帐目分类表
	 * @Create: 2013-3-28 下午03:48:41
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<IncomeClass> query(IncomeClass model, Integer page, Integer rows);
	/**
	 * @Description: 统计帐目分类表
	 * @Create: 2013-3-28 下午03:48:52
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(IncomeClass model);
	/**
	 * 
	 * @Description: combobox使用
	 * @Create: 2013-3-29 上午11:47:26
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	List<IncomeClass> queryByType(IncomeClass model);
}
