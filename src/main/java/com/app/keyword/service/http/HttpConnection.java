package com.app.keyword.service.http;

import com.app.keyword.vo.http.HttpConnVo;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class HttpConnection {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Document getConnection(HttpConnVo httpConnVo) {
        Document doc = null;
        Connection con = null;

        if(httpConnVo == null) {
            logger.error("keywordCrawler is null");
            throw new NullPointerException();
        }

        logger.info(httpConnVo.toString());
        String url = httpConnVo.getUrl();
        String referrer = httpConnVo.getReferrer();
        Map<String, String> header = httpConnVo.getHeader();
        Map<String, String> param = httpConnVo.getParam();
        String method = httpConnVo.getMethod();
        boolean isContentType = httpConnVo.isContentType();


        if(StringUtils.isEmpty(url)) {
            logger.error("URL 값이 없습니다. : " + url);
            throw new NullPointerException();
        }

        con = Jsoup.connect(url);

        con.userAgent("Mozilla");

        if(!StringUtils.isEmpty(referrer))
            con.referrer(referrer);

        if(param != null)
            con.data(param);

        if(header != null) {
            for(String k : header.keySet()) {
                con.header(k, header.get(k));
            }
        }

        if(isContentType)
            con.ignoreContentType(true);

        try {
            doc = method.equals("GET") ? con.get() : con.post();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

}
