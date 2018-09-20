package com.jbauer.jlmg.websql.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.jbauer.jlmg.websql.model.QueryBody;
import com.jbauer.jlmg.websql.model.QueryResults;

@Path("/api")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class ApiSQLServiceImpl implements ApiSQLService {

    @Override
    @POST
    @Path("/sql")
    public Response sql(QueryBody body) {
        QueryResults results = new QueryResults();
        Statement stmt = null;
        ArrayList<String> headers = new ArrayList<String>();
        ArrayList<List<String>> rows = new ArrayList<List<String>>();
        ArrayList<String> logs = new ArrayList<String>();
        
        try {
            InitialContext ic = new InitialContext();
            Context xmlContext = (Context) ic.lookup("java:comp/env");
            DataSource ds = (DataSource) xmlContext.lookup("jdbc/testdb");
            Connection conn;
            if ((Strings.isNullOrEmpty(body.getUser()))||(Strings.isNullOrEmpty(body.getPassword()))){
                conn = ds.getConnection();
            }else{
                 conn = ds.getConnection(body.getUser(), body.getPassword());
            }
                
            

            stmt = conn.createStatement();
            
            
            String sql = body.getSQL();
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            if (stmt.getUpdateCount()>0)
                logs.add("number of rows affected = "+ stmt.getUpdateCount());
            SQLWarning warning = stmt.getWarnings();
            while (warning!=null){
                logs.add(warning.getErrorCode()+" "+warning.getMessage());
                warning = warning.getNextWarning();
            }
            
            if (rs != null){
                ResultSetMetaData metadata = rs.getMetaData();
                int columnCount = metadata.getColumnCount();    
                for (int i = 1; i <= columnCount; i++) {
                    headers.add(metadata.getColumnName(i));      
                }
                results.setHeaders(headers);
            
                while (rs.next()) {
                    ArrayList<String> l= new ArrayList<String>();
                    for (int i = 1; i <= columnCount; i++) {
                        l.add(rs.getString(i));
                    }
                    rows.add(l);
                }
                results.setRows(rows);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            results.setErrorMessage(ex.getMessage());            
            logs.add(Throwables.getStackTraceAsString (ex));            
            results.setLog(logs);
            return Response.ok(results).build();
        } finally {
            if (stmt != null) {
                try{ stmt.close();}catch(Exception e){};
            }
        }
        results.setLog(logs);
        return Response.ok(results).build();
    }


}
