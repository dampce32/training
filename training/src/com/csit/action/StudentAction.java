package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Student;
import com.csit.model.User;
import com.csit.service.StudentService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @Description:学员Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-5
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class StudentAction extends BaseAction implements ModelDriven<Student> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(StudentAction.class);
	private Student model = new Student();
	private Integer potentialId;
	private Integer type;
	@Resource
	private StudentService studentService;

	@Override
	public Student getModel() {
		return model;
	}

	/**
	 * 
	 * @Description: 保存学员
	 * @Create: 2013-3-5 上午10:48:10
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = studentService.save(model,potentialId);
		} catch (Exception e) {
			result.setMessage("保存学员失败");
			logger.error("保存学员失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 删除学员
	 * @Create: 2013-3-5 上午10:48:21
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = studentService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除学员失败");
				logger.error("删除学员失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 分页查询学员
	 * @Create: 2013-2-28 上午10:04:51
	 * @author jcf
	 * @update logs
	 */
	public void query() {
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			if(type==0){
				Integer userId=(Integer) getSession().get(User.LOGIN_USERID);
				User user=new User();
				user.setUserId(userId);
				model.setUser(user);
			}
			result = studentService.query(model, page, rows);
		} catch (Exception e) {
			result.setMessage("查询学员失败");
			logger.error("查询学员失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 统计学员
	 * @Create: 2013-3-5 上午10:48:31
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			if(type==0){
				Integer userId=(Integer) getSession().get(User.LOGIN_USERID);
				User user=new User();
				user.setUserId(userId);
				model.setUser(user);
			}
			result = studentService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计学员失败");
			logger.error("统计学员失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: combobox查询
	 * @Create: 2012-12-29 下午11:18:19
	 * @author cjp
	 * @update logs
	 */
	public void queryCombobox() {
		String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
		String jsonString = studentService.queryCombobox(schoolCode);
		ajaxJson(jsonString);
	}
	public void queryComboboxByTea() {
		Integer userId=(Integer) getSession().get(User.LOGIN_USERID);
		User user=new User();
		user.setUserId(userId);
		model.setUser(user);
		String jsonString = studentService.queryCombobox(model);
		ajaxJson(jsonString);
	}
	
	public void init(){
		ServiceResult result = new ServiceResult(false);
		try {
			if(type==0){
				result = studentService.getStuById(model.getStudentId());
			}
			if(type==1){
				Integer studentId=(Integer) getSession().get(Student.LOGIN_STUDENTID);
				result = studentService.getStuById(studentId);
			}
		} catch (Exception e) {
			result.setMessage("初始化学生信息失败");
			logger.error("初始化学生信息失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 修改密码
	 * @Create: 2013-4-2 上午09:02:50
	 * @author jcf
	 * @update logs
	 */
	public void modifyPwd(){
		ServiceResult result = new ServiceResult(false);
		String studentId = null;
		if(getSession(Student.LOGIN_STUDENTID)!=null){
			studentId = getSession(Student.LOGIN_STUDENTID).toString();
		}
		model.setStudentId(Integer.parseInt(studentId));
		String newStuPwd = getParameter("newStuPwd");
		try {
			result = studentService.modifyPwd(model,newStuPwd);
		} catch (Exception e) {
			result.setMessage("修改密码失败");
			logger.error("修改密码失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	public Integer getPotentialId() {
		return potentialId;
	}

	public void setPotentialId(Integer potentialId) {
		this.potentialId = potentialId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
