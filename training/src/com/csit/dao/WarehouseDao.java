package com.csit.dao;

import java.util.List;

import com.csit.model.Warehouse;
/**
 * @Description:仓库DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface WarehouseDao extends BaseDAO<Warehouse,Integer>{

	
	/**
	 * 
	 * @Description: 分页查询仓库
	 * @Create: 2013-2-28 下午1:40:05
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @throws Exception
	 */
	List<Warehouse> query(Warehouse model, Integer page, Integer rows);
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
	Long getTotalCount(Warehouse model);
	/**
	 * 
	 * @Description: 查询用户所在校区仓库
	 * @Create: 2013-4-22 下午01:08:42
	 * @author jcf
	 * @update logs
	 * @param schoolCode
	 * @return
	 */
	List<Warehouse> querySelfCombobox(String schoolCode);

}
