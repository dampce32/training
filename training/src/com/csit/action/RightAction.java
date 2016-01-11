package com.csit.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Right;
import com.csit.service.RightService;
import com.csit.util.TreeUtil;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description: 系统权限Action
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-14
 * @author lys
 * @vesion 1.0
 */
@Controller("rightAction")
@Scope("prototype")
public class RightAction extends BaseAction implements ModelDriven<Right> {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RightAction.class);
	private Right model = new Right();

	@Resource
	private RightService rightService;

	public Right getModel() {
		return model;
	}
	/**
	 * @Description: 选择权限的跟节点
	 * @Create: 2012-10-14 下午10:24:49
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void selectRoot() {
		String jsonString = rightService.selectRoot();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 单击选择展开树节点
	 * @Create: 2012-10-27 下午3:21:25
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void selectTreeNode(){
		List<Right> children=rightService.selectTreeNode(model);
		String jsonString = TreeUtil.toJSONRightList(children);
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 添加权限
	 * @Create: 2012-10-26 下午10:48:00
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void add(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = rightService.add(model);
		} catch (Exception e) {
			result.setMessage("添加权限失败");
			logger.error("添加权限失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 取得树节点下的孩子节点
	 * @Create: 2012-10-27 上午9:46:10
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void getTreeNodeChildren(){
		String jsonArray = rightService.query(page, rows, model);
		ajaxJson(jsonArray);
	}
	/**
	 * @Description: 更新权限
	 * @Create: 2012-10-27 上午11:22:47
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void update() {
		ServiceResult result = new ServiceResult(false);	
		try {
			result = rightService.update(model);
		} catch (Exception e) {
			result.setMessage("修改系统权限出错失败");
			logger.error("修改系统权限出错失败", e);
		}
		ajaxJson(result.toJSON());
	}
	/**
	 * @Description: 批量删除
	 * @Create: 2012-10-27 下午12:00:30
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void mulDelete(){
		ServiceResult result = new ServiceResult(false);	
		try {
			result = rightService.mulDelete(ids);
		} catch (Throwable e) {
			result.setMessage("批量删除失败");
			logger.error("批量删除失败", e);
		}
		ajaxJson(result.toJSON());
	}
	/**
	 * @Description: 保存权限
	 * @Create: 2013-1-22 上午10:33:19
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = rightService.save(model);
		} catch (Exception e) {
			result.setMessage("保存权限失败");
			logger.error("保存权限失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 更新排序
	 * @Create: 2013-1-29 上午9:41:52
	 * @author lys
	 * @update logs
	 */
	public void updateArray(){
		String rightId = getParameter("rightId");
		String updateRightId = getParameter("updateRightId");
		ServiceResult result = new ServiceResult(false);
		try {
			result = rightService.updateArray(Integer.parseInt(rightId),Integer.parseInt(updateRightId));
		} catch (Exception e) {
			result.setMessage("更新排序失败");
			logger.error("更新排序失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
