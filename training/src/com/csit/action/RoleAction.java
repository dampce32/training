package com.csit.action;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Role;
import com.csit.model.User;
import com.csit.service.RoleService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description: 角色Action
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-27
 * @author lys
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction implements ModelDriven<Role> {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RoleAction.class);
	private Role model = new Role();
	@Resource
	private RoleService roleService;
	public Role getModel() {
		return model;
	}
	/**
	 * @Description: 添加角色
	 * @Create: 2012-10-27 下午4:22:08
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void add(){
		ServiceResult result = new ServiceResult(false);
		try {
			Integer userId = getSession(User.LOGIN_USERID)==null?null:Integer.parseInt(getSession(User.LOGIN_USERID).toString());
			result = roleService.add(model,userId);
		} catch (Exception e) {
			result.setMessage("添加角色失败");
			logger.error("添加角色失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 修改角色
	 * @Create: 2012-10-27 下午4:22:08
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void update(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = roleService.update(model);
		} catch (Exception e) {
			result.setMessage("修改角色失败");
			logger.error("修改角色失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 删除角色
	 * @Create: 2012-10-27 下午7:43:08
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = roleService.delete(model);
		} catch (Exception e) {
			result.setMessage("删除角色失败");
			logger.error("删除角色失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 查询角色列表
	 * @Create: 2012-10-27 下午7:50:17
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void query(){
		try {
			String jsonString = roleService.query(model);
			ajaxJson(jsonString);
		} catch (Exception e) {
			logger.error("查询角色列表失败", e);
		}
	}
}
