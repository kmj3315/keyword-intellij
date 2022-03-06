package com.app.keyword.vo.http;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Data
public class HttpConnVo {

    private String url;
    private String referrer;
    private Map<String, String> header;
    private Map<String, String> param;
    private String method;
    boolean isContentType;
}
