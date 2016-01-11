package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.CourseTypeDAO;
import com.csit.model.CourseType;
import com.csit.service.CourseTypeService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;
/**
 * @Description:课程类型Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author lys
 */
@Service
public class CourseTypeServiceImpl extends BaseServiceImpl<CourseType, Integer>
		implements CourseTypeService {
	@Resource
	private CourseTypeDAO courseTypeDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseTypeService#save(com.csit.model.CourseType)
	 */
	@Override
	public ServiceResult save(CourseType model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写课程类型信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getCourseTypeName())){
			result.setMessage("请填写课程类型");
			return result;
		}
		if(model.getCourseTypeId()==null){//新增
			courseTypeDAO.save(model);
		}else{
			CourseType oldModel = courseTypeDAO.load(model.getCourseTypeId());
			if(oldModel==null){
				result.setMessage("该课程类型已不存在");
				return result;
			}
			oldModel.setCourseTypeName(model.getCourseTypeName());
		}
		result.setIsSuccess(true);
		result.addData("courseTypeId", model.getCourseTypeId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseTypeService#delete(com.csit.model.CourseType)
	 */
	@Override
	public ServiceResult delete(CourseType model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getCourseTypeId()==null){
			result.setMessage("请选择要删除的课程类型");
			return result;
		}
		CourseType oldModel = courseTypeDAO.load(model.getCourseTypeId());
		if(oldModel==null){
			result.setMessage("该课程类型已不存在");
			return result;
		}else{
			courseTypeDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseTypeService#query(com.csit.model.CourseType, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(CourseType model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<CourseType> list = courseTypeDAO.query(model,page,rows);
		
		String[] properties = {"courseTypeId","courseTypeName"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseTypeService#getTotalCount(com.csit.model.CourseType)
	 */
	@Override
	public ServiceResult getTotalCount(CourseType model) {
		ServiceResult result = new ServiceResult(false);
		Long data = courseTypeDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.CourseTypeService#queryCombobox()
	 */
	@Override
	public String queryCombobox() {
		List<CourseType> list = courseTypeDAO.queryAll();
		String[] properties = {"courseTypeId","courseTypeName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}

}
