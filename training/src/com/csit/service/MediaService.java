package com.csit.service;

import com.csit.model.Media;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description:媒体Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
public interface MediaService extends BaseService<Media, Integer> {

	/**
	 * 
	 * @Description: 保存媒体
	 * @Create: 2013-2-28 上午09:41:19
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(Media model);
	/**
	 * 
	 * @Description: 删除媒体
	 * @Create: 2013-2-28 上午09:41:24
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(Media model);
	/**
	 * 
	 * @Description: 分页查询媒体
	 * @Create: 2013-2-28 上午09:41:28
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Media model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计媒体
	 * @Create: 2013-2-28 上午09:41:32
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Media model);
	/**
	 * 
	 * @Description: combobox查询
	 * @Create: 2013-2-28 上午09:41:36
	 * @author jcf
	 * @update logs
	 * @return
	 */
	String queryCombobox();
	/**
	 * 
	 * @Description: 修改状态
	 * @Create: 2013-2-28 下午02:59:03
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult updateStatus(Integer mediaId,Integer state);
}
