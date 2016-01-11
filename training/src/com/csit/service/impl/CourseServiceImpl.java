package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.CourseDAO;
import com.csit.dao.CourseTypeDAO;
import com.csit.model.Course;
import com.csit.model.CourseType;
import com.csit.service.CourseService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 课程Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, Integer> implements
		CourseService {
	@Resource
	private CourseDAO courseDAO;
	@Resource
	private CourseTypeDAO courseTypeDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseService#save(com.csit.model.Course)
	 */
	@Override
	public ServiceResult save(Course model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写课程信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getCourseName())){
			result.setMessage("请填写课程名称");
			return result;
		}
		if(model.getUnitPrice()==null){
			result.setMessage("请填写价格");
			return result;
		}
		if(StringUtils.isEmpty(model.getCourseName())){
			result.setMessage("请选择单位");
			return result;
		}
		if(model.getCourseType()==null||model.getCourseType().getCourseTypeId()==null){
			result.setMessage("请选择分类");
			return result;
		}
		CourseType courseType = courseTypeDAO.get(model.getCourseType().getCourseTypeId());
		if(courseType==null){
			result.setMessage("该课程类型不存在");
			return result;
		}
		if(model.getCourseId()==null){//新增
			courseDAO.save(model);
		}else{
			Course oldModel = courseDAO.load(model.getCourseId());
			if(oldModel==null){
				result.setMessage("该课程已不存在");
				return result;
			}
			oldModel.setCourseName(model.getCourseName());
			oldModel.setUnitPrice(model.getUnitPrice());
			oldModel.setCourseUnit(model.getCourseUnit());
			oldModel.setCourseType(courseType);
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("courseId", model.getCourseId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseService#delete(com.csit.model.Course)
	 */
	@Override
	public ServiceResult delete(Course model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getCourseId()==null){
			result.setMessage("请选择要删除的课程");
			return result;
		}
		Course oldModel = courseDAO.load(model.getCourseId());
		if(oldModel==null){
			result.setMessage("该课程类型已不存在");
			return result;
		}else{
			courseDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseService#query(com.csit.model.Course, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(Course model, Integer page, Integer rows, String courseIds) {
		ServiceResult result = new ServiceResult(false);
		Integer[] courseIdArr;
		if(StringUtils.isNotEmpty(courseIds)){
			String[] s = StringUtil.split(courseIds);
			courseIdArr=new Integer[s.length];
			for(int i=0;i<s.length;i++){
				courseIdArr[i]=Integer.parseInt(s[i]);
			}
		}else{
			courseIdArr=null;
		}
		List<Course> list = courseDAO.query(model,page,rows,courseIdArr);
		
		String[] properties = {"courseId","courseName","unitPrice","courseUnit","courseType.courseTypeId","courseType.courseTypeName","status"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseService#getTotalCount(com.csit.model.Course)
	 */
	@Override
	public ServiceResult getTotalCount(Course model, String courseIds) {
		ServiceResult result = new ServiceResult(false);
		Integer[] courseIdArr;
		if(StringUtils.isNotEmpty(courseIds)){
			String[] s = StringUtil.split(courseIds);
			courseIdArr=new Integer[s.length];
			for(int i=0;i<s.length;i++){
				courseIdArr[i]=Integer.parseInt(s[i]);
			}
		}else{
			courseIdArr=null;
		}
		Long data = courseDAO.getTotalCount(model,courseIdArr);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseService#queryCombobox()
	 */
	@Override
	public String queryCombobox() {
		List<Course> list = courseDAO.queryAll();
		String[] properties = {"courseId","courseName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseService#updateStatus(com.csit.model.Course)
	 */
	@Override
	public ServiceResult updateStatus(Course model) {
		ServiceResult result = new ServiceResult(false);


		Course oldModel = courseDAO.load(model.getCourseId());
		if(oldModel==null){
			result.setMessage("该课程已不存在");
			return result;
		}
		oldModel.setStatus(model.getStatus());
		
		result.setIsSuccess(true);
		result.addData("courseId", model.getCourseId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseService#queryIsTypeCombobox(java.lang.Integer)
	 */
	@Override
	public String queryIsTypeCombobox(Integer courseTypeId) {
		List<Course> list = courseDAO.queryIsTypeCombobox(courseTypeId);
		String[] properties = {"courseId","courseName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
}
