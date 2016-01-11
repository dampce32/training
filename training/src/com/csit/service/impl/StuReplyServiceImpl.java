package com.csit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.StuReplyDAO;
import com.csit.model.StuReply;
import com.csit.service.StuReplyService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

/**
 * 
 * @Description:关怀Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
@Service
public class StuReplyServiceImpl extends BaseServiceImpl<StuReply, Integer> implements StuReplyService {

	@Resource
	private StuReplyDAO stuReplyDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#delete(com.csit.model.Media)
	 */
	public ServiceResult delete(StuReply model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getReplyId() == null) {
			result.setMessage("请选择要删除的关怀");
			return result;
		}
		StuReply oldModel = stuReplyDAO.load(model.getReplyId());
		if (oldModel == null) {
			result.setMessage("该关怀已不存在");
			return result;
		} else {
			stuReplyDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(StuReply model) {
		ServiceResult result = new ServiceResult(false);
		Long data = stuReplyDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#query(com.csit.model.Media, java.lang.Integer, java.lang.Integer)
	 */
	public ServiceResult query(StuReply model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<StuReply> list = stuReplyDAO.query(model, page, rows);

		String[] properties = { "replyId", "user.userName","replyDate","content","nextReplyDate","student.studentId","insertTime" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult save(StuReply model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写关怀信息");
			return result;
		}
		if(model.getReplyDate()==null){
			result.setMessage("请填写关怀时间");
			return result;
		}
		if(StringUtils.isEmpty(model.getContent())){
			result.setMessage("请填写关怀内容");
			return result;
		}
		if (model.getReplyId() == null) {// 新增
			model.setInsertTime(new Date());
			stuReplyDAO.save(model);
		} else {
			StuReply oldModel = stuReplyDAO.load(model.getReplyId());
			if (oldModel == null) {
				result.setMessage("该关怀信息已不存在");
				return result;
			}
			stuReplyDAO.update(model);
		}
		result.setIsSuccess(true);
		result.addData("replyId", model.getReplyId());
		return result;
	}

}
