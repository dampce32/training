package com.csit.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.ReminderDAO;
import com.csit.dao.ReminderDetailDAO;
import com.csit.model.Reminder;
import com.csit.model.ReminderDetail;
import com.csit.model.ReminderItem;
import com.csit.service.ReminderService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;
/**
 * @Description:系统提醒Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Service
public class ReminderServiceImpl extends BaseServiceImpl<Reminder, Integer>
		implements ReminderService {
	@Resource
	private ReminderDAO reminderDAO;
	@Resource
	private ReminderDetailDAO reminderDetailDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderService#query(com.csit.model.Reminder, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(Reminder model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<Reminder> list = reminderDAO.query(model,page,rows);
		
		String[] properties = {"reminderId","title","status","array"};
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderService#getTotalCount(com.csit.model.Reminder)
	 */
	@Override
	public ServiceResult getTotalCount(Reminder model) {
		ServiceResult result = new ServiceResult(false);
		Long data = reminderDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderService#save(com.csit.model.Reminder, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceResult save(Reminder model, String delReminderDetailIds,
			String reminderDetailIds, String reminderItemIds) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写系统提醒信息");
			return result;
		}
		
		if(StringUtils.isEmpty(model.getTitle())){
			result.setMessage("请选择标题");
			return result;
		}
		if(model.getStatus()==null){
			result.setMessage("请选择状态");
			return result;
		}
		if(StringUtils.isEmpty(reminderItemIds)){
			result.setMessage("请选择系统提醒项");
			return result;
		}
		String[] delReminderDetailIdArray = StringUtil.split(delReminderDetailIds);
		String[] reminderDetailIdArray = StringUtil.split(reminderDetailIds);
		String[] reminderItemIdArray = StringUtil.split(reminderItemIds);
		if(reminderItemIdArray==null||reminderItemIdArray.length==0){
			result.setMessage("请选择系统提醒项");
			return result;
		}
		if(model.getReminderId()==null){//新增
			//取得最大排序
			Integer maxArray = reminderDAO.getMaxArray();
			model.setArray(maxArray+1);
			reminderDAO.save(model);
			for (int i = 0; i < reminderItemIdArray.length; i++) {
				String reminderItemId = reminderItemIdArray[i];
				
				ReminderItem reminderItem = new ReminderItem();
				reminderItem.setReminderItemId(Integer.parseInt(reminderItemId));
				
				ReminderDetail reminderDetail = new ReminderDetail();
				reminderDetail.setReminderItem(reminderItem);
				reminderDetail.setReminder(model);
				
				reminderDetailDAO.save(reminderDetail);
			}
		}else{
			//更新系统提醒
			Reminder oldReminder = reminderDAO.load(model.getReminderId());
			if(oldReminder==null){
				result.setMessage("要更新的系统提醒已不存在");
				return result;
			}
			
			oldReminder.setTitle(model.getTitle());
			oldReminder.setStatus(model.getStatus());
			
			//删除已删的系统提醒明细
			if(!"".equals(delReminderDetailIds)){
				for (String delReminderDetailId : delReminderDetailIdArray) {
					ReminderDetail oldModel = reminderDetailDAO.load(Integer.parseInt(delReminderDetailId));
					if(oldModel!=null){
						reminderDetailDAO.delete(oldModel);
					}
				}
			}
			//根据系统提醒明细Id更新或新增
			for (int i = 0 ;i<reminderDetailIdArray.length;i++) {
				String reminderDetailId = reminderDetailIdArray[i];
				String reminderItemId = reminderItemIdArray[i];
				if(StringUtils.isEmpty(reminderDetailId)){//新增
					ReminderItem reminderItem = new ReminderItem();
					reminderItem.setReminderItemId(Integer.parseInt(reminderItemId));
						
					ReminderDetail reminderDetail = new ReminderDetail();
					reminderDetail.setReminder(model);
					reminderDetail.setReminderItem(reminderItem);
					reminderDetailDAO.save(reminderDetail);
				}
			}
		}
		//返回值
		result.addData("reminderId", model.getReminderId());
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderService#init(com.csit.model.Reminder)
	 */
	@Override
	public ServiceResult init(Reminder model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getReminderId()==null){
			result.setMessage("请选择系统提醒单");
			return result;
		}
		Reminder reminder = reminderDAO.load(model.getReminderId());
		String[] propertiesReminder = {"reminderId","title","status"};
		String reminderData = JSONUtil.toJson(reminder,propertiesReminder);
		result.addData("reminderData",reminderData);
		
		List<ReminderDetail> reminderDetailList = reminderDetailDAO.queryByReminder(model);
		String[] propertiesDetail = {"reminderDetailId","reminderItem.reminderItemId","reminderItem.title","reminderItem.message","reminderItem.right.rightName"};
		String detailData = JSONUtil.toJson(reminderDetailList,propertiesDetail);
		result.addData("detailData", detailData);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderService#mulDelete(java.lang.String)
	 */
	@Override
	public ServiceResult mulDelete(String ids) {
		ServiceResult result = new ServiceResult(false);
		if(StringUtils.isEmpty(ids)){
			result.setMessage("请选择要删除的系统提醒");
			return result;
		}
		String[] idArray =StringUtil.split(ids);
		if(idArray.length==0){
			result.setMessage("请选择要删除的系统提醒");
			return result;
		}
		boolean haveDel = false;
		for (String id : idArray) {
			Reminder oldReminder = reminderDAO.load(Integer.parseInt(id));
			if(oldReminder!=null){
				reminderDAO.delete(oldReminder);
				haveDel = true;
			}
		}
		if(!haveDel){
			result.setMessage("没有可删除的系统提醒");
			return result;
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderService#mulUpdateStatus(java.lang.String)
	 */
	@Override
	public ServiceResult mulUpdateStatus(String ids,Reminder model) {
		ServiceResult result = new ServiceResult(false);
		if(StringUtils.isEmpty(ids)){
			result.setMessage("请选择要修改审核状态的系统提醒");
			return result;
		}
		String[] idArray =StringUtil.split(ids);
		if(idArray.length==0){
			result.setMessage("请选择要修改状态的系统提醒");
			return result;
		}
		if(model==null||model.getStatus()==null){
			result.setMessage("请选择要修改状态");
			return result;
		}
		boolean haveUpdateShzt = false;
		for (String id : idArray) {
			Reminder oldReminder = reminderDAO.load(Integer.parseInt(id));
			if(oldReminder!=null&&oldReminder.getStatus().intValue()!=model.getStatus().intValue()){
				oldReminder.setStatus(model.getStatus());
				haveUpdateShzt = true;
			}
		}
		if(!haveUpdateShzt){
			result.setMessage("没有可修改状态的系统提醒");
			return result;
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderService#getCurrentUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceResult getCurrentUser(String userId,String schoolId,String schoolCode) {
		ServiceResult result = new ServiceResult(false);
		/*
		 * 取得所有的系统提醒
		 * 1.如果系统提醒下的系统提醒项，在该用户的权限中则显示系统提醒项，如果所有的系统提醒项，都不在该用户的权限，则不显示该系统提醒项
		 */
		List<Map<String,Object>> reminderList = reminderDAO.getCurrentUserReminder(Integer.parseInt(userId));
		if(reminderList!=null&&reminderList.size()!=0){
			StringBuilder sb = new StringBuilder();
			String preReminderId = "";
			int addReminderItemCount = 0;//统计相同系统提醒下添加的系统提醒明细
			for (Map<String, Object> map : reminderList) {
				String title = map.get("title").toString();
				String reminderId = map.get("reminderId").toString();
				String message = map.get("message").toString();
				String countSql = map.get("countSql").toString();
				String rightName = map.get("rightName").toString();
				String rightUrl = map.get("rightUrl").toString();
				String rightId = map.get("rightId").toString();
				
				if(!preReminderId.equals(reminderId)){//新的系统提醒
					if(!"".equals(preReminderId)&&addReminderItemCount!=0){//新的系统提醒
						sb.append("		</td>");
						sb.append("</tr>");
					}
					addReminderItemCount=0;
					preReminderId = reminderId;
				}
				//系统提醒明细
				
				//处理数量Sql
				Pattern p = Pattern.compile("\\{@(\\w+)\\}", Pattern.CASE_INSENSITIVE);// 正则表达式，后面的参数指定忽略大小写
				Matcher m = p.matcher(countSql);// 匹配的字符串
				StringBuffer buf = new StringBuffer();
				while (m.find()){
					String param = m.group(1).toString();
					if("userId".equals(param)){
						m.appendReplacement(buf,userId);
					}else if("schoolId".equals(param)){
						m.appendReplacement(buf,schoolId);
					}else if("schoolCode".equals(param)){
						m.appendReplacement(buf,schoolCode);
					}
					
				}
				m.appendTail(buf);
				
				Long count = reminderDetailDAO.count(buf.toString());
				//如果统计数量是0，则不需要添加该系统提醒项
				if(count.longValue()!=0){
					p = Pattern.compile("\\{\\d\\}", Pattern.CASE_INSENSITIVE);// 正则表达式，后面的参数指定忽略大小写
					m = p.matcher(message);// 匹配的字符串
					buf = new StringBuffer();
					while (m.find()){
						m.appendReplacement(buf, "<a href=\"#\" rightId = \""+rightId+"\" navigateUrl=\""+rightUrl+"\" navigateTabName=\""+rightName+"\">&nbsp;&nbsp;"+count.toString()+"&nbsp;&nbsp;</a>");
					}
					m.appendTail(buf);
					if(addReminderItemCount==0){
						sb.append("<tr>");
						sb.append("		<td class=\"addReminder\" style=\"padding: 10px 0px 0px 20px;font-size:14px\"><span style=\"font-size:30px\">"+title+"</span><br><br>");
						sb.append("<span style=\"color:red\">&hearts;</span>&nbsp;"+buf.toString());
					}else{
						sb.append("<br><br><span style=\"color:red\">&hearts;</span>&nbsp;"+buf.toString());
					}
					addReminderItemCount++;
				}
			}
			if(addReminderItemCount!=0){
				sb.append("		</td>");
				sb.append("</tr>");
			}
			result.addData("appendHtml", sb.toString());
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ReminderService#updateArray(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult updateArray(Integer reminderId,
			Integer updateReminderId) {
		ServiceResult result = new ServiceResult(false);
		if(reminderId==null||updateReminderId==null){
			result.setMessage("请选择要改变排序的系统提醒");
			return result;
		}
		Reminder oldModel = reminderDAO.load(reminderId);
		Reminder updateReminder = reminderDAO.load(updateReminderId);
		Integer oldArray = oldModel.getArray();
		Integer upateArray = updateReminder.getArray();
		oldModel.setArray(upateArray);
		updateReminder.setArray(oldArray);
		
		result.setIsSuccess(true);
		return result;
	}

}
