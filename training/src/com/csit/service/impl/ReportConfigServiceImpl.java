package com.csit.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.ReportConfigDAO;
import com.csit.dao.ReportParamConfigDAO;
import com.csit.model.ReportConfig;
import com.csit.model.ReportParam;
import com.csit.model.ReportParamConfig;
import com.csit.service.ReportConfigService;
import com.csit.util.FileUtil;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;
/**
 * @Description:报表配置Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-23
 * @author lys
 * @vesion 1.0
 */
@Service
public class ReportConfigServiceImpl extends
		BaseServiceImpl<ReportConfig, String> implements ReportConfigService {
	@Resource
	private ReportConfigDAO reportConfigDAO;
	@Resource
	private ReportParamConfigDAO reportParamConfigDAO;
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportConfigService#delete(org.linys.model.ReportConfig)
	 */
	@Override
	public ServiceResult delete(ReportConfig model, String reportTemplatePath) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||StringUtils.isEmpty(model.getReportConfigId())){
			result.setMessage("请选择要删除的报表配置");
			return result;
		}
		ReportConfig oldModel = reportConfigDAO.load(model.getReportConfigId());
		if(oldModel==null){
			result.setMessage("该报表配置已不存在");
			return result;
		}else{
			FileUtils.deleteQuietly(new File(reportTemplatePath+File.separator+oldModel.getReportCode()+".grf"));
			reportConfigDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportConfigService#query(org.linys.model.ReportConfig, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(ReportConfig model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<ReportConfig> list = reportConfigDAO.query(model,page,rows);
		
		String[] properties = {"reportConfigId","reportCode","reportName","reportDetailSql","reportParamsSql","reportKind"};
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportConfigService#getTotalCount(org.linys.model.ReportConfig)
	 */
	@Override
	public ServiceResult getTotalCount(ReportConfig model) {
		ServiceResult result = new ServiceResult(false);
		Long data = reportConfigDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportConfigService#getByReportCode(java.lang.String)
	 */
	@Override
	public ReportConfig getByReportCode(String reportCode) {
		return reportConfigDAO.load("reportCode", reportCode);
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportConfigService#save(org.linys.model.ReportConfig, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceResult save(ReportConfig model, File file, String reportTemplatePath, String reportParamConfigIds,
			String deleteIds, String reportParamIds, String isNeedChooses) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写报表配置信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getReportKind()+"")){
			result.setMessage("请填写报表类型");
			return result;
		}
		if(StringUtils.isEmpty(model.getReportCode())){
			result.setMessage("请填写报表编号");
			return result;
		}
		if(StringUtils.isEmpty(model.getReportName())){
			result.setMessage("请填写报表名称");
			return result;
		}
		if(StringUtils.isEmpty(model.getReportDetailSql())){
			result.setMessage("请填写报表明细网格Sql");
			return result;
		}
		if(StringUtils.isEmpty(model.getReportConfigId())){//新增
			if(file==null){
				result.setMessage("请上传报表模板");
				return result;
			}
		}
		if(file!=null&&"grf".equals(FilenameUtils.getExtension(file.getName()))){
			result.setMessage("上传的文件格式不正确");
			return result;
		}
		
		String[] reportParamConfigIdArray = StringUtil.split(reportParamConfigIds);
		String[] deleteIdArray = StringUtil.split(deleteIds);
		String[] reportParamIdArray = StringUtil.split(reportParamIds);
		String[] isNeedChooseArray = StringUtil.split(isNeedChooses);
		
		if(StringUtils.isEmpty(model.getReportConfigId())){//新增
			ReportConfig oldModel = reportConfigDAO.load("reportCode", model.getReportCode());
			if(oldModel!=null){
				result.setMessage("该报表编号已存在");
				return result;
			}
			FileUtil.saveFile(file, reportTemplatePath+File.separator+model.getReportCode()+".grf");
			reportConfigDAO.save(model);
			for (int i = 0; i < reportParamIdArray.length&&StringUtils.isNotEmpty(reportParamIdArray[i]); i++) {
				String reportParamId = reportParamIdArray[i];
				String isNeedChoose = isNeedChooseArray[i];
				
				ReportParamConfig reportParamConfig = new ReportParamConfig();
				
				ReportParam reportParam = new ReportParam();
				reportParam.setReportParamId(reportParamId);
				
				reportParamConfig.setReportParam(reportParam);
				reportParamConfig.setReportConfig(model);
				reportParamConfig.setIsNeedChoose(Integer.parseInt(isNeedChoose));
				reportParamConfig.setArray(i);
				reportParamConfigDAO.save(reportParamConfig);
			}
		}else{
			ReportConfig oldModel = reportConfigDAO.load(model.getReportConfigId());
			if(oldModel==null){
				result.setMessage("该报表配置已不存在");
				return result;
			}else if(!oldModel.getReportCode().equals(model.getReportCode())){
				ReportConfig oldReportConfig = reportConfigDAO.load("reportCode", model.getReportCode());
				if(oldReportConfig!=null){
					result.setMessage("该报表编号已存在");
					return result;
				}
			}
			String oldReportCode = oldModel.getReportCode();
			oldModel.setReportKind(model.getReportKind());
			oldModel.setReportCode(model.getReportCode());
			oldModel.setReportName(model.getReportName());
			oldModel.setReportDetailSql(model.getReportDetailSql());
			oldModel.setReportParamsSql(model.getReportParamsSql());
			
			if(file!=null){
				//移除原先的模板文件，保存新的模板文件
				FileUtils.deleteQuietly(new File(reportTemplatePath+File.separator+oldReportCode+".grf"));
				FileUtil.saveFile(file, reportTemplatePath+File.separator+model.getReportCode()+".grf");
			}else{
				if(!model.getReportCode().equals(oldReportCode)){
					try {
						FileUtils.moveFile(new File(reportTemplatePath+File.separator+oldReportCode+".grf"), new File(reportTemplatePath+File.separator+model.getReportCode()+".grf"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			
			//删除已删的报表参数配置
			if(!"".equals(deleteIds)){
				for (String deleteId : deleteIdArray) {
					ReportParamConfig oldReportParamConfig = reportParamConfigDAO.load(deleteId);
					if(oldReportParamConfig!=null){
						reportParamConfigDAO.delete(oldReportParamConfig);
					}
				}
			}
			//根据采购单明细Id更新或新增
			for (int i = 0 ;i<reportParamIdArray.length&&StringUtils.isNotEmpty(reportParamIdArray[i]);i++) {
				String reportParamConfigId = reportParamConfigIdArray[i];
				String reportParamId = reportParamIdArray[i];
				String isNeedChoose = isNeedChooseArray[i];
				
				if(StringUtils.isEmpty(reportParamConfigId)){//新增
					ReportParamConfig reportParamConfig = new ReportParamConfig();
					
					ReportParam reportParam = new ReportParam();
					reportParam.setReportParamId(reportParamId);
					
					reportParamConfig.setReportParam(reportParam);
					reportParamConfig.setReportConfig(model);
					reportParamConfig.setIsNeedChoose(Integer.parseInt(isNeedChoose));
					reportParamConfig.setArray(i);
					reportParamConfigDAO.save(reportParamConfig);
				}else{
					ReportParamConfig oldReportParamConfig = reportParamConfigDAO.load(reportParamConfigId);
					oldReportParamConfig.setArray(i);
					oldReportParamConfig.setIsNeedChoose(Integer.parseInt(isNeedChoose));
					reportParamConfigDAO.update(oldReportParamConfig);
				}
			}
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportConfigService#init(java.lang.String)
	 */
	@Override
	public ServiceResult init(String reportConfigId) {
		ServiceResult result = new ServiceResult(false);
		ReportConfig reportConfig = reportConfigDAO.load(reportConfigId);
		String[] propertiesBuy = {"reportConfigId","reportCode","reportName",
				"reportDetailSql","reportParamsSql","reportKind"};
		String reportConfigData = JSONUtil.toJson(reportConfig,propertiesBuy);
		result.addData("reportConfigData",reportConfigData);
		
		List<ReportParamConfig> reportParamConfigList = reportParamConfigDAO.queryByReportConfigId(reportConfigId);
		String[] propertiesDetail = {"reportParamConfigId","reportParam.reportParamId","reportParam.paramCode","reportParam.paramName","isNeedChoose"};
		String detailData = JSONUtil.toJson(reportParamConfigList,propertiesDetail);
		result.addData("detailData", detailData);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportConfigService#getReport1()
	 */
	@Override
	public ServiceResult getReport1() {
		ServiceResult result = new ServiceResult(false);
		List<ReportConfig> list = reportConfigDAO.getReport1();
		String[] properties = {"reportConfigId","reportName","reportCode"};
		String reportConfig1Data = JSONUtil.toJsonWithoutRows(list,properties);
		result.addData("reportConfig1Data", reportConfig1Data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportConfigService#getReportParams(org.linys.model.ReportConfig)
	 */
	@Override
	public ServiceResult getReportParams(ReportConfig model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||StringUtils.isEmpty(model.getReportConfigId())){
			result.setMessage("请选择报表");
			return result;
		}
		List<ReportParamConfig> list = reportParamConfigDAO.queryByReportConfigId(model.getReportConfigId());
		String[] propertiesDetail = {"reportParam.paramName","reportParam.paramCode","isNeedChoose"};
		String paramsData = JSONUtil.toJsonWithoutRows(list,propertiesDetail);
		result.addData("paramsData", paramsData);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.ReportConfigService#getReportAll()
	 */
	@Override
	public ServiceResult getReportAll() {
		ServiceResult result = new ServiceResult(false);
		List<ReportConfig> list = reportConfigDAO.queryAll();
		String[] properties = {"reportConfigId","reportName","reportCode"};
		String reportConfigData = JSONUtil.toJsonWithoutRows(list,properties);
		result.addData("reportConfigData", reportConfigData);
		result.setIsSuccess(true);
		return result;
	}

}
