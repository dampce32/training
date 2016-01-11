package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.ClassroomDAO;
import com.csit.dao.SchoolDAO;
import com.csit.model.Classroom;
import com.csit.model.School;
import com.csit.service.ClassroomService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 教室Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author yk
 * @vesion 1.0
 */
@Service
public class ClassroomServiceImpl extends BaseServiceImpl<Classroom, Integer>
		implements ClassroomService {
	@Resource
	private ClassroomDAO classroomDAO;
	@Resource
	private SchoolDAO schoolDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassroomService#save(com.csit.model.Classroom)
	 */
	@Override
	public ServiceResult save(Classroom model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写教室信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getClassroomName())){
			result.setMessage("请填写教室名称");
			return result;
		}
		if(model.getSeating()==null){
			result.setMessage("请填写容纳人数");
			return result;
		}
		if(model.getSchool()==null||model.getSchool().getSchoolId()==null){
			result.setMessage("请填写教室名称");
			return result;
		}
		School school = schoolDAO.get(model.getSchool().getSchoolId());
		if(school==null){
			result.setMessage("该校区不存在");
			return result;
		}
		if(model.getClassroomId()==null){//新增
			classroomDAO.save(model);
		}else{
			Classroom oldModel = classroomDAO.load(model.getClassroomId());
			if(oldModel==null){
				result.setMessage("该教室已不存在");
				return result;
			}
			oldModel.setClassroomName(model.getClassroomName());
			oldModel.setSeating(model.getSeating());
			oldModel.setSchool(model.getSchool());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("classroomId", model.getClassroomId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassroomService#delete(com.csit.model.Classroom)
	 */
	@Override
	public ServiceResult delete(Classroom model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getClassroomId()==null){
			result.setMessage("请选择要删除的教室");
			return result;
		}
		Classroom oldModel = classroomDAO.load(model.getClassroomId());
		if(oldModel==null){
			result.setMessage("该教室已不存在");
			return result;
		}else{
			classroomDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassroomService#query(com.csit.model.Classroom, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(Classroom model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<Classroom> list = classroomDAO.query(model,page,rows);
		
		String[] properties = {"classroomId","classroomName","seating","status","school.schoolId","school.schoolName"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassroomService#getTotalCount(com.csit.model.Classroom)
	 */
	@Override
	public ServiceResult getTotalCount(Classroom model) {
		ServiceResult result = new ServiceResult(false);
		Long data = classroomDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassroomService#queryCombobox()
	 */
	@Override
	public String queryCombobox() {
		List<Classroom> list = classroomDAO.queryAll();
		String[] properties = {"classroomId","classroomName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassroomService#updateStatus(com.csit.model.Classroom)
	 */
	@Override
	public ServiceResult updateStatus(Classroom model) {
		ServiceResult result = new ServiceResult(false);


		Classroom oldModel = classroomDAO.load(model.getClassroomId());
		if(oldModel==null){
			result.setMessage("该教室已不存在");
			return result;
		}
		oldModel.setStatus(model.getStatus());
		
		result.setIsSuccess(true);
		result.addData("classroomId", model.getClassroomId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassroomService#queryInSchoolCombobox(java.lang.Integer)
	 */
	@Override
	public String queryInSchoolCombobox(Integer schoolId) {
		List<Classroom> list = classroomDAO.queryInSchoolCombobox(schoolId);
		String[] properties = {"classroomId","classroomName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
}
