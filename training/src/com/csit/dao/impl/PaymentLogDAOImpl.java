package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.PaymentLogDAO;
import com.csit.model.PaymentLog;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:支付宝日志Dao实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-7
 * @author jcf
 * @vesion 1.0
 */
@Repository
public class PaymentLogDAOImpl extends BaseDAOImpl<PaymentLog, Integer> implements PaymentLogDAO {

	@Override
	public Long getTotalCount(PaymentLog model) {
		Criteria criteria = getCurrentSession().createCriteria(PaymentLog.class);

		if(model != null && model.getStudent()!=null && model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student.studentId", model.getStudent().getStudentId()));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentLog> query(PaymentLog model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(PaymentLog.class);

		if(model != null && model.getStudent()!=null && model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student.studentId", model.getStudent().getStudentId()));
		}
		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.asc("paymentLogId"));
		return criteria.list();
	}

}
