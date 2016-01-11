package com.csit.service;

import com.csit.model.Leave;
import com.csit.vo.ServiceResult;
/**
 * @Description:请假Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface LeaveService extends BaseService<Leave,Integer>{
	/**
	 * @Description: 保存请假
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @return
	 */
	ServiceResult save(Leave model);

	/**
	 * @Description: 分页查询请假
	 * @Create: 2013-2-28 上午11:28:25
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param rows 
	 * @param page 
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(Leave model,String schoolCode, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计请假
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param endDate 
	 * @param beginDate 
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Leave model,String schoolCode);

	/**
	 * 
	 * @Description: 删除请假
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Leave model);
	/**
	 * 
	 * @Description: 教师或学员查询
	 * @Create: 2013-4-24 下午01:37:28
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Leave model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 教师或学员统计
	 * @Create: 2013-4-24 下午01:37:55
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param schoolCode
	 * @return
	 */
	ServiceResult getTotalCount(Leave model);
}
