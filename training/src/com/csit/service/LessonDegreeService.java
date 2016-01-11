package com.csit.service;

import com.csit.model.LessonDegree;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 排课表Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author yk
 * @vesion 1.0
 */
public interface LessonDegreeService extends BaseService<LessonDegree, Integer> {
	/**
	 * 
	 * @Description: 保存逐个排课
	 * @param
	 * @Create: 2013-3-7 下午02:27:10
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult save(LessonDegree model);
	/**
	 * 
	 * @Description: 删除排课
	 * @param
	 * @Create: 2013-3-7 下午02:27:20
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(LessonDegree model);
	/**
	 * 
	 * @Description: 分页查询排课
	 * @param
	 * @Create: 2013-3-7 下午02:27:28
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(LessonDegree model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计排课
	 * @param
	 * @Create: 2013-3-7 下午02:27:41
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(LessonDegree model);
	/**
	 * 
	 * @Description: 保存自动排课
	 * @param
	 * @Create: 2013-3-12 下午04:12:11
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult autoDegreeSave(String params,String firstDegreeDate,String waitDegree,
			String userId,LessonDegree model);
	/**
	 * 
	 * @Description: 清除排课
	 * @param
	 * @Create: 2013-3-13 下午12:33:40
	 * @author yk
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult clear(String idStr,String lessonStr,LessonDegree model);
	/**
	 * 
	 * @Description: 查询课程总表（周）
	 * @param
	 * @Create: 2013-3-18 下午04:01:20
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult weekTableQuery(String toSunday,String toWeekOfToday,LessonDegree model,
			Integer schoolId,Integer queryParam);
	/**
	 * 
	 * @Description: 查询某个学生的上课信息
	 * @Create: 2013-4-11 下午05:18:38
	 * @author jcf
	 * @update logs
	 * @param stuId
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult findByStu(Integer stuId, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计某个学生的上课信息
	 * @Create: 2013-4-12 上午11:19:57
	 * @author jcf
	 * @update logs
	 * @param stuId
	 * @return
	 */
	ServiceResult getTotalCountByStu(Integer stuId);
}
