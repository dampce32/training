package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.FeeItemDAO;
import com.csit.model.FeeItem;
import com.csit.service.FeeItemService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 消费项Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Service
public class FeeItemServiceImpl extends BaseServiceImpl<FeeItem, Integer>
		implements FeeItemService {
	@Resource
	private FeeItemDAO feeItemDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.FeeItemService#save(com.csit.model.FeeItem)
	 */
	@Override
	public ServiceResult save(FeeItem model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写消费项信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getFeeItemName())){
			result.setMessage("请填写消费项名称");
			return result;
		}
		if(model.getFeeItemId()==null){//新增
			feeItemDAO.save(model);
		}else{
			FeeItem oldModel = feeItemDAO.load(model.getFeeItemId());
			if(oldModel==null){
				result.setMessage("该消费项已不存在");
				return result;
			}
			oldModel.setFeeItemName(model.getFeeItemName());
			oldModel.setFee(model.getFee());
			oldModel.setStatus(model.getStatus());
		}
		result.setIsSuccess(true);
		result.addData("feeItemId", model.getFeeItemId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.FeeItemService#delete(com.csit.model.FeeItem)
	 */
	@Override
	public ServiceResult delete(FeeItem model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getFeeItemId()==null){
			result.setMessage("请选择要删除的消费项");
			return result;
		}
		FeeItem oldModel = feeItemDAO.load(model.getFeeItemId());
		if(oldModel==null){
			result.setMessage("该消费项已不存在");
			return result;
		}else{
			feeItemDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.FeeItemService#query(com.csit.model.FeeItem, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(FeeItem model, Integer page, Integer rows, String feeItemIds) {
		ServiceResult result = new ServiceResult(false);
		Integer[] feeItemIdArr;
		if(StringUtils.isNotEmpty(feeItemIds)){
			String[] s = StringUtil.split(feeItemIds);
			feeItemIdArr=new Integer[s.length];
			for(int i=0;i<s.length;i++){
				feeItemIdArr[i]=Integer.parseInt(s[i]);
			}
		}else{
			feeItemIdArr=null;
		}
		List<FeeItem> list = feeItemDAO.query(model,page,rows,feeItemIdArr);
		
		String[] properties = {"feeItemId","feeItemName","fee","status"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.FeeItemService#getTotalCount(com.csit.model.FeeItem)
	 */
	@Override
	public ServiceResult getTotalCount(FeeItem model, String feeItemIds) {
		ServiceResult result = new ServiceResult(false);
		Integer[] feeItemIdArr;
		if(StringUtils.isNotEmpty(feeItemIds)){
			String[] s = StringUtil.split(feeItemIds);
			feeItemIdArr=new Integer[s.length];
			for(int i=0;i<s.length;i++){
				feeItemIdArr[i]=Integer.parseInt(s[i]);
			}
		}else{
			feeItemIdArr=null;
		}
		Long data = feeItemDAO.getTotalCount(model,feeItemIdArr);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.FeeItemService#queryCombobox()
	 */
	@Override
	public String queryCombobox() {
		List<FeeItem> list = feeItemDAO.queryAll();
		String[] properties = {"feeItemId","feeItemName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	@Override
	public ServiceResult updateStatus(FeeItem model) {
		ServiceResult result = new ServiceResult(false);


		FeeItem oldModel = feeItemDAO.load(model.getFeeItemId());
		if(oldModel==null){
			result.setMessage("该消费项已不存在");
			return result;
		}
		oldModel.setStatus(model.getStatus());
		
		result.setIsSuccess(true);
		result.addData("feeItemId", model.getFeeItemId());
		return result;
	}

}
