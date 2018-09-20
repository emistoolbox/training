package com.jbauer.jlmg.websql.service;

import javax.ws.rs.core.Response;

import com.jbauer.jlmg.websql.model.QueryBody;

public interface ApiSQLService {

	public Response sql(QueryBody body);
	
}
