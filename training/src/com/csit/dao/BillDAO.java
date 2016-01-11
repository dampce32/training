package com.csit.dao;

import java.util.Date;
import java.util.List;

import com.csit.model.Bill;
/**
 * @Description:消费单Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-11
 * @author jcf
 * @vesion 1.0
 */
public interface BillDAO extends BaseDAO<Bill,Integer>{
	/**
	 * @Description: 分页查询消费单
	 * @Create: 2013-3-11 下午03:10:32
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Bill> query(Bill model,Date beginDate,Date endDate, Integer page, Integer rows);
	/**
	 * @Description: 统计消费单
	 * @Create: 2013-3-11 下午03:10:52
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Bill model,Date beginDate,Date endDate);
	/**
	 * 
	 * @Description: 初始化消费单号
	 * @Create: 2013-3-13 上午09:03:41
	 * @author jcf
	 * @update logs
	 * @param date
	 * @return
	 */
	Long initBillCode(String date);
}
