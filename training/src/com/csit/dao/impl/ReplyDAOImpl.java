package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ReplyDAO;
import com.csit.model.Reply;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description:媒体Dao实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
@Repository
public class ReplyDAOImpl extends BaseDAOImpl<Reply, Integer> implements ReplyDAO {

	@Override
	public Long getTotalCount(Reply model) {
		Criteria criteria = getCurrentSession().createCriteria(Reply.class);

		criteria.createAlias("potential", "potential", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("potentialStuStatus", "potentialStuStatus", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("user", "user", CriteriaSpecification.LEFT_JOIN);
		if (model != null && model.getPotential().getPotentialId()!=null) {
			criteria.add(Restrictions.eq("potential.potentialId", model.getPotential().getPotentialId()));
		}
		if (model != null && model.getPotentialStuStatus()!=null&&model.getPotentialStuStatus().getPotentialStuStatusId()!=null) {
			criteria.add(Restrictions.eq("potentialStuStatus.potentialStuStatusId", model.getPotentialStuStatus().getPotentialStuStatusId()));
		}
		if(model!=null&&model.getReplyDate()!=null){
			criteria.add(Restrictions.eq("replyDate", model.getReplyDate()));
		}
		if(model!=null&&model.getNextReplyDate()!=null){
			criteria.add(Restrictions.eq("nextReplyDate", model.getNextReplyDate()));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reply> query(Reply model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Reply.class);

		criteria.createAlias("potential", "potential", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("potentialStuStatus", "potentialStuStatus", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("user", "user", CriteriaSpecification.LEFT_JOIN);
		if (model != null && model.getPotential().getPotentialId()!=null) {
			criteria.add(Restrictions.eq("potential.potentialId", model.getPotential().getPotentialId()));
		}
		if (model != null && model.getPotentialStuStatus()!=null&&model.getPotentialStuStatus().getPotentialStuStatusId()!=null) {
			criteria.add(Restrictions.eq("potentialStuStatus.potentialStuStatusId", model.getPotentialStuStatus().getPotentialStuStatusId()));
		}
		if(model!=null&&model.getReplyDate()!=null){
			criteria.add(Restrictions.eq("replyDate", model.getReplyDate()));
		}
		if(model!=null&&model.getNextReplyDate()!=null){
			criteria.add(Restrictions.eq("nextReplyDate", model.getNextReplyDate()));
		}

		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.asc("replyId"));
		return criteria.list();
	}

	@Override
	public Integer MaxId(Integer potentialId) {
		
		Criteria criteria = getCurrentSession().createCriteria(Reply.class);
		criteria.add(Restrictions.eq("potential.potentialId", potentialId));
		Integer maxReplyId=(Integer) criteria.setProjection(Projections.max("replyId")).uniqueResult();
		return maxReplyId;
	}

}
