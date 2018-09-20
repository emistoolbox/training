package com.jbauer.jlmg.websql.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryResults {

    private List<String> headers = new ArrayList<String>();
    private List<List<String>> rows = new ArrayList<List<String>>();
    private List<String> log = new ArrayList<String>();
    private String errorMessage = "";
    
    @JsonProperty("log")    
    public List<String> getLog() {
        return log;
    }
    public void setLog(List<String> log) {
        this.log = log;
    }
    
    @JsonProperty("errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    @JsonProperty("headers")
    public List<String> getHeaders() {
        return headers;
    }
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }
    
    
    @JsonProperty("rows")
    public List<List<String>> getRows() {
        return rows;
    }
    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }
    
    
}
