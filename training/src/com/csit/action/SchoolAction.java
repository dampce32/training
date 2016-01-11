package com.csit.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.School;
import com.csit.model.User;
import com.csit.service.SchoolService;
import com.csit.util.TreeUtil;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:校区Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-25
 * @Author lys
 */
@Controller
@Scope("prototype")
public class SchoolAction extends BaseAction implements ModelDriven<School> {
	
	private static final long serialVersionUID = -5833544519223396351L;
	private static final Logger logger = Logger.getLogger(SchoolAction.class);
	@Resource
	private SchoolService schoolService;
	private School model = new School();
	@Override
	public School getModel() {
		return model;
	}
	/**
	 * @Description: 取得跟校区
	 * @Created Time: 2013-2-25 下午2:46:59
	 * @Author lys
	 */
	public void selectRoot() {
		try {
			String jsonString = schoolService.selectRoot();
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @Description: 单击选择展开树节点
	 * @Create: 2012-10-27 下午3:21:25
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void selectTreeNode(){
		List<School> children=schoolService.selectTreeNode(model);
		String jsonString = TreeUtil.toJSONSchoolList(children);
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
		String jsonArray = schoolService.query(page, rows, model);
		ajaxJson(jsonArray);
	}
	/**
	 * @Description: 批量删除校区
	 * @Create: 2012-10-27 下午12:00:30
	 * @author lys
	 * @update logs
	 * @throws Exception
	 */
	public void mulDelete(){
		ServiceResult result = new ServiceResult(false);	
		try {
			result = schoolService.mulDelete(ids);
		} catch (Throwable e) {
			result.setMessage("批量删除失败");
			logger.error("批量删除失败", e);
		}
		ajaxJson(result.toJSON());
	}
	/**
	 * @Description: 保存校区
	 * @Create: 2013-1-22 上午10:33:19
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = schoolService.save(model);
		} catch (Exception e) {
			result.setMessage("保存校区失败");
			logger.error("保存校区失败", e);
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
		String schoolId = getParameter("schoolId");
		String updateSchoolId = getParameter("updateSchoolId");
		ServiceResult result = new ServiceResult(false);
		try {
			result = schoolService.updateArray(Integer.parseInt(schoolId),Integer.parseInt(updateSchoolId));
		} catch (Exception e) {
			result.setMessage("更新排序失败");
			logger.error("更新排序失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: combobox查询所有校区
	 * @Create: 2012-12-29 下午11:18:19
	 * @author lys
	 * @update logs
	 */
	public void queryAllCombobox() {
		String jsonString = schoolService.queryAllCombobox();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: combobox查询用户所在校区
	 * @Create: 2012-12-29 下午11:18:19
	 * @author lys
	 * @update logs
	 */
	public void querySelfCombobox() {
		String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
		String jsonString = schoolService.querySelfCombobox(schoolCode);
		ajaxJson(jsonString);
	}
}
