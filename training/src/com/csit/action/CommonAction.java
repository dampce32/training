package com.csit.action;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.util.JSONUtil;

/**
 * @Description:可全局调用的action
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-12-30
 * @author lys
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class CommonAction extends BaseAction {

	private static final long serialVersionUID = -7485373143593963699L;
//	private static final Logger logger = Logger.getLogger(CommonAction.class);
	/**
	 * @Description: 清空datagrid
	 * @Create: 2012-12-30 下午5:07:53
	 * @author lys
	 * @update logs
	 */
	public void clearDatagrid(){
		ajaxJson(JSONUtil.EMPTYJSON);
	}
	/**
	 * @Description: 清空combobox
	 * @Created Time: 2013-3-8 上午10:13:22
	 * @Author lys
	 */
	public void clearCombobx(){
		ajaxJson(JSONUtil.EMPTY_COMBOBOX_JSON);
	}
	
}
