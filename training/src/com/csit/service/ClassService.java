package com.csit.service;

import com.csit.model.Clazz;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 班级Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @author yk
 * @vesion 1.0
 */
public interface ClassService extends BaseService<Clazz, Integer> {
	/**
	 * 
	 * @Description: 保存班级
	 * @param
	 * @Create: 2013-3-4 下午03:46:24
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult save(Clazz model);
	/**
	 * 
	 * @Description: 删除班级
	 * @param
	 * @Create: 2013-3-4 下午03:46:40
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Clazz model);
	/**
	 * 
	 * @Description: 分页查询班级
	 * @param
	 * @Create: 2013-3-4 下午03:46:55
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(Clazz model, Integer page, Integer rows,Integer status);
	/**
	 * 
	 * @Description: 统计班级
	 * @param
	 * @Create: 2013-3-4 下午03:47:07
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Clazz model,Integer status);
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-3-4 下午03:47:19
	 * @author yk
	 * @param model 
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryCombobox(Clazz model);
	/**
	 * 
	 * @Description: combobox查询指定校区、上课状态、讲师下的班级
	 * @Create: 2013-3-21 下午04:15:17
	 * @author yk
	 * @update logs
	 * @param lessonStatus
	 * @param model
	 * @return
	 */
	String queryInSSTCombobox(Integer courseId,Integer schoolId,Integer lessonStatus, Integer teacherId);
	/**
	 * 
	 * @Description: 根据班级Id查询班级，根据学生Id、班级Id查询选班状态
	 * @Create: 2013-3-27 下午05:26:58
	 * @author yk
	 * @update logs
	 * @param classId
	 * @return
	 */
	ServiceResult queryOne(Integer studentId,Integer classId);
}
