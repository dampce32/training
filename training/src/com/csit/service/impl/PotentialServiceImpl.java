package com.csit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.PotentialDAO;
import com.csit.model.Potential;
import com.csit.service.PotentialService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

/**
 * 
 * @Description:咨询信息Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author jcf
 * @vesion 1.0
 */
@Service
public class PotentialServiceImpl extends BaseServiceImpl<Potential, Integer> implements PotentialService {

	@Resource
	private PotentialDAO potentialDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PotentialService#delete(com.csit.model.Potential)
	 */
	public ServiceResult delete(Potential model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getPotentialId() == null) {
			result.setMessage("请选择要删除的咨询信息");
			return result;
		}
		Potential oldModel = potentialDAO.load(model.getPotentialId());
		if (oldModel == null) {
			result.setMessage("该咨询信息已不存在");
			return result;
		} else {
			potentialDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PotentialService#getTotalCount(com.csit.model.Potential)
	 */
	public ServiceResult getTotalCount(Potential model) {
		ServiceResult result = new ServiceResult(false);
		Long data = potentialDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PotentialService#query(com.csit.model.Potential, java.lang.Integer, java.lang.Integer)
	 */
	public ServiceResult query(Potential model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<Potential> list = potentialDAO.query(model, page, rows);

		String[] properties = { "potentialId", "potCourse.courseName", "potCourse.potCourseId","media.mediaName","media.mediaId","school.schoolId","school.schoolName","user.userName","user.userId","reCount","potentialStuStatus.potentialStuStatusName","potentialStuStatus.potentialStuStatusId","potentialName","potentialDate","sex","appellation","tel","tel1","mobileTel","qq","email","timeRule","nextReplyDate","note","insertDate","isStu" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult save(Potential model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写咨询信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getPotentialName())){
			result.setMessage("请填写咨询人姓名");
			return result;
		}
		if(model.getSchool()==null||model.getSchool().getSchoolId()==null){
			result.setMessage("请选择校区");
			return result;
		}
		if(model.getPotentialDate()==null){
			result.setMessage("请填写咨询日期");
			return result;
		}
		if(model.getPotCourse()==null||model.getPotCourse().getPotCourseId()==null){
			result.setMessage("请选择咨询课程");
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
		if(model.getMedia()==null||model.getMedia().getMediaId()==null){
			result.setMessage("请选择获知方式");
			return result;
		}
		if(model.getPotentialStuStatus()==null||model.getPotentialStuStatus().getPotentialStuStatusId()==null){
			result.setMessage("请选择状态");
			return result;
		}
		if(model.getUser()==null||model.getUser().getUserId()==null){
			result.setMessage("请选择咨询顾问");
			return result;
		}
		if (model.getPotentialId() == null) {// 新增
			model.setInsertDate(new Date());
			model.setReCount(0);
			model.setIsStu(0);
			potentialDAO.save(model);
		} else {
			Potential oldModel = potentialDAO.load(model.getPotentialId());
			if (oldModel == null) {
				result.setMessage("该媒体已不存在");
				return result;
			}
			oldModel.setPotentialName(model.getPotentialName());
			oldModel.setPotCourse(model.getPotCourse());
			oldModel.setMedia(model.getMedia());
			oldModel.setEmail(model.getEmail());
			oldModel.setAppellation(model.getAppellation());
			oldModel.setMobileTel(model.getMobileTel());
			oldModel.setNextReplyDate(model.getNextReplyDate());
			oldModel.setNote(model.getNote());
			oldModel.setPotentialDate(model.getPotentialDate());
			oldModel.setPotentialStuStatus(model.getPotentialStuStatus());
			oldModel.setQq(model.getQq());
			oldModel.setTel(model.getTel());
			oldModel.setTel1(model.getTel1());
			oldModel.setSchool(model.getSchool());
			oldModel.setSex(model.getSex());
			oldModel.setUser(model.getUser());
			oldModel.setTimeRule(model.getTimeRule());
		}
		result.setIsSuccess(true);
		result.addData("potentialId", model.getPotentialId());
		return result;
	}

}
