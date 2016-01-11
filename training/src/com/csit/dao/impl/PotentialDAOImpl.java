package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.PotentialDAO;
import com.csit.model.Potential;
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
public class PotentialDAOImpl extends BaseDAOImpl<Potential, Integer> implements PotentialDAO {

	@Override
	public Long getTotalCount(Potential model) {
		Criteria criteria = getCurrentSession().createCriteria(Potential.class);
		criteria.createAlias("lastReplyUser", "lastReplyUser", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("potCourse", "potCourse", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("media", "media", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("user", "user", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("potentialStuStatus", "potentialStuStatus", CriteriaSpecification.LEFT_JOIN);
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if(model != null && model.getPotCourse() != null && model.getPotCourse().getPotCourseId() != null){
			criteria.add(Restrictions.eq("potCourse.potCourseId", model.getPotCourse().getPotCourseId()));
		}
		if(model != null && model.getPotentialName() != null ){
			criteria.add(Restrictions.like("potentialName",  model.getPotentialName(), MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Potential> query(Potential model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Potential.class);

		criteria.createAlias("lastReplyUser", "lastReplyUser", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("potCourse", "potCourse", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("media", "media", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("user", "user", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("potentialStuStatus", "potentialStuStatus", CriteriaSpecification.LEFT_JOIN);

		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if(model != null && model.getPotCourse() != null && model.getPotCourse().getPotCourseId() != null){
			criteria.add(Restrictions.eq("potCourse.potCourseId", model.getPotCourse().getPotCourseId()));
		}
		if(model != null && model.getPotentialName() != null ){
			criteria.add(Restrictions.like("potentialName",  model.getPotentialName(), MatchMode.ANYWHERE));
		}

		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.asc("potentialId"));
		return criteria.list();
	}

}
