package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.RewPunDao;
import com.csit.dao.RewPunTypeDao;
import com.csit.dao.UserDAO;
import com.csit.model.RewPun;
import com.csit.model.RewPunType;
import com.csit.model.User;
import com.csit.service.RewPunService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

@Service
public class RewPunServiceImpl implements RewPunService {
	@Resource
	private RewPunDao rewPunDao;
	@Resource
	private RewPunTypeDao rewPunTypeDao;
	@Resource
	private UserDAO userDao;
	public ServiceResult save(RewPun model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写奖惩信息");
			return result;
		}
		if(model.getUser()==null){
			result.setMessage("请选择奖惩员工");
			return result;
		}
		User user=userDao.get(model.getUser().getUserId());
		if( user==null){
			result.setMessage("请选择已有的员工");
			return result;
		}
		if(model.getRewPunType()==null){
			result.setMessage("请选择奖惩类别");
			return result;
		}
		RewPunType rewPunType=rewPunTypeDao.get(model.getRewPunType().getRewPunTypeId());
		if( rewPunType==null){
			result.setMessage("请选择已有的奖惩类别");
			return result;
		}
		if (model.getRewPunPrice() == null) {
			result.setMessage("请填写奖惩金额");
			return result;
		}
		if (model.getRewPunDate() == null) {
			result.setMessage("请填写奖惩日期");
			return result;
		}
		if(model.getHandler()==null){
			result.setMessage("请选择经办人");
			return result;
		}
		User handler=userDao.get(model.getHandler().getUserId());
		if( handler==null){
			result.setMessage("请选择已有的经办人");
			return result;
		}
		if(model.getRewPunId()==null){//新增
			rewPunDao.save(model);
		}else{
			RewPun oldModel = rewPunDao.load(model.getRewPunId());
			if(oldModel==null){
				result.setMessage("该奖惩已不存在");
				return result;
			}
			oldModel.setUser(model.getUser());
			oldModel.setRewPunType(model.getRewPunType());
			oldModel.setRewPunPrice(model.getRewPunPrice());
			oldModel.setRewPunDate(model.getRewPunDate());
			oldModel.setHandler(model.getHandler());
			oldModel.setNote(model.getNote());
			
		}
		result.setIsSuccess(true);
		result.addData("rewPunId", model.getRewPunId());
		return result;
	}
	public ServiceResult query(RewPun model, String beginDate, String endDate, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		
		List<RewPun> list = rewPunDao.query(model,beginDate,endDate,page,rows);
		
		String[] properties = {"rewPunId","user.userId","user.userName","rewPunType.rewPunTypeName",
				"rewPunType.rewPunTypeId","rewPunPrice","rewPunDate","note","handlerName","handlerId"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	public ServiceResult getTotalCount(RewPun model, String beginDate, String endDate) {
		ServiceResult result = new ServiceResult(false);
		Long data = rewPunDao.getTotalCount(model,beginDate,endDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(RewPun model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getRewPunId()==null){
			result.setMessage("请选择要删除的奖惩类型");
			return result;
		}
		RewPun oldModel = rewPunDao.load(model.getRewPunId());
		if(oldModel==null){
			result.setMessage("该奖惩已不存在");
			return result;
		}else{
			rewPunDao.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
}
