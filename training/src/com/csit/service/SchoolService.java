package com.csit.service;

import java.util.List;

import com.csit.model.School;
import com.csit.vo.ServiceResult;
/**
 * @Description:校区Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-25
 * @Author lys
 */
public interface SchoolService extends BaseService<School,Integer>{
	/**
	 * @Description: 取得跟校区
	 * @Created Time: 2013-2-25 下午2:47:31
	 * @Author lys
	 * @return
	 */
	String selectRoot();
	/**
	 * @Description: 单击选择展开树节点
	 * @Created Time: 2013-2-25 下午6:42:08
	 * @Author lys
	 * @param model
	 * @return
	 */
	List<School> selectTreeNode(School model);
	/**
	 * @Description: 取得树节点下的孩子节点
	 * @Created Time: 2013-2-25 下午6:44:04
	 * @Author lys
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	String query(Integer page, Integer rows, School model);
	/**
	 * @Description: 保存校区
	 * @Created Time: 2013-2-26 上午8:44:51
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult save(School model);
	/**
	 * @Description: 批量删除校区
	 * @Created Time: 2013-2-26 上午8:45:29
	 * @Author lys
	 * @param ids
	 * @return
	 */
	ServiceResult mulDelete(String ids);
	/**
	 * @Description: 更新校区顺序
	 * @Created Time: 2013-2-26 上午8:46:21
	 * @Author lys
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 */
	ServiceResult updateArray(Integer schoolId, Integer updateSchoolId);
	/**
	 * @Description: combobox查询所有校区
	 * @Created Time: 2013-2-28 上午9:27:55
	 * @Author lys
	 * @return
	 */
	String queryAllCombobox();
	/**
	 * @Description: combobox查询用户所在校区
	 * @Created Time: 2013-2-28 上午9:28:19
	 * @Author lys
	 * @param schoolCode
	 * @return
	 */
	String querySelfCombobox(String schoolCode);

}
