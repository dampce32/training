package com.csit.service;

import com.csit.model.PotCourse;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description:咨询课程Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
public interface PotCourseService extends BaseService<PotCourse, Integer> {

	/**
	 * 
	 * @Description: 保存咨询课程
	 * @Create: 2013-2-28 上午09:41:19
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(PotCourse model);
	/**
	 * 
	 * @Description: 删除咨询课程
	 * @Create: 2013-2-28 上午09:41:24
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(PotCourse model);
	/**
	 * 
	 * @Description: 分页查询咨询课程
	 * @Create: 2013-2-28 上午09:41:28
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(PotCourse model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计咨询课程
	 * @Create: 2013-2-28 上午09:41:32
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(PotCourse model);
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
	 * @Create: 2013-2-28 下午03:56:01
	 * @author jcf
	 * @update logs
	 * @param potCourseId
	 * @param state
	 * @return
	 */
	ServiceResult updateStatus(Integer potCourseId,Integer state);
}
