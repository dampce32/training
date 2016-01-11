package com.csit.dao;

import java.util.List;

import com.csit.model.CommodityType;
/**
 * @Description:商品分类DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface CommodityTypeDao extends BaseDAO<CommodityType,Integer>{

	
	/**
	 * 
	 * @Description: 分页查询商品分类
	 * @Create: 2013-2-28 下午1:40:05
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @throws Exception
	 */
	List<CommodityType> query(CommodityType model, Integer page, Integer rows);
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
	Long getTotalCount(CommodityType model);

}
