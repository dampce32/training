package com.csit.util;

import com.csit.vo.GobelConstants;
/**
 * @Description:分页工具类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-25
 * @Author lys
 */
public class PageUtil {
	/**
	 * @Description: 取得分页的开始记录
	 * @Created Time: 2013-2-25 下午6:49:44
	 * @Author lys
	 * @param page
	 * @param rows
	 * @return
	 */
	public static Integer getPageBegin(Integer page, Integer rows){
		if(page==null||page<1){
			page = 1;
		}
		if(rows==null||rows<1){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		return (page-1)*rows;
	}
}
