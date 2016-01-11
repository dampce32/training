package com.csit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.BillDetailDAO;
import com.csit.model.BillDetail;
import com.csit.service.BillDetailService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;
/**
 * @Description:消费Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-11
 * @author jcf
 * @vesion 1.0
 */
@Service
public class BillDetailServiceImpl extends BaseServiceImpl<BillDetail, Integer> implements BillDetailService {

	@Resource
	private BillDetailDAO billDetailDAO;
	
	public ServiceResult getTotalCount(BillDetail model,Date beginDate,Date endDate) {
		ServiceResult result = new ServiceResult(false);
		Long data = billDetailDAO.getTotalCount(model,beginDate,endDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	
	/* (non-Javadoc)   
	 * @see com.csit.service.PaymentService#query(com.csit.model.Payment, java.lang.Integer, java.lang.Integer)   
	 */
	public ServiceResult query(BillDetail model,Date beginDate,Date endDate, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<BillDetail> list = billDetailDAO.query(model,beginDate,endDate, page, rows);

		String[] properties = {"billDetailId","billItemName","bill.billCode","bill.billDate",
				"bill.billId","warehouse.warehouseId","warehouse.warehouseName","price","qty",
				"discount","discountAmount", "unitName","status", "payed","totalPrice",
				"bill.student.studentId","bill.student.studentName","course.courseName",
				"course.courseId","returnQty","actualQty"};

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.BillDetailService#queryDetail(com.csit.model.BillDetail)
	 */
	@Override
	public ServiceResult queryDetail(String billDetailIds) {
		ServiceResult result = new ServiceResult(false);
		if(StringUtils.isEmpty(billDetailIds)){
			result.setMessage("请选择要退货的课程或商品");
			return result;
		}
		List<BillDetail> list=new ArrayList<BillDetail>();
		String[] billDetailId = StringUtil.split(billDetailIds);
		for(int i=0;i<billDetailId.length;i++){
			BillDetail billDetail=billDetailDAO.load(Integer.parseInt(billDetailId[i]));
			list.add(billDetail);
		}
		
		String[] properties = { "billDetailId","productType","price", "qty","discount", "discountAmount", "course.courseId", "warehouse.warehouseName", "unitName", "billItemName","totalPrice","payed","actualQty"};
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}
	
}
