package com.csit.service.impl;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.RoleDAO;
import com.csit.dao.UserRoleDAO;
import com.csit.model.Role;
import com.csit.model.UserRole;
import com.csit.model.UserRoleId;
import com.csit.service.UserRoleService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole, UserRoleId> implements UserRoleService {
	@Resource
	private UserRoleDAO userRoleDAO;
	@Resource
	private RoleDAO roleDAO;
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserRoleService#queryRole(org.linys.model.UserRole)
	 */
	public String queryRole(UserRole model) {
		List<UserRole> list = userRoleDAO.queryRole(model);
		List<Role> roleList = roleDAO.queryAll();
		for (Role role : roleList) {
			for (UserRole userRole : list) {
				Integer roleId = role.getRoleId();
				if(roleId.equals(userRole.getUserRoleId().getRoleId())){
					role.setChecked("checked");
					break;
				}
			}
		}
		List<String> propertyList = new ArrayList<String>();
		propertyList.add("roleId");
		propertyList.add("roleName");
		propertyList.add("checked");
		String ajaxString = JSONUtil.toJson(roleList,propertyList);
		return ajaxString;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserRoleService#updateRole(org.linys.model.UserRole, java.lang.String, java.lang.String)
	 */
	public ServiceResult updateRole(UserRole model, String ids, String oldIds) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getUser()==null||model.getUser().getUserId()==null){
			result.setMessage("请选择用户");
			return result;
		}
		String[] idArray = StringUtil.split(ids);
		if(idArray.length==0){
			result.setMessage("请选中角色");
			return result;
		}
		String[] oldIdArray = {} ;
		if(StringUtils.isNotEmpty(oldIds)){
			oldIdArray = StringUtil.split(oldIds);
		}
		
		
		List<String> deleteIdList = new ArrayList<String>();
		List<String> addIdList = new ArrayList<String>();
		for (String oldId : oldIdArray) {
			boolean isDel = true;
			for (String id : idArray) {
				if(oldId.equals(id)){
					isDel =false;
					break;
				}
			}
			if(StringUtils.isNotEmpty(oldId)&&isDel){
				deleteIdList.add(oldId);
			}
		}
		for (String id : idArray) {
			boolean isAdd = true;
			for (String oldId : oldIdArray) {
				if(oldId.equals(id)){
					isAdd =false;
					break;
				}
			}
			if(StringUtils.isNotEmpty(id)&&isAdd){
				addIdList.add(id);
			}
		}
		
		if(addIdList.size()==0&&deleteIdList.size()==0){
			result.setMessage("角色没修改");
			return result;
		}
		Integer userId = model.getUser().getUserId();
		for (String id : deleteIdList) {
			UserRoleId userRoleId = new UserRoleId();
			userRoleId.setUserId(userId);
			userRoleId.setRoleId(Integer.parseInt(id));
			userRoleDAO.delete(userRoleId);
		}
		for (String id : addIdList) {
			UserRoleId userRoleId = new UserRoleId();
			userRoleId.setUserId(userId);
			userRoleId.setRoleId(Integer.parseInt(id));
			
			UserRole userRole = new UserRole();
			userRole.setUserRoleId(userRoleId);
			userRoleDAO.save(userRole);
		}
		
		result.setIsSuccess(true);
		return result;
	}
}
