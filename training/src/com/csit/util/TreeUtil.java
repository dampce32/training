package com.csit.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.csit.model.Right;
import com.csit.model.RoleRight;
import com.csit.model.School;
import com.csit.vo.TreeNode;
import com.csit.vo.TreeNode.StateType;
/**
 * @Description:easyui 生产树的工具类
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-14
 * @author lys
 * @vesion 1.0
 */
public class TreeUtil {
	/**
	 * 空树对象
	 */
	public static final String EMPTY = "[]";
	/**
	 * @Description: 将树节点转化成JSONObject对象
	 * @Create: 2012-10-14 下午11:22:50
	 * @author lys
	 * @update logs
	 * @param treeNode
	 * @return
	 * @throws Exception
	 */
	public static JSONObject toJSONObject(TreeNode treeNode){
		JSONObject object = new JSONObject();
		try {
			object.put("id", treeNode.getId());
			object.put("text", treeNode.getText());
			object.put("iconCls", treeNode.getIconCls());
			if(treeNode.getChecked()!=null&&treeNode.getChecked()==true){
				object.put("checked", treeNode.getChecked());
			}
			object.put("state", treeNode.getState());
			if(treeNode.getAttributes()!=null && !treeNode.getAttributes().isEmpty()){
				object.put("attributes", treeNode.getAttributes());
			}
			if(treeNode.getChildren().size()>0){
				object.put("children", toJSONArray(treeNode.getChildren()));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
	/**
	 * @Description: 将多个树节点转化成JSONArray
	 * @Create: 2012-10-14 下午11:24:05
	 * @author lys
	 * @update logs
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static JSONArray toJSONArray(List<TreeNode> list){
		JSONArray array = new JSONArray();
		for(TreeNode tree : list){
			array.add(toJSONObject(tree));
		}
		return array;
	}
	/**
	 * @Description: 生成单节点树
	 * @Create: 2012-10-14 下午11:25:13
	 * @author lys
	 * @update logs
	 * @param treeNode
	 * @return
	 * @throws Exception
	 */
	public static String toJSON(TreeNode treeNode){
		JSONObject object = toJSONObject(treeNode);
		return "["+object.toString()+"]";
	}
	/**
	 * @Description: 生成多节点树
	 * @Create: 2012-10-14 下午11:25:58
	 * @author lys
	 * @update logs
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static String toJSON(List<TreeNode> list){
		JSONArray array = toJSONArray(list);
		return array.toString();
	}
	/**
	 * @Description: 将权限Right转化成TreeNode
	 * @Create: 2012-10-14 下午11:34:39
	 * @author lys
	 * @update logs
	 * @param right
	 * @return
	 * @throws Exception
	 */
	public static TreeNode toTreeNode(Right right){
		if(right==null){
			return null;
		}
		TreeNode treeNode = new TreeNode();
		treeNode.setId(right.getRightId());
		treeNode.setText(right.getRightName());
		treeNode.setChecked(right.getState());
		if(!right.getIsLeaf()&&!"系统权限".equals(right.getRightName())){
			treeNode.setState(StateType.closed);
		}
		treeNode.getAttributes().put("rightUrl", right.getRightUrl());
		if(!right.getIsLeaf()){
			List<TreeNode> childrenNode = new ArrayList<TreeNode>();
			List<Right> children = right.getChildrenRightList();
			for (Right right2 : children) {
				childrenNode.add(toTreeNode(right2));
			}
			treeNode.setChildren(childrenNode);
		}
		return treeNode;
	}
	/**
	 * @Description: 将List<Right>转化成List<TreeNode>
	 * @Create: 2012-10-14 下午11:43:15
	 * @author lys
	 * @update logs
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static List<TreeNode> toTreeNodeList(List<Right> list){
		if(list==null){
			return null;
		}
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		for (Right right : list) {
			treeNodeList.add(toTreeNode(right));
		}
		return treeNodeList;
	}
	/**
	 * @Description: 将List<Right>生成JSON字符串
	 * @Create: 2012-10-14 下午11:46:04
	 * @author lys
	 * @update logs
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static String toJSONRightList(List<Right> list){
		List<TreeNode> treeNodeList = toTreeNodeList(list);
		return toJSON(treeNodeList);
	}
	
	
	public static void main(String[] args) {
		TreeNode treeNode = new TreeNode();
		treeNode.setId(1);
		
		TreeNode treeNode2 = new TreeNode();
		treeNode2.setId(2);
		
		TreeNode treeNode3 = new TreeNode();
		treeNode3.setId(3);
		
		TreeNode treeNode4 = new TreeNode();
		treeNode4.setId(4);
		
		treeNode.getChildren().add(treeNode4);
		
		treeNode3.getChildren().add(treeNode);
		treeNode3.getChildren().add(treeNode2);
		treeNode3.getAttributes().put("url", "myUrl");
		
		TreeNode treeNode5 = new TreeNode();
		treeNode5.setText("系统权限");
		
		List<TreeNode> list = new ArrayList<TreeNode>();
		list.add(treeNode);
		list.add(treeNode2);
		
		List<TreeNode> list2 = new ArrayList<TreeNode>();
		
		String result1 = TreeUtil.toJSONObject(treeNode).toString();
		System.out.println("toJSONObject: "+result1);
		
		String result2 = TreeUtil.toJSONArray(list).toString();
		System.out.println("toJSONArray: "+result2);
		
		String result3 = TreeUtil.toJSON(treeNode);
		System.out.println("toJSON(TreeNode): "+result3);
		
		String result4 = TreeUtil.toJSON(list);
		System.out.println("toJSON(List<TreeNode>) : "+result4);
		
		String result5 = TreeUtil.toJSON(treeNode3);
		System.out.println("toJSON(TreeNode) have children : "+result5);
		
		treeNode5.setChildren(list);
		String result6 = TreeUtil.toJSON(treeNode5);
		System.out.println("空节点 : "+result6);
		
		
		String result7 = TreeUtil.toJSON(list2);
		System.out.println("空List : "+result7);
	}
	/**
	 * @Description: 角色权限List转化成树节点List
	 * @Create: 2012-10-27 下午9:17:45
	 * @author lys
	 * @update logs
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static List<TreeNode> toTreeNodeListRoleRight(List<RoleRight> list) {
		if(list==null){
			return null;
		}
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		for (RoleRight roleRight : list) {
			treeNodeList.add(toTreeNode(roleRight));
		}
		return treeNodeList;
	}
	/**
	 * @Description: 将角色权限RoleRight转化成树节点TreeNode
	 * @Create: 2012-10-27 下午9:20:45
	 * @author lys
	 * @update logs
	 * @param roleRight
	 * @return
	 * @throws Exception
	 */
	private static TreeNode toTreeNode(RoleRight roleRight) {
		if(roleRight==null){
			return null;
		}
		TreeNode treeNode = new TreeNode();
		treeNode.setId(roleRight.getId().getRoleId()+"_"+roleRight.getId().getRightId());
		treeNode.setText(roleRight.getRight().getRightName());
		
		if(roleRight.getState()==1){
			treeNode.setChecked(true);
		}else{
			treeNode.setChecked(false);
		}
		if(!roleRight.getRight().getIsLeaf()){
			treeNode.setState(StateType.closed);
		}
		return treeNode;
	}
	
	//----------------校区--------------
	/**
	 * @Description: 将校区转化成树节点
	 * @Created Time: 2013-2-25 下午6:34:54
	 * @Author lys
	 * @param school
	 * @return
	 */
	public static TreeNode toTreeNode(School school){
		if(school==null){
			return null;
		}
		TreeNode treeNode = new TreeNode();
		treeNode.setId(school.getSchoolId());
		treeNode.setText(school.getSchoolName());
		if(!school.getIsLeaf()&&!"学校总部".equals(school.getSchoolName())){
			treeNode.setState(StateType.closed);
		}
		if(!school.getIsLeaf()){
			List<TreeNode> childrenNode = new ArrayList<TreeNode>();
			List<School> children = school.getChildrenSchoolList();
			if(children!=null){
				for (School school2 : children) {
					childrenNode.add(toTreeNode(school2));
				}
			}
			treeNode.setChildren(childrenNode);
		}
		return treeNode;
	}
	/**
	 * @Description: 将List<Scholl>转化成树节点
	 * @Created Time: 2013-2-25 下午6:35:25
	 * @Author lys
	 * @param children
	 * @return
	 */
	public static List<TreeNode> toTreeNodeSchoolList(List<School> list) {
		if(list==null){
			return null;
		}
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		for (School school : list) {
			treeNodeList.add(toTreeNode(school));
		}
		return treeNodeList;
	}
	
	/**
	 * @Description: 将List<School>生成JSON字符串
	 * @Create: 2012-10-14 下午11:46:04
	 * @author lys
	 * @update logs
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static String toJSONSchoolList(List<School> list){
		List<TreeNode> treeNodeList = toTreeNodeSchoolList(list);
		return toJSON(treeNodeList);
	}
}
