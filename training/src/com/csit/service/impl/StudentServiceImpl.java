package com.csit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.PotentialDAO;
import com.csit.dao.StudentDAO;
import com.csit.model.Potential;
import com.csit.model.Student;
import com.csit.service.StudentService;
import com.csit.util.JSONUtil;
import com.csit.util.MD5Util;
import com.csit.vo.GobelConstants;
import com.csit.vo.ServiceResult;

/**
 * 
 * @Description:学员Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-5
 * @author jcf
 * @vesion 1.0
 */
@Service
public class StudentServiceImpl extends BaseServiceImpl<Student, Integer> implements StudentService {

	@Resource
	private StudentDAO studentDAO;
	@Resource
	private PotentialDAO potentialDAO;
	
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StudentService#delete(com.csit.model.Student)
	 */
	public ServiceResult delete(Student model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getStudentId() == null) {
			result.setMessage("请选择要删除的学员");
			return result;
		}
		Student oldModel = studentDAO.load(model.getStudentId());
		if (oldModel == null) {
			result.setMessage("该学员已不存在");
			return result;
		} else {
			studentDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StudentService#getTotalCount(com.csit.model.Student)
	 */
	public ServiceResult getTotalCount(Student model) {
		ServiceResult result = new ServiceResult(false);
		Long data = studentDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StudentService#query(com.csit.model.Student, java.lang.Integer, java.lang.Integer)
	 */
	public ServiceResult query(Student model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<Student> list = studentDAO.query(model, page, rows);

		String[] properties = { "studentId", "media.mediaName","media.mediaId","school.schoolName","school.schoolId","user.userName","user.userId", "studentName","appellation"
								,"sex", "studentType","birthday","password", "enrollDate","tel","mobileTel", "tel1","qq"
								,"email", "address","postCode","idcard", "diploma","nextReplyDate","note", "consumedMoney","insertDate"
								,"billCount", "arrearageMoney","availableMoney","creditExpiration","newStuClass"};

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StudentService#save(com.csit.model.Student)
	 */
	public ServiceResult save(Student model,Integer potentialId) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写学员信息");
			return result;
		}
		if (StringUtils.isEmpty(model.getStudentName())) {
			result.setMessage("请填写学员名称");
			return result;
		}
		if(model.getSex()==null){
			result.setMessage("请填写学员性别");
			return result;
		}
		if(model.getStudentType()==null){
			result.setMessage("请填写学员类型");
			return result;
		}
		if(model.getSchool()==null||model.getSchool().getSchoolId()==null){
			result.setMessage("请选择校区");
			return result;
		}
		if(model.getEnrollDate()==null){
			result.setMessage("请填写报名时间");
			return result;
		}
		if(StringUtils.isEmpty(model.getTel())){
			result.setMessage("请填写电话号码");
			return result;
		}
		if(StringUtils.isEmpty(model.getMobileTel())){
			result.setMessage("请填写手机号码");
			return result;
		}
		if(model.getUser()==null||model.getUser().getUserId()==null){
			result.setMessage("请选择学习顾问");
			return result;
		}
		if(model.getMedia()==null||model.getMedia().getMediaId()==null){
			result.setMessage("请选择获知方式");
			return result;
		}
		if (model.getStudentId() == null) {// 新增
			if(potentialId!=null){
				Potential potentialModel=potentialDAO.load(potentialId);
				potentialModel.setIsStu(1);
				potentialDAO.update(potentialModel);
			}
			model.setInsertDate(new Date());
			model.setPassword(MD5Util.getMD5(GobelConstants.DEFAULT_USER_PWD));
			model.setArrearageMoney(0d);
			model.setAvailableMoney(0d);
			model.setBillCount(0);
			model.setConsumedMoney(0d);
			studentDAO.save(model);
		} else {
			Student oldModel = studentDAO.load(model.getStudentId());
			if (oldModel == null) {
				result.setMessage("该学员已不存在");
				return result;
			}
			oldModel.setAddress(model.getAddress());
			oldModel.setAppellation(model.getAppellation());
			oldModel.setBirthday(model.getBirthday());
			oldModel.setDiploma(model.getDiploma());
			oldModel.setEmail(model.getEmail());
			oldModel.setEnrollDate(model.getEnrollDate());
			oldModel.setIdcard(model.getIdcard());
			oldModel.setMedia(model.getMedia());
			oldModel.setMobileTel(model.getMobileTel());
			oldModel.setNextReplyDate(model.getNextReplyDate());
			oldModel.setNote(model.getNote());
			oldModel.setPostCode(model.getPostCode());
			oldModel.setQq(model.getQq());
			oldModel.setSchool(model.getSchool());
			oldModel.setSex(model.getSex());
			oldModel.setStudentName(model.getStudentName());
			oldModel.setStudentType(model.getStudentType());
			oldModel.setTel(model.getTel());
			oldModel.setTel1(model.getTel1());
			oldModel.setUser(model.getUser());
		}
		result.setIsSuccess(true);
		result.addData("studentId", model.getStudentId());
		return result;
	}

	public ServiceResult getStuById(Integer studentId) {
		ServiceResult result = new ServiceResult(false);
		Student student=studentDAO.load(studentId);
		String[] properties = { "studentId", "media.mediaName","media.mediaId","school.schoolName","school.schoolId","user.userName","user.userId", "studentName","appellation"
				,"sex", "studentType","birthday","password", "enrollDate","tel","mobileTel", "tel1","qq"
				,"email", "address","postCode","idcard", "diploma","nextReplyDate","note", "consumedMoney","insertDate"
				,"billCount", "arrearageMoney","availableMoney","creditExpiration"};
		String data = JSONUtil.toJson(student, properties);
		result.addData("student", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StudentService#login(java.lang.Integer, java.lang.String)
	 */
	@Override
	public Student login(Integer userCodeInt, String userPwd) {
		String[] propertyNames = {"studentId","password"};
		Object[] values = {userCodeInt,userPwd};
		return studentDAO.load(propertyNames, values);
	}

	public String queryCombobox() {
		List<Student> list = studentDAO.queryAll();
		String[] properties = { "studentId", "studentName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list, properties);
		return jsonString;
	}

	@Override
	public ServiceResult modifyPwd(Student model, String newUserPwd) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getStudentId()==null){
			result.setMessage("对不起你还没登陆系统");
			return result;
		}
		if(StringUtils.isEmpty(model.getPassword())){
			result.setMessage("请输入原密码");
			return result;
		}
		if(StringUtils.isEmpty(newUserPwd)){
			result.setMessage("请输入新密码");
			return result;
		}
		
		Student oldModel = studentDAO.load(model.getStudentId());
		String stuPwdMD5  = MD5Util.getMD5(model.getPassword());
		String oldUserPwd = oldModel.getPassword();
		if(!oldUserPwd.equals(stuPwdMD5)){
			result.setMessage("你输入的原密码不正确");
			return result;
		}
		String newUserPwdMD5 = MD5Util.getMD5(newUserPwd);
		oldModel.setPassword(newUserPwdMD5);
		result.setIsSuccess(true);
		return result;
	}

	@Override
	public String queryCombobox(String schoolCode) {
		List<Student> list = studentDAO.queryCombobox(schoolCode);
		String[] properties = { "studentId", "studentName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list, properties);
		return jsonString;
	}

	@Override
	public String queryCombobox(Student model) {
		List<Student> list = studentDAO.queryCombobox(model);
		String[] properties = { "studentId", "studentName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list, properties);
		return jsonString;
	}

}
