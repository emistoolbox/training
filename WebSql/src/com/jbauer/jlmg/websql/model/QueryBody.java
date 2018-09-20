package com.jbauer.jlmg.websql.model;

import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryBody {

    private String user = null;
    private String password = null;
    private String sql = null;
    

    @JsonProperty("user")
    @NotNull    
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }


    @JsonProperty("password")
    @NotNull    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    

    @JsonProperty("sql")
    @NotNull    
    public String getSQL() {
        return sql;
    }
    public void setSQL(String sql) {
        this.sql = sql;
    }
    
    
    
    
}
