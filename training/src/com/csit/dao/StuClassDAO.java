package com.csit.dao;

import java.util.Date;
import java.util.List;

import com.csit.model.StuClass;
/**
 * 
 * @Description: 选班表DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-14
 * @author yk
 * @vesion 1.0
 */
public interface StuClassDAO extends BaseDAO<StuClass, Integer> {
	/**
	 * 
	 * @Description: 分页查询选班
	 * @param
	 * @Create: 2013-3-14 上午10:49:06
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<StuClass> query(StuClass model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计选班 
	 * @param
	 * @Create: 2013-3-14 上午10:49:21
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(StuClass model);
	/**
	 * 
	 * @Description: 查询选班信息
	 * @Create: 2013-3-26 下午02:18:32
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 */
	List<StuClass> query(StuClass model,Date lessonDegreeDate);
	/**
	 * 
	 * @Description: combobox查询学生已选班级
	 * @Create: 2013-3-28 下午05:21:59
	 * @author yk
	 * @update logs
	 * @param studentId
	 * @return
	 */
	List<StuClass> querySelectedClassCombobox(Integer courseId,Integer studentId,Integer scStatus);
	/**
	 * 
	 * @Description: 查询学生所选班级的状态
	 * @Create: 2013-3-29 上午11:32:08
	 * @author yk
	 * @update logs
	 * @param studentId
	 * @param classId
	 * @return
	 */
	StuClass getStuClass(Integer studentId,Integer classId,Integer param,Date maxSelectDate);
	/**
	 * 
	 * @Description: 最新选班日期
	 * @Create: 2013-4-2 上午10:45:51
	 * @author yk
	 * @update logs
	 * @param studentId
	 * @return
	 */
	Date getMax(Integer studentId);
}
