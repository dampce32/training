package com.csit.service;

import com.csit.model.Classroom;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 教室Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author yk
 * @vesion 1.0
 */
public interface ClassroomService extends BaseService<Classroom, Integer> {
	/**
	 * 
	 * @Description: 保存教室
	 * @param
	 * @Create: 2013-3-1 上午08:57:35
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult save(Classroom model);
	/**
	 * 
	 * @Description: 删除教室
	 * @param
	 * @Create: 2013-3-1 上午08:57:53
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Classroom model);
	/**
	 * 
	 * @Description: 分页查询教室
	 * @param
	 * @Create: 2013-3-1 上午08:58:03
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(Classroom model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计教室
	 * @param
	 * @Create: 2013-3-1 上午08:58:13
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Classroom model);
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-3-1 上午08:58:22
	 * @author yk
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryCombobox();
	/**
	 * 
	 * @Description: 修改教室状态
	 * @param
	 * @Create: 2013-3-1 下午04:21:56
	 * @author yk
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult updateStatus(Classroom model);
	/**
	 * 
	 * @Description: combobox查询指定校区的教室
	 * @param
	 * @Create: 2013-3-5 下午01:51:43
	 * @author yk
	 * @update logs
	 * @param schoolCode
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryInSchoolCombobox(Integer schoolId);
}
