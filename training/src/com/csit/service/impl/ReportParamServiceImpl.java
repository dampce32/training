package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.ReportParamDAO;
import com.csit.model.ReportParam;
import com.csit.service.ReportParamService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;
/**
 * @Description:报表参数Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-31
 * @author lys
 * @vesion 1.0
 */
@Service
public class ReportParamServiceImpl extends
		BaseServiceImpl<ReportParam, String> implements ReportParamService {
	
	@Resource
	private ReportParamDAO reportParamDAO;
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportParamService#save(org.linys.model.ReportParam)
	 */
	@Override
	public ServiceResult save(ReportParam model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写报表参数信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getParamCode())){
			result.setMessage("请填写参数编号");
			return result;
		}
		
		if(StringUtils.isEmpty(model.getReportParamId())){//新增
			ReportParam oldModel = reportParamDAO.load("paramCode",model.getParamCode());
			if(oldModel!=null){
				result.setMessage("该参数编号已存在，请重新输入");
				return result;
			}
			reportParamDAO.save(model);
		}else{
			ReportParam oldModel = reportParamDAO.load(model.getReportParamId());
			if(oldModel==null){
				result.setMessage("该报表参数已不存在");
				return result;
			}
			
			if(!oldModel.getParamCode().equals(model.getParamCode())){
				ReportParam oldReportParam = reportParamDAO.load("paramCode",model.getParamCode());
				if(oldReportParam!=null){
					result.setMessage("该参数编号已存在，请重新输入");
					return result;
				}
			}
			
			oldModel.setParamName(model.getParamName());
			oldModel.setParamCode(model.getParamCode());
			reportParamDAO.update(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportParamService#delete(org.linys.model.ReportParam)
	 */
	@Override
	public ServiceResult delete(ReportParam model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||StringUtils.isEmpty(model.getReportParamId())){
			result.setMessage("请选择要删除的报表参数");
			return result;
		}
		ReportParam oldModel = reportParamDAO.load(model.getReportParamId());
		if(oldModel==null){
			result.setMessage("该报表参数已不存在");
			return result;
		}else{
			reportParamDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportParamService#query(org.linys.model.ReportParam, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(ReportParam model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<ReportParam> list = reportParamDAO.query(model,page,rows);
		
		String[] properties = {"reportParamId","paramName","paramCode"};
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportParamService#getTotalCount(org.linys.model.ReportParam)
	 */
	@Override
	public ServiceResult getTotalCount(ReportParam model) {
		ServiceResult result = new ServiceResult(false);
		Long data = reportParamDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportParamService#select(org.linys.model.ReportParam, java.lang.String)
	 */
	@Override
	public ServiceResult select(ReportParam model, String ids, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		String[] idArray = {""};
		if(StringUtils.isNotEmpty(ids)){
			idArray = StringUtil.split(ids);
		}
		
		List<ReportParam> list = reportParamDAO.select(model,idArray,page,rows);
		Long total = reportParamDAO.getTotalCountSelect(model,idArray);
		
		String[] properties = {"reportParamId","paramName","paramCode"};
		String data = JSONUtil.toJson(list,properties,total);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}

}
