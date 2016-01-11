package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.User;
import com.csit.service.UserService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:员工管理
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @Author lys
 */
@Controller
@Scope("prototype")
public class EmployeeAction extends BaseAction implements ModelDriven<User> {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EmployeeAction.class);
	private User model = new User();

	@Resource
	private UserService userService;

	public User getModel() {
		return model;
	}
	/**
	 * @Description: 保存员工
	 * @Created Time: 2013-2-28 下午2:58:48
	 * @Author lys
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = userService.saveEmployee(model);
		} catch (Throwable e) {
			result.setMessage("保存员工失败");
			logger.error("保存员工失败", e);
		}
		String ajaxString = result.toJSON();
		ajaxJson(ajaxString);
	}
	
	/**
	 * @Description: 分页查询员工
	 * @Create: 2012-10-28 上午9:14:13
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = userService.queryEmployee(model,page,rows);
		} catch (Exception e) {
			result.setMessage("分页查询员工失败");
			logger.error("分页查询员工失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 统计员工
	 * @Created Time: 2013-2-28 下午3:02:23
	 * @Author lys
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = userService.getTotalCountEmployee(model);
		} catch (Exception e) {
			result.setMessage("统计员工失败");
			logger.error("统计员工失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 打开初始化
	 * @Create: 2013-1-8 下午10:28:15
	 * @author lys
	 * @update logs
	 */
	public void init(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = userService.init(model.getUserId());
		} catch (Exception e) {
			result.setMessage("打开初始化失败");
			logger.error("打开初始化失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 删除员工
	 * @Created Time: 2013-2-28 下午10:48:05
	 * @Author lys
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = userService.delete(model);
		} catch (Throwable e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除员工失败");
				logger.error("删除员工失败", e);
			}
		}
		String ajaxString = result.toJSON();
		ajaxJson(ajaxString);
	}
	/**
	 * @Description: 批量删除员工
	 * @Created Time: 2013-2-28 下午10:51:10
	 * @Author lys
	 */
	public void mulDelele(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = userService.mulDelele(ids);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("批量删除员工失败");
				logger.error("批量删除员工失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 批量修改员工状态
	 * @Created Time: 2013-2-28 下午10:57:47
	 * @Author lys
	 */
	public void mulUpdateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = userService.mulUpdateStatus(ids,model);
		} catch (Exception e) {
			result.setMessage("批量修改员工状态失败");
			logger.error("批量修改员工状态失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 用于Combobox查询
	 * @Created Time: 2013-3-1 下午5:02:50
	 * @Author lys
	 */
	public void queryCombobox(){
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			String jsonString = userService.queryCombobox(schoolCode);
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @Description: 查询本校区下的授课教师(用于Combobox)
	 * @Created Time: 2013-3-4 下午2:13:58
	 * @Author lys
	 */
	public void queryIsTeacherCombobox(){
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			String jsonString = userService.queryIsTeacherCombobox(schoolCode);
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
