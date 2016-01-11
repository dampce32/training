package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.SchoolDAO;
import com.csit.model.School;
import com.csit.service.SchoolService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.util.TreeUtil;
import com.csit.vo.ServiceResult;
import com.csit.vo.TreeNode;
/**
 * @Description:校区Service实现类
 * @Copyschool: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-25
 * @Author lys
 */
@Service
public class SchoolServiceImpl extends BaseServiceImpl<School, Integer>
		implements SchoolService {
	@Resource
	SchoolDAO schoolDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.SchoolService#selectRoot()
	 */
	@Override
	public String selectRoot() {
		School root = schoolDAO.selectRoot();
		TreeNode rootNode = TreeUtil.toTreeNode(root);
		if(rootNode!=null){
			if(!root.getIsLeaf()){
				List<School> children = schoolDAO.getChildren(root);
				List<TreeNode> childrenNodeList = TreeUtil.toTreeNodeSchoolList(children);
				rootNode.setChildren(childrenNodeList);
			}
		}
		return TreeUtil.toJSON(rootNode);
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.SchoolService#selectTreeNode(com.csit.model.School)
	 */
	@Override
	public List<School> selectTreeNode(School model) {
		return schoolDAO.getChildren(model);
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.SchoolService#query(java.lang.Integer, java.lang.Integer, com.csit.model.School)
	 */
	@Override
	public String query(Integer page, Integer rows, School model) {
		if(model.getSchoolId()==null){
			School root = schoolDAO.selectRoot();
			model.setSchoolId(root.getSchoolId());
		}
		
		List<School> list = schoolDAO.query(page,rows,model);
		Long total=schoolDAO.count(model);
		
		String[] properties = {"schoolId","schoolCode","schoolName","tel","address","status"};
		
		return JSONUtil.toJson(list, properties, total);
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.SchoolService#save(com.csit.model.School)
	 */
	@Override
	public ServiceResult save(School model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写校区信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getSchoolName())){
			result.setMessage("请填写校区名");
			return result;
		}
		if(model.getStatus()==null){
			result.setMessage("请选择校区状态");
			return result;
		}
		if(model.getParentSchool()==null||model.getParentSchool().getSchoolId()==null){
			result.setMessage("请选择上级校区");
			return result;
		}
		if(model.getSchoolId()==null){//新增
			if(model.getParentSchool().getSchoolId()==null){
				result.setMessage("请选择父校区");
				return result;
			}
			//取得该校区编号
			School parentSchool = schoolDAO.load(model.getParentSchool().getSchoolId());
			model.setSchoolCode(schoolDAO.getNewCode(parentSchool.getSchoolCode()));
			//取得该父校区下的校区排序最大值
			Integer maxArray = schoolDAO.getMaxArray(model.getParentSchool().getSchoolId());
			model.setArray(maxArray+1);
			model.setIsLeaf(true);
			schoolDAO.save(model);
			//更新父节点是否叶子节点
			if(parentSchool.getIsLeaf()){
				parentSchool.setIsLeaf(false);
			}
			result.getData().put("schoolId", model.getSchoolId());
		}else{
			School oldModel = schoolDAO.load(model.getSchoolId());
			if(oldModel==null){
				if(model.getParentSchool().getSchoolId()==null){
					result.setMessage("要修改的校区已不存在");
					return result;
				}
			}
			oldModel.setSchoolName(model.getSchoolName());
			oldModel.setTel(model.getTel());
			oldModel.setAddress(model.getAddress());
			schoolDAO.update(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * 
	 */
	@Override
	public ServiceResult mulDelete(String ids) {
		ServiceResult result = new ServiceResult(false);
		if(StringUtils.isEmpty(ids)){
			result.setMessage("请选择要删除的记录");
			return result;
		}
		String[] idArray = StringUtil.split(ids);
		if(idArray.length==0){
			result.setMessage("请选择要删除的记录");
			return result;
		}
		Integer parentID = null;
		School item = null;
		if(StringUtils.isNotEmpty(idArray[0])){
			item = schoolDAO.load(new Integer(idArray[0]));
		}
		for (String id : idArray) {
			if(StringUtils.isNotEmpty(idArray[0])){
				schoolDAO.delete(new Integer(id));
			}
		}
		//更新父节点是否是叶子节点
		if(idArray.length>0){
			if(item!=null&&item.getParentSchool()!=null){
				parentID = item.getParentSchool().getSchoolId();
				Long countChildren = schoolDAO.countChildren(parentID);
				if(countChildren==0){
					schoolDAO.updateIsLeaf(parentID, true);
				}
			}
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.SchoolService#updateArray(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult updateArray(Integer schoolId, Integer updateSchoolId) {
		ServiceResult result = new ServiceResult(false);
		if(schoolId==null||updateSchoolId==null){
			result.setMessage("请选择要改变校区排序的校区");
			return result;
		}
		School oldModel = schoolDAO.load(schoolId);
		School updateSchool = schoolDAO.load(updateSchoolId);
		Integer oldArray = oldModel.getArray();
		Integer upateArray = updateSchool.getArray();
		oldModel.setArray(upateArray);
		updateSchool.setArray(oldArray);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.SchoolService#queryAllCombobox()
	 */
	@Override
	public String queryAllCombobox() {
		List<School> list = schoolDAO.queryAllCombobox();
		String[] properties = {"schoolId","schoolName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.SchoolService#querySelfCombobox(java.lang.String)
	 */
	@Override
	public String querySelfCombobox(String schoolCode) {
		List<School> list = schoolDAO.querySelfCombobox(schoolCode);
		String[] properties = {"schoolId","schoolCode","schoolName"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}

}
