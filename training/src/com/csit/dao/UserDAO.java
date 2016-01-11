package com.csit.dao;

import java.util.List;
import java.util.Map;

import com.csit.model.User;
/**
 * @Description:用户DAO
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-28
 * @author lys
 * @vesion 1.0
 */
public interface UserDAO extends BaseDAO<User,Integer>{

	User login(User operator);
	/**
	 * @Description: 分页查询用户列表
	 * @Create: 2012-10-28 上午9:16:34
	 * @author lys
	 * @update logs
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<User> query(int page, int rows, User model);
	/**
	 * @Description: 统计用户数
	 * @Create: 2012-10-28 上午9:21:04
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	Long count(User model);
	/**
	 * @Description: 取得用户userId的跟权限
	 * @Create: 2012-10-29 上午12:00:10
	 * @author lys
	 * @update logs
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getRootRight(Integer userId);
	/**
	 * @Description: 取得用户userId下的权限rightId下的孩子权限
	 * @Create: 2012-10-29 上午12:14:30
	 * @author lys
	 * @update logs
	 * @param userId
	 * @param rightId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getChildrenRight(Integer userId, Integer rightId);
	/**
	 * @Description: 取得用户userId的用户权限
	 * @Create: 2012-10-29 下午10:31:06
	 * @author lys
	 * @update logs
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> queryUserRight(Integer userId);
	/**
	 * @Description: 分页查询员工列表
	 * @Created Time: 2013-2-28 下午3:43:16
	 * @Author lys
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<User> queryEmployee(User model, Integer page, Integer rows);
	/**
	 * @Description: 统计员工
	 * @Created Time: 2013-2-28 下午3:45:28
	 * @Author lys
	 * @param model
	 * @return
	 */
	Long getTotalCountEmployee(User model);
	/**
	 * @Description: 查询本校区的员工
	 * @Created Time: 2013-3-1 下午5:13:09
	 * @Author lys
	 * @param schoolCode
	 * @return
	 */
	List<User> queryCombobox(String schoolCode);
	/**
	 * @param isTeacher 
	 * @Description: 查询本校区下的授课教师(用于Combobox)
	 * @Created Time: 2013-3-4 下午2:15:23
	 * @Author lys
	 * @param schoolCode
	 * @return
	 */
	List<User> queryIsTeacherCombobox(Integer isTeacher, String schoolCode);
	/**
	 * @Description: 根据类型取得子权限
	 * @Created Time: 2013-3-26 下午4:25:46
	 * @Author lys
	 * @param userId
	 * @param rightId
	 * @param kind
	 * @return
	 */
	List<Map<String, Object>> getChildrenRight(Integer userId, Integer rightId,
			Integer kind);

}
