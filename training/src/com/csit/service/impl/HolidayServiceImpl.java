package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.HolidayDAO;
import com.csit.model.Holiday;
import com.csit.service.HolidayService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 假期表Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Service
public class HolidayServiceImpl extends BaseServiceImpl<Holiday, Integer>
		implements HolidayService {
	@Resource
	private HolidayDAO holidayDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.HolidayService#save(com.csit.model.Holiday)
	 */
	@Override
	public ServiceResult save(Holiday model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写假期信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getHolidayName())){
			result.setMessage("请填写假期名称");
			return result;
		}
		if(model.getStartDate()==null){
			result.setMessage("请选择开始时间");
			return result;
		}
		if(model.getEndDate()==null){
			result.setMessage("请选择结束时间");
			return result;
		}
		if(model.getHolidayId()==null){//新增
			holidayDAO.save(model);
		}else{
			Holiday oldModel = holidayDAO.load(model.getHolidayId());
			if(oldModel==null){
				result.setMessage("该假期已不存在");
				return result;
			}
			oldModel.setHolidayName(model.getHolidayName());
			oldModel.setStartDate(model.getStartDate());
			oldModel.setEndDate(model.getEndDate());
		}
		result.setIsSuccess(true);
		result.addData("holidayId", model.getHolidayId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.HolidayService#delete(com.csit.model.Holiday)
	 */
	@Override
	public ServiceResult delete(Holiday model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getHolidayId()==null){
			result.setMessage("请选择要删除的假期");
			return result;
		}
		Holiday oldModel = holidayDAO.load(model.getHolidayId());
		if(oldModel==null){
			result.setMessage("该假期已不存在");
			return result;
		}else{
			holidayDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.HolidayService#query(com.csit.model.Holiday, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(Holiday model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<Holiday> list = holidayDAO.query(model,page,rows);
		
		String[] properties = {"holidayId","holidayName","startDate","endDate"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.HolidayService#getTotalCount(com.csit.model.Holiday)
	 */
	@Override
	public ServiceResult getTotalCount(Holiday model) {
		ServiceResult result = new ServiceResult(false);
		Long data = holidayDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.HolidayService#queryCombobox()
	 */
	@Override
	public String queryCombobox() {
		List<Holiday> list = holidayDAO.queryAll();
		String[] properties = {"holidayId","holidayName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}

}
