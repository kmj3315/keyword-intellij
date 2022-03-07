package com.app.keyword.service.http;

import com.app.keyword.vo.http.HttpConnVo;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ToString
@Component
public class SearchCrawlerImpl {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    HttpConnVo httpConnVo;


    public void setup(){
        httpConnVo = new HttpConnVo();
        httpConnVo.setHeader(this.getHeader());
        httpConnVo.setMethod(this.method);
        httpConnVo.setReferrer(this.referrer);
        httpConnVo.setUrl(this.url);
        httpConnVo.setContentType(this.isContenType);
    }

    @Value("${SEARCH_URL}")
    private String url;
    private String referrer="";
    private Map<String, String> header;

    @Value("${SEARCH_CERTKEY}")
    private String SEARCH_CERTKEY;
    @Value("${SEARCH_LICENSE}")
    private String SEARCH_LICENSE;
    @Value("${SEARCH_CUSTOMER_ID}")
    private String SEARCH_CUSTOMER_ID;

    private String method = "GET";

    boolean isContenType = true;

    private String context = "/keywordstool";


    public HttpConnVo search(String paramName){
        Map<String, String> param = new HashMap<String, String>();
        param.put("hintKeywords", paramName);
        httpConnVo.setParam(param);
        return httpConnVo;
    }

    public Map<String, String> getHeader() {

        String timeStamp = String.valueOf(new Date().getTime());
        String text = timeStamp + "." + method + "." + context;
        String signature = base64sha256(text,SEARCH_CERTKEY);

        header = new HashMap<String, String>();
        header.put("X-Timestamp", timeStamp);
        header.put("X-API-KEY", SEARCH_LICENSE);
        header.put("X-Customer", SEARCH_CUSTOMER_ID);
        header.put("X-Signature", signature);
        header.put("Content-Type", "application/json");

        return header;
    }




    public String base64sha256(String message, String secretKey) {
        String result = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] bt = sha256_HMAC.doFinal(message.getBytes());
            Base64.Encoder encoder = Base64.getEncoder();
            String hash = encoder.encodeToString(bt);
            result = hash;
        }
        catch (Exception e){
            logger.error("!! base64sha256 Error !!");
        }
        return result;
    }
}

