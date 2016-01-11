package com.csit.dao;

import java.util.List;

import com.csit.model.School;
/**
 * @Description:校区DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-25
 * @Author lys
 */
public interface SchoolDAO extends BaseDAO<School,Integer>{
	/**
	 * @Description: 取得跟校区
	 * @Created Time: 2013-2-25 下午2:49:43
	 * @Author lys
	 * @return
	 */
	School selectRoot();
	/**
	 * @Description: 取得校区下的子校区
	 * @Created Time: 2013-2-25 下午3:06:19
	 * @Author lys
	 * @param root
	 * @return
	 */
	List<School> getChildren(School root);
	/**
	 * @Description: 分页查询校区
	 * @Created Time: 2013-2-25 下午6:46:14
	 * @Author lys
	 * @param page
	 * @param rows
	 * @param model
	 * @return
	 */
	List<School> query(Integer page, Integer rows, School model);
	/**
	 * @Description: 统计校区
	 * @Created Time: 2013-2-25 下午6:46:39
	 * @Author lys
	 * @param model
	 * @return
	 */
	Long count(School model);
	/**
	 * @Description: 取得新编号
	 * @Created Time: 2013-2-26 上午8:56:07
	 * @Author lys
	 * @param schoolCode
	 * @return
	 */
	String getNewCode(String schoolCode);
	/**
	 * @Description:取得该父校区下的校区排序最大值 
	 * @Created Time: 2013-2-26 下午2:10:07
	 * @Author lys
	 * @param schoolId
	 * @return
	 */
	Integer getMaxArray(Integer schoolId);
	/**
	 * @Description: 统计父亲节点下的孩子节点个数
	 * @Created Time: 2013-2-26 下午2:31:15
	 * @Author lys
	 * @param parentID
	 * @return
	 */
	Long countChildren(Integer parentID);
	/**
	 * @Description: 
	 * @Created Time: 2013-2-26 下午2:31:44
	 * @Author lys
	 * @param parentID
	 * @param b
	 */
	void updateIsLeaf(Integer schoolId, boolean isLeaf);
	/**
	 * @Description: combobox查询用户所在校区
	 * @Created Time: 2013-2-28 上午9:30:17
	 * @Author lys
	 * @param schoolCode
	 * @return
	 */
	List<School> querySelfCombobox(String schoolCode);
	/**
	 * @Description: combobox查询所有校区
	 * @Created Time: 2013-2-28 上午11:01:39
	 * @Author lys
	 * @return
	 */
	List<School> queryAllCombobox();

}
