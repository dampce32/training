package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.LeaveDao;
import com.csit.dao.StudentDAO;
import com.csit.dao.UserDAO;
import com.csit.model.Leave;
import com.csit.model.Student;
import com.csit.model.User;
import com.csit.service.LeaveService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

@Service
public class LeaveServiceImpl implements LeaveService {
	@Resource
	private LeaveDao leaveDao;
	@Resource
	private UserDAO userDao;
	@Resource
	private StudentDAO studentDao;
	public ServiceResult save(Leave model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写请假信息");
			return result;
		}
		if(model.getStudent()==null){
			result.setMessage("请选择请假学员");
			return result;
		}
		Student student=studentDao.get(model.getStudent().getStudentId());
		if( student==null){
			result.setMessage("请选择已有的学员");
			return result;
		}
		if(model.getDate()==null){
			result.setMessage("请选择办理日期");
			return result;
		}
		if(model.getBeginDate()==null){
			result.setMessage("请选择开始时间");
			return result;
		}
		if (model.getEndDate() == null) {
			result.setMessage("请填写结束时间");
			return result;
		}
		if(model.getUser()==null){
			result.setMessage("请选择经办人");
			return result;
		}
		User user=userDao.get(model.getUser().getUserId());
		if( user==null){
			result.setMessage("请选择已有的经办人");
			return result;
		}
		if(model.getLeaveId()==null){//新增
			leaveDao.save(model);
		}else{
			Leave oldModel = leaveDao.load(model.getLeaveId());
			if(oldModel==null){
				result.setMessage("该请假已不存在");
				return result;
			}
			oldModel.setUser(model.getUser());
			oldModel.setStudent(model.getStudent());
			oldModel.setDate(model.getDate());
			oldModel.setBeginDate(model.getBeginDate());
			oldModel.setEndDate(model.getEndDate());
			oldModel.setNote(model.getNote());
		}
		result.setIsSuccess(true);
		result.addData("leaveId", model.getLeaveId());
		return result;
	}
	public ServiceResult query(Leave model,String schoolCode, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		
		List<Leave> list = leaveDao.query(model,schoolCode,page,rows);
		
		String[] properties = {"leaveId","user.userId","user.userName","student.studentName",
				"student.studentId","date","note","beginDate","endDate"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	public ServiceResult getTotalCount(Leave model,String schoolCode) {
		ServiceResult result = new ServiceResult(false);
		Long data = leaveDao.getTotalCount(model,schoolCode);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(Leave model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getLeaveId()==null){
			result.setMessage("请选择要删除的请假单");
			return result;
		}
		Leave oldModel = leaveDao.load(model.getLeaveId());
		if(oldModel==null){
			result.setMessage("该请假已不存在");
			return result;
		}else{
			leaveDao.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	@Override
	public ServiceResult getTotalCount(Leave model) {
		ServiceResult result = new ServiceResult(false);
		Long data = leaveDao.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
		
	}
	@Override
	public ServiceResult query(Leave model, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		
		List<Leave> list = leaveDao.query(model,page,rows);
		
		String[] properties = {"leaveId","user.userId","user.userName","student.studentName",
				"student.studentId","date","note","beginDate","endDate"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
}
