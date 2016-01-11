package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.MediaDAO;
import com.csit.model.Media;
import com.csit.service.MediaService;
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
public class MediaServiceImpl extends BaseServiceImpl<Media, Integer> implements MediaService {

	@Resource
	private MediaDAO mediaDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#delete(com.csit.model.Media)
	 */
	public ServiceResult delete(Media model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getMediaId() == null) {
			result.setMessage("请选择要删除的媒体");
			return result;
		}
		Media oldModel = mediaDAO.load(model.getMediaId());
		if (oldModel == null) {
			result.setMessage("该媒体已不存在");
			return result;
		} else {
			mediaDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(Media model) {
		ServiceResult result = new ServiceResult(false);
		Long data = mediaDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.MediaService#query(com.csit.model.Media, java.lang.Integer, java.lang.Integer)
	 */
	public ServiceResult query(Media model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<Media> list = mediaDAO.query(model, page, rows);

		String[] properties = { "mediaId", "mediaName","status" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public String queryCombobox() {
		List<Media> list = mediaDAO.query("status", 1);
		String[] properties = {"mediaId","mediaName","status"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}

	public ServiceResult save(Media model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写媒体信息");
			return result;
		}
		if (StringUtils.isEmpty(model.getMediaName())) {
			result.setMessage("请填写媒体名称");
			return result;
		}
		if (model.getMediaId() == null) {// 新增
			mediaDAO.save(model);
		} else {
			Media oldModel = mediaDAO.load(model.getMediaId());
			if (oldModel == null) {
				result.setMessage("该媒体已不存在");
				return result;
			}
			oldModel.setMediaName(model.getMediaName());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("mediaId", model.getMediaId());
		return result;
	}

	@Override
	public ServiceResult updateStatus(Integer mediaId,Integer state) {
		ServiceResult result = new ServiceResult(false);
		if (mediaId == null) {
			result.setMessage("请选择要修改的媒体信息");
			return result;
		}
		if (state == null) {
			result.setMessage("请选择要修改的状态");
			return result;
		}
		Media oldModel=mediaDAO.load(mediaId);
		if(oldModel==null){
			result.setMessage("对不起，你要修改的媒体信息已不存在");
			return result;
		}
		if(oldModel.getStatus().intValue()==state.intValue()){
			result.setMessage("没有可修改的媒体信息");
			return result;
		}
		else {
			oldModel.setStatus(state);
			mediaDAO.update(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

}
