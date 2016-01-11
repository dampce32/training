package com.csit.service;
import com.csit.model.User;
import com.csit.vo.ServiceResult;
/**
 * @Description: 用户Service
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-28
 * @author lys
 * @vesion 1.0
 */
public interface UserService extends BaseService<User,Integer>{

	/**
	 * 
	 * @Description: 用户登录
	 * @Create: 2012-9-17 下午11:09:19
	 * @author lys
	 * @update logs
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	User login(String userCode, String userPwd);
	/**
	 * @Description: 添加用户
	 * @Create: 2012-10-28 上午8:55:29
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ServiceResult add(User model);
	/**
	 * @Description: 分页查询用户列表
	 * @Create: 2012-10-28 上午9:15:24
	 * @author lys
	 * @update logs
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 * @throws Exception
	 */
	String query(Integer page, Integer rows, User model);
	/**
	 * @Description: 修改用户
	 * @Create: 2012-10-28 上午9:36:06
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ServiceResult update(User model);
	/**
	 * @Description: 删除用户
	 * @Create: 2012-10-28 上午9:48:22
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(User model);
	/**
	 * @Description: 查询用户的跟权限
	 * @Create: 2012-10-28 下午11:56:20
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	String queryRootRight(User model);
	/**
	 * @Description: 当前用户的权限rightId下的孩子节点
	 * @Create: 2012-10-29 下午9:52:26
	 * @author lys
	 * @update logs
	 * @param model
	 * @param rightId
	 * @return
	 * @throws Exception
	 */
	String queryChildrenRight(User model, Integer rightId);
	/**
	 * @Description: 取得主界面权限树的第一层权限
	 * @Create: 2012-11-15 下午11:35:52
	 * @author lys
	 * @update logs
	 * @param userId
	 * @return
	 */
	String getRootRightMain(Integer userId);
	/**
	 * @Description: 取得用户个人信息
	 * @Create: 2013-1-22 下午1:57:21
	 * @author lys
	 * @update logs
	 * @param userId
	 * @return
	 */
	ServiceResult getSelfInfor(Integer userId);
	/**
	 * @Description: 更新用户个人信息
	 * @Create: 2013-1-22 下午2:07:53
	 * @author lys
	 * @update logs
	 * @param userId
	 * @param model
	 * @return
	 */
	ServiceResult updateSelfInfo(Integer userId, User model);
	/**
	 * @Description: 修改密码
	 * @Create: 2013-1-22 下午2:34:56
	 * @author lys
	 * @update logs
	 * @param model
	 * @param newUserPwd
	 * @return
	 */
	ServiceResult modifyPwd(User model, String newUserPwd);
	/**
	 * @Description: 保存用户
	 * @Created Time: 2013-2-28 上午9:51:40
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult save(User model);
	/**
	 * @Description: 保存员工
	 * @Created Time: 2013-2-28 下午3:00:44
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult saveEmployee(User model);
	/**
	 * @Description: 分页查询员工
	 * @Created Time: 2013-2-28 下午3:41:06
	 * @Author lys
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult queryEmployee(User model, Integer page, Integer rows);
	/**
	 * @Description: 统计员工
	 * @Created Time: 2013-2-28 下午3:42:06
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCountEmployee(User model);
	/**
	 * @Description: 打开初始化
	 * @Created Time: 2013-2-28 下午10:13:50
	 * @Author lys
	 * @param userId
	 * @return
	 */
	ServiceResult init(Integer userId);
	/**
	 * @Description: 批量删除员工
	 * @Created Time: 2013-2-28 下午10:52:24
	 * @Author lys
	 * @param ids
	 * @return
	 */
	ServiceResult mulDelele(String ids);
	/**
	 * @Description: 批量修改员工状态
	 * @Created Time: 2013-2-28 下午10:58:31
	 * @Author lys
	 * @param ids
	 * @param model
	 * @return
	 */
	ServiceResult mulUpdateStatus(String ids, User model);
	/**
	 * @Description: 用于Combobox查询
	 * @Created Time: 2013-3-1 下午5:10:30
	 * @Author lys
	 * @param schoolCode
	 * @return
	 */
	String queryCombobox(String schoolCode);
	/**
	 * @Description: 查询本校区下的授课教师(用于Combobox)
	 * @Created Time: 2013-3-4 下午2:14:50
	 * @Author lys
	 * @param schoolCode
	 * @return
	 */
	String queryIsTeacherCombobox(String schoolCode);
	/**
	 * @Description: 取得主界面下的所有Url权限
	 * @Created Time: 2013-3-26 下午4:06:44
	 * @Author lys
	 * @param parseInt
	 * @return
	 */
	String getUrlRightAll(Integer userId);
	/**
	 * 根据权限类型取得子权限
	 */
	String queryChildrenRight(User model, Integer rightId, Integer kind);

}
