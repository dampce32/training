package com.csit.dao;

import java.util.List;

import com.csit.model.Supplier;
/**
 * @Description:供应商DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface SupplierDao extends BaseDAO<Supplier,Integer>{

	
	/**
	 * 
	 * @Description: 分页查询供应商
	 * @Create: 2013-2-28 下午1:40:05
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @throws Exception
	 */
	List<Supplier> query(Supplier model, Integer page, Integer rows);
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
	Long getTotalCount(Supplier model);

}
