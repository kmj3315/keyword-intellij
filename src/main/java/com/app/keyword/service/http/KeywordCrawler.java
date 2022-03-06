package com.app.keyword.service.http;

import java.util.Map;


public interface KeywordCrawler {

    public String getUrl();
    public String getReferrer();
    public Map<String, String> getHeader();
    public Map<String, String> getParam();
    public String getMethod();
    public boolean getIsContenType();






}
