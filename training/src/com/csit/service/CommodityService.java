package com.csit.service;

import com.csit.model.Commodity;
import com.csit.vo.ServiceResult;
/**
 * @Description:商品Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface CommodityService extends BaseService<Commodity,Integer>{
	/**
	 * @Description: 保存商品
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @return
	 */
	ServiceResult save(Commodity model);

	/**
	 * @Description: 分页查询商品
	 * @Create: 2013-2-28 上午11:28:25
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param rows 
	 * @param page 
	 * @param commodityIds 
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(Commodity model, Integer page, Integer rows, String commodityIds);

	/**
	 * 
	 * @Description: 统计商品
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param commodityIds 
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Commodity model, String commodityIds);

	/**
	 * 
	 * @Description: 删除商品
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Commodity model);

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

	ServiceResult updateStatus(Commodity model);

	/**
	 * 
	 * @Description:商品库存预警
	 * @param
	 * @Create: 2013-2-28 下午2:21:05
	 * @author cjp
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult queryQtyWarm(Commodity model,Integer page, Integer rows);
	/**
	 * 
	 * @Description: 分页查询商品
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @param model 
	 * @update logs
	 * @throws Exception
	 */
	ServiceResult getTotalCountQtyWarm(Commodity model);
}
