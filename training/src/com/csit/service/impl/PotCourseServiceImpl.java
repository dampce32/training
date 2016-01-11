package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.PotCourseDAO;
import com.csit.model.PotCourse;
import com.csit.service.PotCourseService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

/**
 * 
 * @Description:媒体Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
@Service
public class PotCourseServiceImpl extends BaseServiceImpl<PotCourse, Integer> implements PotCourseService {

	@Resource
	private PotCourseDAO potCourseDao;

	public ServiceResult delete(PotCourse model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getPotCourseId() == null) {
			result.setMessage("请选择要删除的咨询课程");
			return result;
		}
		PotCourse oldModel = potCourseDao.load(model.getPotCourseId());
		if (oldModel == null) {
			result.setMessage("该咨询课程已不存在");
			return result;
		} else {
			potCourseDao.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(PotCourse model) {
		ServiceResult result = new ServiceResult(false);
		Long data = potCourseDao.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult query(PotCourse model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<PotCourse> list = potCourseDao.query(model, page, rows);

		String[] properties = { "potCourseId", "courseName","status" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public String queryCombobox() {
		List<PotCourse> list = potCourseDao.query("status", 1);
		String[] properties = {"potCourseId","courseName","status"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}

	public ServiceResult save(PotCourse model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写咨询课程信息");
			return result;
		}
		if (StringUtils.isEmpty(model.getCourseName())) {
			result.setMessage("请填写咨询课程名称");
			return result;
		}
		if (model.getPotCourseId() == null) {// 新增
			potCourseDao.save(model);
		} else {
			PotCourse oldModel = potCourseDao.load(model.getPotCourseId());
			if (oldModel == null) {
				result.setMessage("该咨询课程已不存在");
				return result;
			}
			oldModel.setCourseName(model.getCourseName());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("potCourseId", model.getPotCourseId());
		return result;
	}

	@Override
	public ServiceResult updateStatus(Integer potCourseId, Integer state) {
		ServiceResult result = new ServiceResult(false);
		if (potCourseId == null) {
			result.setMessage("请选择要修改的咨询课程");
			return result;
		}
		if (state == null) {
			result.setMessage("请选择要修改的状态");
			return result;
		}
		PotCourse oldModel=potCourseDao.load(potCourseId);
		if(oldModel==null){
			result.setMessage("对不起，你要修改的咨询课程信息已不存在");
			return result;
		}
		if(oldModel.getStatus().intValue()==state.intValue()){
			result.setMessage("没有可修改的咨询课程");
			return result;
		}
		else {
			oldModel.setStatus(state);
			potCourseDao.update(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

}
