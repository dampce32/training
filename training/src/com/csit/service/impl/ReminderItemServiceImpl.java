package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.ReminderItemDAO;
import com.csit.model.ReminderItem;
import com.csit.service.ReminderItemService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;
/**
 * @Description:系统提醒项Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Service
public class ReminderItemServiceImpl extends
		BaseServiceImpl<ReminderItem, Integer> implements ReminderItemService {
	@Resource
	private ReminderItemDAO reminderItemDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderItemService#save(com.csit.model.ReminderItem)
	 */
	@Override
	public ServiceResult save(ReminderItem model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写系统提醒项信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getTitle())){
			result.setMessage("请填写标题");
			return result;
		}
		if(StringUtils.isEmpty(model.getMessage())){
			result.setMessage("请填写提示信息");
			return result;
		}
		if(model.getRight()==null||(model.getRight()!=null&&model.getRight().getRightId()==null)){
			model.setRight(null);
		}
		
		if(model.getReminderItemId()==null){//新增
			reminderItemDAO.save(model);
		}else{
			ReminderItem oldModel = reminderItemDAO.load(model.getReminderItemId());
			if(oldModel==null){
				result.setMessage("该系统提醒项已不存在");
				return result;
			}
			oldModel.setTitle(model.getTitle());
			oldModel.setMessage(model.getMessage());
			oldModel.setCountSql(model.getCountSql());
			oldModel.setRight(model.getRight());
		}
		result.setIsSuccess(true);
		result.addData("reminderItemId", model.getReminderItemId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderItemService#delete(com.csit.model.ReminderItem)
	 */
	@Override
	public ServiceResult delete(ReminderItem model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getReminderItemId()==null){
			result.setMessage("请选择要删除的系统提醒项");
			return result;
		}
		ReminderItem oldModel = reminderItemDAO.load(model.getReminderItemId());
		if(oldModel==null){
			result.setMessage("该系统提醒项已不存在");
			return result;
		}else{
			reminderItemDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderItemService#query(com.csit.model.ReminderItem, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(ReminderItem model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<ReminderItem> list = reminderItemDAO.query(model,page,rows);
		
		String[] properties = {"reminderItemId","title","message","countSql","right.rightId","right.rightName","right.rightUrl"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderItemService#getTotalCount(com.csit.model.ReminderItem)
	 */
	@Override
	public ServiceResult getTotalCount(ReminderItem model) {
		ServiceResult result = new ServiceResult(false);
		Long data = reminderItemDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderItemService#selectQuery(java.lang.String, com.csit.model.ReminderItem, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public String selectQuery(String ids, ReminderItem model, Integer page,
			Integer rows) {
		Integer[] idArray = null;
		if(StringUtils.isNotEmpty(ids)){
			idArray = StringUtil.splitToInteger(ids);
		}
		
		List<ReminderItem> list = reminderItemDAO.selectQuery(idArray,model,page,rows);
		Long total = reminderItemDAO.getTotalCountSelectQuery(idArray,model);
		String[] properties = {"reminderItemId","title","message","right.rightName"};
		
		return JSONUtil.toJson(list,properties,total);
	}

}
