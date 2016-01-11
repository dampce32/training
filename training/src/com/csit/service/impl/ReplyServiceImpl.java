package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.PotentialDAO;
import com.csit.dao.ReplyDAO;
import com.csit.model.Potential;
import com.csit.model.Reply;
import com.csit.service.ReplyService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

/**
 * 
 * @Description:回访Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
@Service
public class ReplyServiceImpl extends BaseServiceImpl<Reply, Integer> implements ReplyService {

	@Resource
	private ReplyDAO replyDAO;
	@Resource
	private PotentialDAO potentialDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#delete(com.csit.model.Media)
	 */
	public ServiceResult delete(Reply model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getReplyId() == null) {
			result.setMessage("请选择要删除的媒体");
			return result;
		}
		Reply oldModel = replyDAO.load(model.getReplyId());
		if (oldModel == null) {
			result.setMessage("该媒体已不存在");
			return result;
		} else {
			replyDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(Reply model) {
		ServiceResult result = new ServiceResult(false);
		Long data = replyDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#query(com.csit.model.Media, java.lang.Integer, java.lang.Integer)
	 */
	public ServiceResult query(Reply model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<Reply> list = replyDAO.query(model, page, rows);

		String[] properties = { "replyId", "user.userName","potentialStuStatus.potentialStuStatusName","replyDate","content","potentialStuStatus.potentialStuStatusId","nextReplyDate" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult save(Reply model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写回访信息");
			return result;
		}
		if(model.getReplyDate()==null){
			result.setMessage("请填写回访时间");
			return result;
		}
		if(StringUtils.isEmpty(model.getContent())){
			result.setMessage("请填写回访内容");
			return result;
		}
		if(model.getPotentialStuStatus()==null&&model.getPotentialStuStatus().getPotentialStuStatusId()==null){
			result.setMessage("请选择回访结果");
			return result;
		}
		if (model.getReplyId() == null) {// 新增
			Potential potentialModel=potentialDAO.load(model.getPotential().getPotentialId());
			if(potentialModel==null){
				result.setMessage("对不起，该咨询单已不存在");
				return result;
			}
			potentialModel.setPotentialStuStatus(model.getPotentialStuStatus());
			potentialModel.setReCount(potentialModel.getReCount()+1);
			potentialModel.setLastReplyDate(model.getReplyDate());
			potentialModel.setlastReplyUser(model.getUser());
			replyDAO.save(model);
		} else {
			Integer maxReplyId=replyDAO.MaxId(model.getPotential().getPotentialId());
			if(maxReplyId==model.getReplyId().intValue()){
				Potential potentialModel=potentialDAO.load(model.getPotential().getPotentialId());
				potentialModel.setPotentialStuStatus(model.getPotentialStuStatus());
				potentialDAO.update(potentialModel);
			}
			Reply oldModel = replyDAO.load(model.getReplyId());
			if (oldModel == null) {
				result.setMessage("该回访信息已不存在");
				return result;
			}
			replyDAO.update(model);
		}
		result.setIsSuccess(true);
		result.addData("replyId", model.getReplyId());
		return result;
	}

}
