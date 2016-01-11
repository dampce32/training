package com.csit.service;

import com.csit.model.Student;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description:学员Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-5
 * @author jcf
 * @vesion 1.0
 */
public interface StudentService extends BaseService<Student, Integer> {

	/**
	 * 
	 * @Description: 保存学员
	 * @Create: 2013-3-5 上午10:27:36
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(Student model,Integer potentialId);
	
	/**
	 * @Description: 删除学员
	 * @Create: 2013-3-5 上午10:28:28
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(Student model);
	
	/**
	 * @Description: 分页查询学员
	 * @Create: 2013-3-5 上午10:28:47
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Student model, Integer page, Integer rows);
	
	/**
	 * @Description: 统计学员
	 * @Create: 2013-3-5 上午10:29:11
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Student model);
	/**
	 * 
	 * @Description: 通过Id获得学员
	 * @Create: 2013-4-2 上午08:55:25
	 * @author jcf
	 * @update logs
	 * @param studentId
	 * @return
	 */
	ServiceResult getStuById(Integer studentId);
	/**
	 * @Description: 学生登陆
	 * @Created Time: 2013-3-29 下午1:45:10
	 * @Author lys
	 * @param userCode
	 * @param md5
	 * @return
	 */
	Student login(Integer userCodeInt, String userPwd);
	/**
	 * 
	 * @Description: combobox查询所有学员
	 * @param
	 * @Create: 2013-2-28 下午2:21:05
	 * @author cjp
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryCombobox();
	/**
	 * 
	 * @Description: 修改密码
	 * @Create: 2013-4-2 上午08:56:06
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param newUserPwd
	 * @return
	 */
	ServiceResult modifyPwd(Student model, String newUserPwd);
	/**
	 * 
	 * @Description: 查询本校区学员
	 * @Create: 2013-4-23 上午10:28:47
	 * @author jcf
	 * @update logs
	 * @param schoolCode
	 * @return
	 */
	String queryCombobox(String schoolCode);
	/**
	 * 
	 * @Description: 教师查询自己学员
	 * @Create: 2013-4-24 下午02:29:55
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	String queryCombobox(Student model);
	
}
