package com.app.keyword.service.http.search;

import com.app.keyword.repository.http.search.KeywordMainRepository;
import com.app.keyword.repository.http.search.KeywordSubRepository;
import com.app.keyword.vo.http.HttpConnVo;
import com.app.keyword.vo.http.search.KeywordMainVo;
import com.app.keyword.vo.http.search.KeywordSubVo;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@ToString
@Component
public class KeywordSearchService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    HttpConnVo httpConnVo;

    @Autowired
    private KeywordMainRepository keywordMainRepository;

    @Autowired
    private KeywordSubRepository keywordSubRepository;


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

    @Value("${INSERT_TERM}")
    private int insertTerm;

    private String method = "GET";

    boolean isContenType = true;

    private String context = "/keywordstool";


    public HttpConnVo search(String paramName){
        httpConnVo = new HttpConnVo();
        httpConnVo.setHeader(this.getHeader());
        httpConnVo.setMethod(this.method);
        httpConnVo.setReferrer(this.referrer);
        httpConnVo.setUrl(this.url);
        httpConnVo.setContentType(this.isContenType);

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


    public Map<String, Object> htmlParser(Document doc) {
        String html = doc.select("body").text();

        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = new JSONObject(html);
        JSONArray jsonArray = jsonObject.getJSONArray("keywordList");

        String keywordNm;
        int pcCnt;
        int mbCnt;

        KeywordMainVo km = null;
        List<KeywordSubVo> keywordSubList = new ArrayList<KeywordSubVo>();



        for (int i = 0; i < jsonArray.length(); i++) {
            logger.debug(String.valueOf(jsonArray.getJSONObject(i)));
            JSONObject o = jsonArray.getJSONObject(i);
            keywordNm = (String) o.get("relKeyword");

            String pcCnt_1 =  o.get("monthlyPcQcCnt").toString();
            String mbCnt_1 = o.get("monthlyMobileQcCnt").toString();

            pcCnt_1 = pcCnt_1.replaceAll("<", "").trim();
            mbCnt_1 = mbCnt_1.replaceAll("<", "").trim();

            pcCnt = Integer.parseInt(pcCnt_1);
            mbCnt = Integer.parseInt(mbCnt_1);
            if(i == 0){
                km = KeywordMainVo.builder()
                        .keywordNm(keywordNm)
                        .pcCnt(pcCnt)
                        .mbCnt(mbCnt)
                        .build();
            }else{
                KeywordSubVo keywordSubVo = KeywordSubVo.builder()
                        .keywordNm(keywordNm)
                        .pcCnt(pcCnt)
                        .mbCnt(mbCnt)
                        .build();

                keywordSubList.add(keywordSubVo);
            }
        }

        result.put("keywordSubList", keywordSubList);
        result.put("keywordMain", km);

        return result;
    }


    public void insertResult(KeywordMainVo keywordM, List<KeywordSubVo> keywordSubList){
        keywordMainRepository.save(keywordM);

        for(KeywordSubVo keywordSubVo :  keywordSubList){
            keywordSubVo.setKeywordMainSqno(keywordM.getKeywordMainSqno());
        }
        keywordSubRepository.saveAll(keywordSubList);
    }

    public List<KeywordMainVo> checkInsert(String keyword){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        Date d1 = cal.getTime();
        Date d2 = new Date();
        List<KeywordMainVo> keywordMains =keywordMainRepository.findByKeywordNmAndSearchDtBetween(keyword, d1, d2);

        return keywordMains;

    }


    public KeywordSubVo findJob(){
        return keywordSubRepository.findFirstByRegYnOrderBySearchTimeAsc("N");
    }

    public void saveKeywordSub(KeywordSubVo keywordSubVo){
        keywordSubRepository.save(keywordSubVo);
    }
}

