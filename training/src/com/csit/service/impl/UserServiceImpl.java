package com.csit.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.SchoolDAO;
import com.csit.dao.UserDAO;
import com.csit.model.Right;
import com.csit.model.School;
import com.csit.model.User;
import com.csit.service.UserService;
import com.csit.util.JSONUtil;
import com.csit.util.MD5Util;
import com.csit.util.StringUtil;
import com.csit.util.TreeUtil;
import com.csit.vo.GobelConstants;
import com.csit.vo.ServiceResult;
import com.csit.vo.TreeNode;

@Service
public class UserServiceImpl extends BaseServiceImpl<User,Integer> implements UserService{
	
	@Resource
	private UserDAO userDAO;
	@Resource
	private SchoolDAO schoolDAO;
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserServ#login(java.lang.String, java.lang.String)
	 */
	public User login(String userCode, String userPwd) {
		String[] propertyNames = {"userCode","userPwd"};
		Object[] values = {userCode,userPwd};
		return userDAO.load(propertyNames, values);
	}
	/*
	 * 	(non-Javadoc)   
	 * @see org.linys.service.UserService#add(org.linys.model.User)
	 */
	public ServiceResult add(User model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||StringUtils.isEmpty(model.getUserName())){
			result.setMessage("请填写用户名称");
			return result;
		}
		if(StringUtils.isEmpty(model.getUserCode())){
			result.setMessage("请填写登录名称");
			return result;
		}
		User oldModel = userDAO.load("userCode", model.getUserCode());
		if(oldModel!=null){
			result.setMessage("该登录名称已存在，请换个登录名称");
			return result;
		}
		String userPwd = MD5Util.getMD5(GobelConstants.DEFAULT_USER_PWD);
		model.setUserPwd(userPwd);
		userDAO.save(model);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserService#query(Integer, Integer, org.linys.model.User)
	 */
	public String query(Integer page, Integer rows, User model) {
		if(model!=null&&model.getSchool()!=null&&model.getSchool().getSchoolId()!=-1){
			School oldSchool = schoolDAO.load(model.getSchool().getSchoolId());
			model.getSchool().setSchoolCode(oldSchool.getSchoolCode());
		}
		List<User> list = userDAO.query(page,rows,model);
		Long total = userDAO.count(model);
		String[] properties = {"userId","userName","userCode","school.schoolName","school.schoolId"};
		String ajaxString = JSONUtil.toJson(list, properties, total);
		return ajaxString;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserService#update(org.linys.model.User)
	 */
	public ServiceResult update(User model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||StringUtils.isEmpty(model.getUserName())){
			result.setMessage("请填写用户名称");
			return result;
		}
		if(StringUtils.isEmpty(model.getUserCode())){
			result.setMessage("请填写登录名称");
			return result;
		}
		User oldModel = userDAO.load("userCode", model.getUserCode());
		if(oldModel!=null&&!oldModel.getUserId().equals(model.getUserId())){
			result.setMessage("该登录名称已存在，请换个登录名称");
			return result;
		}
		String userPwd = MD5Util.getMD5(model.getUserCode());
		model.setUserPwd(userPwd);
		if(oldModel!=null&&oldModel.getUserId().equals(model.getUserId())){
			oldModel.setUserCode(model.getUserCode());
			oldModel.setUserPwd(model.getUserPwd());
			oldModel.setUserName(model.getUserName());
			userDAO.update(oldModel);
		}else{
			userDAO.update(model);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserService#delete(org.linys.model.User)
	 */
	public ServiceResult delete(User model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getUserId()==null){
			result.setMessage("请选择要删除的用户");
			return result;
		}
		User user = userDAO.load(model.getUserId());
		userDAO.delete(user);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserService#queryRootRight(org.linys.model.User)
	 */
	public String queryRootRight(User model) {
		String result = "[]";
		if(model!=null&&model.getUserId()!=null){
			List<Map<String,Object>> rootList = userDAO.getRootRight(model.getUserId());
			List<Right> rootRightList = toRightList(rootList);
			List<TreeNode> rootTreeNodeList = TreeUtil.toTreeNodeList(rootRightList);
			if(rootRightList!=null){
				for (int i =0;i<rootRightList.size();i++) {
					Right rootRight = rootRightList.get(i);
					if(!rootRight.getIsLeaf()){
						List<Map<String,Object>> children = userDAO.getChildrenRight(model.getUserId(),rootRight.getRightId());
						List<Right> childrenRightList = toRightList(children);
						List<TreeNode> childrenTreeNodeList = TreeUtil.toTreeNodeList(childrenRightList);
						rootTreeNodeList.get(i).setChildren(childrenTreeNodeList);
					}
				}
			}
			result = TreeUtil.toJSON(rootTreeNodeList);
		}
		
		return result;
	}
	/**
	 * @Description:  将Map装化成Right
	 * @Create: 2012-10-29 上午12:10:38
	 * @author lys
	 * @update logs
	 * @param treeNodeMap
	 * @return
	 * @throws Exception
	 */
	private Right toRight(Map<String,Object> treeNodeMap){
		if(treeNodeMap==null){
			return null;
		}
		Right right = new Right();
		right.setRightId(Integer.parseInt(treeNodeMap.get("rightId").toString()));
		right.setRightName(treeNodeMap.get("rightName").toString());
		if(treeNodeMap.get("rightUrl")!=null&&StringUtils.isNotEmpty(treeNodeMap.get("rightUrl").toString())){
			right.setRightUrl(treeNodeMap.get("rightUrl").toString());
		}
		String isLeaf = treeNodeMap.get("isLeaf").toString();
		if("true".equals(isLeaf)||"1".equals(isLeaf)){
			right.setIsLeaf(new Boolean(true));
		}else{
			right.setIsLeaf(new Boolean(false));
		}
		
		String state = treeNodeMap.get("state").toString();
		if("1".equals(state)){
			right.setState(new Boolean(true));
		}else{
			right.setState(new Boolean(false));
		}
		return right;
	}
	/**
	 * @Description: 将ListMap<String,Object>转化成List<Right>
	 * @Create: 2012-10-29 上午12:20:39
	 * @author lys
	 * @update logs
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private List<Right> toRightList(List<Map<String,Object>> list){
		if(list==null){
			return null;
		}
		List<Right> rightList = new ArrayList<Right>();
		for (Map<String, Object> map : list) {
			Right right = toRight(map);
			if(right!=null){
				rightList.add(right);
			}
		}
		return rightList;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserService#queryChildrenRight(org.linys.model.User, java.lang.Integer)
	 */
	public String queryChildrenRight(User model, Integer rightId) {
		String result = "[]";
		if(model!=null&&model.getUserId()!=null){
			List<Map<String,Object>> children = userDAO.getChildrenRight(model.getUserId(),rightId);
			List<Right> childrenRightList = toRightList(children);
			result = TreeUtil.toJSONRightList(childrenRightList);
		}
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserService#getRootRightMain(java.lang.String)
	 */
	public String getRootRightMain(Integer userId) {
		String result = "[]";
		List<Map<String,Object>> rootList = userDAO.getRootRight(userId); 
		if(rootList!=null&&rootList.size()!=0){
			Integer rightId = Integer.parseInt(rootList.get(0).get("rightId").toString());
			//取得孩子节点
			List<Right> childrenRightList = getChildrenRight(userId,rightId);
			result = TreeUtil.toJSONRightList(childrenRightList);
		}
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserService#getSelfInfor(org.linys.model.User)
	 */
	@Override
	public ServiceResult getSelfInfor(Integer userId) {
		ServiceResult result = new ServiceResult(false);
		if(userId==null){
			result.setMessage("对不起，您还没登录");
			return result;
		}
		User oldModel = userDAO.load(userId);
		result.addData("userId", oldModel.getUserId());
		result.addData("userCode", oldModel.getUserCode());
		result.addData("userName", oldModel.getUserName());
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserService#updateSelfInfo(java.lang.String, org.linys.model.User)
	 */
	@Override
	public ServiceResult updateSelfInfo(Integer userId, User model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写个人信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getUserCode())){
			result.setMessage("请填写登录名");
			return result;
		}
		if(StringUtils.isEmpty(model.getUserName())){
			result.setMessage("请填写用户名");
			return result;
		}
		User oldModel = userDAO.load("userCode", model.getUserCode());
		if(oldModel!=null&&!oldModel.getUserId().equals(userId)){
			result.setMessage("该登录名已存在，请换个登录名");
			return result;
		}
		if(oldModel!=null&&oldModel.getUserId().equals(userId)){
			oldModel.setUserName(model.getUserName());
			userDAO.update(oldModel);
		}else{
			oldModel = userDAO.load(userId);
			oldModel.setUserCode(model.getUserCode());
			oldModel.setUserName(model.getUserName());
			userDAO.update(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.service.UserService#modifyPwd(org.linys.model.User, java.lang.String)
	 */
	@Override
	public ServiceResult modifyPwd(User model, String newUserPwd) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getUserId()==null){
			result.setMessage("对不起你还没登陆系统");
			return result;
		}
		if(StringUtils.isEmpty(model.getUserPwd())){
			result.setMessage("请输入原密码");
			return result;
		}
		if(StringUtils.isEmpty(newUserPwd)){
			result.setMessage("请输入新密码");
			return result;
		}
		
		User oldModel = userDAO.load(model.getUserId());
		String userPwdMD5  = MD5Util.getMD5(model.getUserPwd());
		String oldUserPwd = oldModel.getUserPwd();
		if(!oldUserPwd.equals(userPwdMD5)){
			result.setMessage("你输入的原密码不正确");
			return result;
		}
		String newUserPwdMD5 = MD5Util.getMD5(newUserPwd);
		oldModel.setUserPwd(newUserPwdMD5);
		result.setIsSuccess(true);
		return result;
	}
	/**
	 * @Description: 取得子权限
	 * @Create: 2013-1-29 上午10:37:00
	 * @author lys
	 * @update logs
	 * @param userId
	 * @param rightId
	 * @return
	 */
	private List<Right> getChildrenRight(Integer userId,Integer rightId){
		List<Map<String,Object>> children = userDAO.getChildrenRight(userId,rightId);
		List<Right> childrenRightList = toRightList(children);
		for (Right right : childrenRightList) {
			if(!right.getIsLeaf()){
				right.setChildrenRightList(getChildrenRight(userId,right.getRightId()));
			}
		}
		return childrenRightList;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#save(com.csit.model.User)
	 */
	@Override
	public ServiceResult save(User model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写用户信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getUserName())){
			result.setMessage("请填写用户名称");
			return result;
		}
		if(StringUtils.isEmpty(model.getUserCode())){
			result.setMessage("请填写登录名称");
			return result;
		}
		if(model.getSchool()==null||model.getSchool().getSchoolId()==null){
			result.setMessage("请选择校区");
			return result;
		}
		School oldSchool = schoolDAO.load(model.getSchool().getSchoolId());
		if(oldSchool==null){
			result.setMessage("请选择存在的校区");
			return result;
		}
		if(model.getUserId()==null){//新增
			User oldModel = userDAO.load("userCode", model.getUserCode());
			if(oldModel!=null){
				result.setMessage("该登录名称已存在，请换个登录名称");
				return result;
			}
			String userPwd = MD5Util.getMD5(GobelConstants.DEFAULT_USER_PWD);
			model.setUserPwd(userPwd);
			model.setSex(1);
			model.setIsPartTime(0);
			model.setIsTeacher(0);
			model.setBirthday(new Date());
			model.setComeDate(new Date());
			model.setStatus(1);
			userDAO.save(model);
		}else{
			User oldModel = userDAO.load("userCode", model.getUserCode());
			if(oldModel!=null&&oldModel.getUserId().intValue()!=model.getUserId().intValue()){
				result.setMessage("该登录名称已存在，请换个登录名称");
				return result;
			}
			if(oldModel!=null&&oldModel.getUserId().intValue()!=model.getUserId().intValue()){
				oldModel.setUserCode(model.getUserCode());
				oldModel.setUserName(model.getUserName());
				oldModel.setSchool(model.getSchool());
			}else{
				oldModel = userDAO.load(model.getUserId());
				oldModel.setUserCode(model.getUserCode());
				oldModel.setUserName(model.getUserName());
				oldModel.setSchool(model.getSchool());
			}
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#saveEmployee(com.csit.model.User)
	 */
	@Override
	public ServiceResult saveEmployee(User model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写员工信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getUserName())){
			result.setMessage("请填写姓名");
			return result;
		}
		if(StringUtils.isEmpty(model.getUserCode())){
			result.setMessage("请填写登陆名称");
			return result;
		}
		//性别
		if(model.getSex()==null){
			result.setMessage("请选择性别");
			return result;
		}
		if(model.getSex()!=0&&model.getSex()!=1){
			result.setMessage("请选择提供选择的性别");
			return result;
		}
		//出生日期
		if(model.getBirthday()==null){
			result.setMessage("请选择出生日期");
			return result;
		}
		//是否兼职
		if(model.getIsPartTime()==null){
			result.setMessage("请选择是否兼职");
			return result;
		}
		if(model.getIsPartTime()!=0&&model.getIsPartTime()!=1){
			result.setMessage("请选择提供选择的是否兼职");
			return result;
		}
		//是否授课
		if(model.getIsTeacher()==null){
			result.setMessage("请选择是否授课");
			return result;
		}
		if(model.getIsTeacher()!=0&&model.getIsTeacher()!=1){
			result.setMessage("请选择提供选择的是否授课");
			return result;
		}
		//任聘日期
		if(model.getComeDate()==null){
			result.setMessage("请选择任聘日期");
			return result;
		}
		if(model.getSchool()==null||model.getSchool().getSchoolId()==null){
			result.setMessage("请选择校区");
			return result;
		}
		School oldSchool = schoolDAO.load(model.getSchool().getSchoolId());
		if(oldSchool==null){
			result.setMessage("请选择存在的校区");
			return result;
		}
		//状态
		if(model.getStatus()==null){
			result.setMessage("请选择员工状态");
			return result;
		}
		if(model.getStatus()!=0&&model.getStatus()!=1){
			result.setMessage("请选择提供选择的员工状态");
			return result;
		}
		if(model.getUserId()==null){//新增
			User oldModel = userDAO.load("userCode", model.getUserCode());
			if(oldModel!=null){
				result.setMessage("该登录名称已存在，请换个登录名称");
				return result;
			}
			String userPwd = MD5Util.getMD5(GobelConstants.DEFAULT_USER_PWD);
			model.setUserPwd(userPwd);
			userDAO.save(model);
			result.addData("userId", model.getUserId());
		}else{
			User oldModel = userDAO.load("userCode", model.getUserCode());
			if(oldModel!=null&&oldModel.getUserId().intValue()!=model.getUserId().intValue()){
				result.setMessage("该登录名称已存在，请换个登录名称");
				return result;
			}
			if(oldModel!=null&&oldModel.getUserId().intValue()!=model.getUserId().intValue()){
				model.setUserPwd(oldModel.getUserPwd());
				userDAO.evict(oldModel);
				userDAO.update(model);
			}else{
				oldModel = userDAO.load(model.getUserId());
				model.setUserPwd(oldModel.getUserPwd());
				userDAO.evict(oldModel);
				userDAO.update(model);
			}
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#queryEmployee(com.csit.model.User, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult queryEmployee(User model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		if(model!=null&&model.getSchool()!=null&&model.getSchool().getSchoolId()!=-1){
			School oldSchool = schoolDAO.load(model.getSchool().getSchoolId());
			model.getSchool().setSchoolCode(oldSchool.getSchoolCode());
		}
		List<User> list = userDAO.queryEmployee(model,page,rows);
		
		String[] properties = {"userId","userName","userCode","status","basePay","hourFee","isTeacher","school.schoolName"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#getTotalCountEmployee(com.csit.model.User)
	 */
	@Override
	public ServiceResult getTotalCountEmployee(User model) {
		ServiceResult result = new ServiceResult(false);
		if(model!=null&&model.getSchool()!=null&&model.getSchool().getSchoolId()!=-1){
			School oldSchool = schoolDAO.load(model.getSchool().getSchoolId());
			model.getSchool().setSchoolCode(oldSchool.getSchoolCode());
		}
		Long data = userDAO.getTotalCountEmployee(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#init(java.lang.Integer)
	 */
	@Override
	public ServiceResult init(Integer userId) {
		ServiceResult result = new ServiceResult(false);
		if(userId==null){
			result.setMessage("请选择打开的数据行");
			return result;
		}
		User user = userDAO.load(userId);
		String[] properties = {"userId","userCode","userName","sex","birthday",
				"isPartTime","isTeacher","comeDate","outDate",
				"course","finishSchool","diploma","resume",
				"tel","email","address","postCode",
				"headship","idcard","basePay","hourFee","status","school.schoolId"};
		String userData = JSONUtil.toJson(user,properties);
		result.addData("userData",userData);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#mulDelele(java.lang.String)
	 */
	@Override
	public ServiceResult mulDelele(String ids) {
		ServiceResult result = new ServiceResult(false);
		if(StringUtils.isEmpty(ids)){
			result.setMessage("请选择要删除的员工");
			return result;
		}
		String[] idArray =StringUtil.split(ids);
		if(idArray.length==0){
			result.setMessage("请选择要删除的员工");
			return result;
		}
		for (String id : idArray) {
			User oldUser = userDAO.load(Integer.parseInt(id));
			if(oldUser!=null){
				userDAO.delete(oldUser);
			}
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#mulUpdateStatus(java.lang.String, com.csit.model.User)
	 */
	@Override
	public ServiceResult mulUpdateStatus(String ids, User model) {
		ServiceResult result = new ServiceResult(false);
		if(StringUtils.isEmpty(ids)){
			result.setMessage("请选择要修改状态的员工");
			return result;
		}
		String[] idArray =StringUtil.split(ids);
		if(idArray.length==0){
			result.setMessage("请选择要修改状态的员工");
			return result;
		}
		if(model==null||model.getStatus()==null){
			result.setMessage("请选择要修改成的状态");
			return result;
		}
		boolean haveUpdateShzt = false;
		for (String id : idArray) {
			User oldUser = userDAO.load(Integer.parseInt(id));
			if(oldUser!=null&&oldUser.getStatus().intValue()!=model.getStatus().intValue()){
				oldUser.setStatus(model.getStatus());
				haveUpdateShzt = true;
			}
		}
		if(!haveUpdateShzt){
			result.setMessage("没有可修改状态的员工");
			return result;
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#queryCombobox(java.lang.String)
	 */
	@Override
	public String queryCombobox(String schoolCode) {
		List<User> list = userDAO.queryCombobox(schoolCode);
		String[] properties = {"userId","userName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#queryIsTeacherCombobox(java.lang.String)
	 */
	@Override
	public String queryIsTeacherCombobox(String schoolCode) {
		List<User> list = userDAO.queryIsTeacherCombobox(1,schoolCode);
		String[] properties = {"userId","userName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#getUrlRightAll(java.lang.Integer)
	 */
	@Override
	public String getUrlRightAll(Integer userId) {
		String result = "[]";
		List<Map<String,Object>> rootList = userDAO.getRootRight(userId); 
		if(rootList!=null&&rootList.size()!=0){
			Integer rightId = Integer.parseInt(rootList.get(0).get("rightId").toString());
			//取得孩子节点
			List<Right> childrenRightList = getChildrenRight(userId,rightId,1);
			result = TreeUtil.toJSONRightList(childrenRightList);
		}
		return result;
	}
	
	/**
	 * @Description: 根据类型取得子权限
	 * @Create: 2013-1-29 上午10:37:00
	 * @author lys
	 * @update logs
	 * @param userId
	 * @param rightId
	 * @return
	 */
	private List<Right> getChildrenRight(Integer userId,Integer rightId,Integer kind){
		List<Map<String,Object>> children = userDAO.getChildrenRight(userId,rightId,kind);
		List<Right> childrenRightList = toRightList(children);
		for (Right right : childrenRightList) {
			if(!right.getIsLeaf()){
				List<Right> tempList = getChildrenRight(userId,right.getRightId(),kind);
				if(tempList.size()==0){
					right.setIsLeaf(true);
				}
				right.setChildrenRightList(tempList);
			}
		}
		return childrenRightList;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.UserService#queryChildrenRight(com.csit.model.User, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public String queryChildrenRight(User model, Integer rightId, Integer kind) {
		String result = "[]";
		if(model!=null&&model.getUserId()!=null){
			List<Map<String,Object>> children = userDAO.getChildrenRight(model.getUserId(),rightId,kind);
			List<Right> childrenRightList = toRightList(children);
			result = TreeUtil.toJSONRightList(childrenRightList);
		}
		return result;
	}
	
}
