package com.app.keyword.service.http.search;

import com.app.keyword.repository.http.search.KeywordMainRepository;
import com.app.keyword.repository.http.search.KeywordRelationRepository;
import com.app.keyword.repository.http.search.KeywordSubRepository;
import com.app.keyword.service.http.HttpConnection;
import com.app.keyword.vo.http.HttpConnVo;
import com.app.keyword.vo.http.search.KeywordMainVo;
import com.app.keyword.vo.http.search.KeywordRelationVo;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private KeywordRelationRepository keywordRelationRepository;

    @Autowired
    private HttpConnection httpConnection;



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
        List<KeywordRelationVo> keywordRelList = new ArrayList<KeywordRelationVo>();

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
                KeywordRelationVo keywordRelationVo = KeywordRelationVo.builder()
                        .keywordNm(keywordNm)
                        .pcCnt(pcCnt)
                        .mbCnt(mbCnt)
                        .regYn("N")
                        .build();

                keywordRelList.add(keywordRelationVo);
            }
        }

        result.put("keywordRelList", keywordRelList);
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

    @Transactional
    public void insertResult2(KeywordMainVo keywordM, List<KeywordRelationVo> keywordRelList){
        keywordMainRepository.save(keywordM);

        for(KeywordRelationVo kr : keywordRelList){
            KeywordRelationVo KeyRel = keywordRelationRepository.findByKeywordNm(kr.getKeywordNm());
            if(KeyRel == null){
                keywordRelationRepository.save(kr);
            }
        }
    }

    public List<KeywordMainVo> checkInsert(String keyword){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        Date d1 = cal.getTime();
        Date d2 = new Date();
        List<KeywordMainVo> keywordMains =keywordMainRepository.findByKeywordNmAndSearchDtBetween(keyword, d1, d2);

        return keywordMains;

    }


    public KeywordRelationVo findJob(){
        return keywordRelationRepository.findFirstByRegYnOrderByCreateDtAsc("N");
    }

    public void saveKeywordSub(KeywordSubVo keywordSubVo){
        keywordSubRepository.save(keywordSubVo);
    }

    @Transactional
    public void savekeywordRelation(KeywordRelationVo keywordRelationVo){

        KeywordRelationVo byKeyword = keywordRelationRepository.findByKeywordNm(keywordRelationVo.getKeywordNm());
        Date d = new Date();

        byKeyword.setMbCnt(keywordRelationVo.getMbCnt());
        byKeyword.setPcCnt(keywordRelationVo.getPcCnt());
        byKeyword.setRegYn(keywordRelationVo.getRegYn());
        byKeyword.setSearchDt(d);
        byKeyword.setSearchTime(d);

    }


    public void keywordSearchProcess() throws InterruptedException {
        logger.info("start job");
        int jobnum = 1;

        while (true){
            logger.info("["+jobnum++ + "] start =======================================================");
            KeywordRelationVo job = findJob();
            logger.info(job.toString());

            //조회 할 내용 셋팅
            HttpConnVo httpConnVo = search(job.getKeywordNm());
            //연결 값
            Document doc = httpConnection.getConnection(httpConnVo);
            Map<String, Object> htmlParser = htmlParser(doc);
            //메인키워드값
            KeywordMainVo keywordM = (KeywordMainVo) htmlParser.get("keywordMain");
            //릴레이션 키워드
            List<KeywordRelationVo> keywordSubList = (List<KeywordRelationVo>) htmlParser.get("keywordRelList");

            insertResult2(keywordM, keywordSubList);

            job.setRegYn("Y");
            savekeywordRelation(job);
            logger.info("["+jobnum + "] end =======================================================");

            Thread.sleep(1000);
        }

    }

}

