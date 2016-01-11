package com.csit.dao;

import java.util.Date;
import java.util.List;

import com.csit.model.BillDetail;
/**
 * @Description:消费单明细Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-11
 * @author jcf
 * @vesion 1.0
 */
public interface BillDetailDAO extends BaseDAO<BillDetail,Integer>{

	/**
	 * 
	 * @Description: 分页查询消费单详细
	 * @Create: 2013-3-18 下午04:18:22
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<BillDetail> query(BillDetail model,Date beginDate,Date endDate, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计消费单详细
	 * @Create: 2013-3-18 下午04:18:55
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(BillDetail model,Date beginDate,Date endDate);
	/**
	 * 
	 * @Description: 分页查询选班处理
	 * @Create: 2013-3-20 下午05:29:49
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<BillDetail> aboutStuClassQuery(BillDetail model, Integer page, Integer rows);
}
