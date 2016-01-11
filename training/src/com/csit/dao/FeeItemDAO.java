package com.csit.dao;

import java.util.List;

import com.csit.model.FeeItem;
/**
 * 
 * @Description: 消费项DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
public interface FeeItemDAO extends BaseDAO<FeeItem, Integer> {
	/**
	 * 
	 * @Description: 分页查询消费项
	 * @param
	 * @Create: 2013-2-28 上午10:44:43
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<FeeItem> query(FeeItem model, Integer page, Integer rows, Integer[] feeItemIdArr);
	/**
	 * 
	 * @Description: 统计消费项
	 * @param
	 * @Create: 2013-2-28 上午10:45:04
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(FeeItem model, Integer[] feeItemIdArr);
}
