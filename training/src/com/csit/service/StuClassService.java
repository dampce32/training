package com.csit.service;

import java.util.Date;

import com.csit.model.StuClass;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 选班Serive
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-14
 * @author yk
 * @vesion 1.0
 */
public interface StuClassService extends BaseService<StuClass, Integer> {
	/**
	 * 
	 * @Description: 保存选班 （选择已选班级、选择一对多班级）
	 * @param
	 * @Create: 2013-3-14 上午10:55:44
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult oneToManySave(StuClass model,Integer updateParam,Integer billDetailId);
	/**
	 * 
	 * @Description: 保存开班、选班（开设一对一班级）
	 * @Create: 2013-3-26 上午10:51:24
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult oneToOneSave(StuClass model,Integer billDetailId);
	/**
	 * 
	 * @Description: 删除选班 
	 * @param
	 * @Create: 2013-3-14 上午10:55:53
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(StuClass model);
	/**
	 * 
	 * @Description: 分页查询选班 
	 * @param
	 * @Create: 2013-3-14 上午10:56:03
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(StuClass model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计选班 
	 * @param
	 * @Create: 2013-3-14 上午10:56:13
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(StuClass model);
	/**
	 * 
	 * @Description: 查询选班信息
	 * @Create: 2013-3-26 下午02:23:45
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult query(StuClass model);
	/**
	 * 
	 * @Description: combobox查询学员已选班级
	 * @Create: 2013-3-28 下午05:26:41
	 * @author yk
	 * @update logs
	 * @param studentId
	 * @return
	 */
	String querySelectedClassCombobox(Integer courseId,Integer studentId,Integer scStatus);
	/**
	 * 
	 * @Description: 验证学员是否已选班
	 * @Create: 2013-4-7 上午09:23:18
	 * @author yk
	 * @update logs
	 * @return
	 */
	ServiceResult selectedValidate(StuClass model);
	/**
	 * 
	 * @Description: 获取学员列表tree
	 * @Create: 2013-4-8 上午11:21:32
	 * @author yk
	 * @update logs
	 * @param model
	 * @param lessonDegreeDate
	 * @param LessonDegreeId
	 * @return
	 */
	ServiceResult getStudentTree(StuClass model,Date lessonDegreeDate,Integer lessonDegreeId);
}
