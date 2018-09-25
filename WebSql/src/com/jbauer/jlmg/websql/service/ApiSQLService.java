package com.jbauer.jlmg.websql.service;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import com.jbauer.jlmg.websql.model.QueryBody;

public interface ApiSQLService {

	public Response sql(@Context HttpHeaders headers,QueryBody body);
	
}
