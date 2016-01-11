package com.csit.dao;

import java.util.List;

import com.csit.model.Leave;
/**
 * @Description:请假DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface LeaveDao extends BaseDAO<Leave,Integer>{

	
	/**
	 * 
	 * @Description: 分页查询请假
	 * @Create: 2013-2-28 下午1:40:05
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @throws Exception
	 */
	List<Leave> query(Leave model,String schoolCode,Integer page, Integer rows);
	/**
	 * 
	 * @Description: 
	 * @Create: 2013-2-28 下午1:43:43
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(Leave model,String schoolCode);
	/**
	 * 
	 * @Description: 学生或教师查询
	 * @Create: 2013-4-24 下午12:42:14
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Leave> query(Leave model,Integer page, Integer rows);
	/**
	 * 
	 * @Description: 学生或教师统计
	 * @Create: 2013-4-24 下午12:42:45
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Leave model);

}
