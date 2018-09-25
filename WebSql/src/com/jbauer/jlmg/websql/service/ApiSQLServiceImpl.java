package com.jbauer.jlmg.websql.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.jbauer.jlmg.websql.model.QueryBody;
import com.jbauer.jlmg.websql.model.QueryResults;

@Path("/api")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class ApiSQLServiceImpl implements ApiSQLService {

    @Override
    @POST
    @Path("/sql")
    public Response sql(@javax.ws.rs.core.Context HttpHeaders headers, QueryBody body) {
        QueryResults results = new QueryResults();
        Statement stmt = null;
        ArrayList<String> tableHeaders = new ArrayList<String>();
        ArrayList<List<String>> rows = new ArrayList<List<String>>();
        ArrayList<String> logs = new ArrayList<String>();
        
        
        try {
            
            String authHeader = headers.getHeaderString("authorization");
            System.out.println("Auth decoded "+authHeader);            
            String encodedValue = authHeader.split(" ")[1];
            String decodedValue = new String(Base64.getDecoder().decode(encodedValue));
            System.out.println("Auth decoded "+decodedValue);
            String user = decodedValue.split(":")[0];
            String pwd = decodedValue.split(":")[1];
            
            InitialContext ic = new InitialContext();
            Context xmlContext = (Context) ic.lookup("java:comp/env");
            Connection conn;
/*            DataSource ds = (DataSource) xmlContext.lookup("jdbc/testdb");
            
            if ((Strings.isNullOrEmpty(body.getUser()))||(Strings.isNullOrEmpty(body.getPassword()))){
                conn = ds.getConnection();
            }else{
                 conn = ds.getConnection(body.getUser(), body.getPassword());
            }
*/
            String dburl = (String) xmlContext.lookup("dburl");
            String table = (String) xmlContext.lookup("table_"+user);
            Class.forName("com.mysql.jdbc.Driver");
        
            conn = DriverManager.getConnection(dburl+table, user, pwd);
            

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
                    tableHeaders.add(metadata.getColumnName(i));      
                }
                results.setHeaders(tableHeaders);
            
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
            //logs.add(Throwables.getStackTraceAsString (ex));            
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
