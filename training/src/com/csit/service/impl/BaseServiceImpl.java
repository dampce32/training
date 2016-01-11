package com.csit.service.impl;


import java.io.Serializable;

import com.csit.dao.BaseDAO;
import com.csit.service.BaseService;

public class BaseServiceImpl<T,PK extends Serializable> implements BaseService<T,PK>{

	private BaseDAO<T,PK> baseDAO;
	
	@SuppressWarnings("rawtypes")
	public BaseDAO getBaseDAO() {
		return baseDAO;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

}
