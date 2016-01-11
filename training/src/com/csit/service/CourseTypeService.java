package com.csit.service;

import com.csit.model.CourseType;
import com.csit.vo.ServiceResult;
/**
 * @Description:课程类型Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author lys
 */
public interface CourseTypeService extends BaseService<CourseType,Integer>{
	/**
	 * @Description: 保存课程类型
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult save(CourseType model);
	/**
	 * @Description: 删除课程类型
	 * @Created Time: 2013-2-26 下午5:28:59
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult delete(CourseType model);
	/**
	 * @Description: 分页查询课程类型
	 * @Created Time: 2013-2-26 下午5:29:27
	 * @Author lys
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(CourseType model, Integer page, Integer rows);
	/**
	 * @Description: 统计课程类型
	 * @Created Time: 2013-2-26 下午5:29:48
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(CourseType model);
	/**
	 * @Description: combobox查询
	 * @Created Time: 2013-2-26 下午5:30:02
	 * @Author lys
	 * @return
	 */
	String queryCombobox();

}
