package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.PeriodTimeDAO;
import com.csit.model.PeriodTime;
import com.csit.service.PeriodTimeService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 时间段Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author yk
 * @vesion 1.0
 */
@Service
public class PeriodTimeServiceImpl extends BaseServiceImpl<PeriodTime, Integer>
		implements PeriodTimeService {
	@Resource
	private PeriodTimeDAO periodTimeDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PeriodTimeService#save(com.csit.model.PeriodTime)
	 */
	@Override
	public ServiceResult save(PeriodTime model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写时间段信息");
			return result;
		}
		
		if(StringUtils.isEmpty(model.getStartTime())){
			result.setMessage("请填写开始时间");
			return result;
		}
		
		if(StringUtils.isEmpty(model.getEndTime())){
			result.setMessage("请填写结束时间");
			return result;
		}
		
		if(model.getGroupType()==null){
			result.setMessage("请选择分组");
			return result;
		}
		
		if(model.getPeriodTimeId()==null){//新增
			periodTimeDAO.save(model);
		}else{
			PeriodTime oldModel = periodTimeDAO.load(model.getPeriodTimeId());
			if(oldModel==null){
				result.setMessage("该时间段已不存在");
				return result;
			}
			oldModel.setStartTime(model.getStartTime());
			oldModel.setEndTime(model.getEndTime());
			oldModel.setGroupType(model.getGroupType());
		}
		result.setIsSuccess(true);
		result.addData("ceriodTimeId", model.getPeriodTimeId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PeriodTimeService#delete(com.csit.model.PeriodTime)
	 */
	@Override
	public ServiceResult delete(PeriodTime model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getPeriodTimeId()==null){
			result.setMessage("请选择要删除的时间段");
			return result;
		}
		PeriodTime oldModel = periodTimeDAO.load(model.getPeriodTimeId());
		if(oldModel==null){
			result.setMessage("该时间段已不存在");
			return result;
		}else{
			periodTimeDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PeriodTimeService#query(com.csit.model.PeriodTime, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(PeriodTime model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<PeriodTime> list = periodTimeDAO.query(model,page,rows);
		
		String[] properties = {"periodTimeId","startTime","endTime","groupType"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PeriodTimeService#getTotalCount(com.csit.model.PeriodTime)
	 */
	@Override
	public ServiceResult getTotalCount(PeriodTime model) {
		ServiceResult result = new ServiceResult(false);
		Long data = periodTimeDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PeriodTimeService#queryInGroupTypeCombobox(java.lang.Integer)
	 */
	@Override
	public String queryInGroupTypeCombobox(Integer groupType) {
		List<PeriodTime> list = periodTimeDAO.queryInGroupTypeCombobox(groupType);
		for(PeriodTime periodTime:list){
			periodTime.setTime(periodTime.getStartTime()+"-"+periodTime.getEndTime());
		}
		String[] properties = {"periodTimeId","time"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PeriodTimeService#queryCombobox()
	 */
	@Override
	public String queryCombobox() {
		List<PeriodTime> list = periodTimeDAO.queryAll();
		for(PeriodTime periodTime:list){
			periodTime.setTime(periodTime.getStartTime()+"-"+periodTime.getEndTime());
		}
		String[] properties = {"periodTimeId","time"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}

}
