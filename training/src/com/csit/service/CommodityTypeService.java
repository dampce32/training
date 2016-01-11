package com.csit.service;

import com.csit.model.CommodityType;
import com.csit.vo.ServiceResult;
/**
 * @Description:商品分类Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface CommodityTypeService extends BaseService<CommodityType,Integer>{
	/**
	 * @Description: 保存商品分类
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @return
	 */
	ServiceResult save(CommodityType model);

	/**
	 * @Description: 分页查询商品分类
	 * @Create: 2013-2-28 上午11:28:25
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param rows 
	 * @param page 
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(CommodityType model, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计商品分类
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(CommodityType model);

	/**
	 * 
	 * @Description: 删除商品分类
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(CommodityType model);

	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-2-28 下午2:21:05
	 * @author cjp
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryCombobox();
	
	/**
	 * 
	 * @Description: 修改状态
	 * @param
	 * @Create: 2013-3-1 下午5:14:18
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult updateStatus(CommodityType model);
}
