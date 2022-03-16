package com.app.keyword.service.http.blog;

import com.app.keyword.repository.http.blog.BlogBoardRepository;
import com.app.keyword.repository.http.blog.BlogMainRepository;
import com.app.keyword.repository.http.blog.BlogVisitRepository;
import com.app.keyword.repository.http.search.KeywordMainRepository;
import com.app.keyword.service.http.HttpConnection;
import com.app.keyword.vo.http.HttpConnVo;
import com.app.keyword.vo.http.blog.BlogBoardVo;
import com.app.keyword.vo.http.blog.BlogMainVo;
import com.app.keyword.vo.http.blog.BlogVisitVo;
import com.app.keyword.vo.http.search.KeywordMainVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BlogService {

    @Value("${BLOG_URL}")
    String BLOG_URL;
    @Value("${BLOG_CERTKEY}")
    String BLOG_CERTKEY;
    @Value("${BLOG_CLIENT_ID}")
    String BLOG_CLIENT_ID;

    @Value("${BLOG_VISIT_URL}")
    String BLOG_VISIT_URL;

    @Value("${BLOG_SEARCH_RANGE.START}")
    int BLOG_SEARCH_RANGE_START;

    @Value("${BLOG_SEARCH_RANGE.END}")
    int BLOG_SEARCH_RANGE_END;



    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    final String method = "GET";
    final String referrer = "";
    final boolean isContenType = true;
    HttpConnVo httpConnVo;






    @Autowired
    HttpConnection httpConnection;

    @Autowired
    BlogMainRepository blogMainRepository;
    @Autowired
    BlogBoardRepository blogBoardRepository;
    @Autowired
    BlogVisitRepository blogVisitRepository;

    @Autowired
    KeywordMainRepository keywordMainRepository;




    public HttpConnVo searchBlog(String paramName){
        httpConnVo = new HttpConnVo();

        httpConnVo.setHeader(this.getBlogSetHeader());
        httpConnVo.setMethod(this.method);
        httpConnVo.setReferrer(this.referrer);
        httpConnVo.setUrl(this.BLOG_URL);
        httpConnVo.setContentType(this.isContenType);

        Map<String, String> param = new HashMap<String, String>();
        param.put("query", paramName);
        httpConnVo.setParam(param);

        return httpConnVo;
    }

    public HttpConnVo searchVisit(String blogId){
        httpConnVo = new HttpConnVo();
        httpConnVo.setMethod(this.method);
        httpConnVo.setReferrer(this.referrer);
        httpConnVo.setUrl(this.BLOG_VISIT_URL);
        httpConnVo.setContentType(this.isContenType);

        Map<String, String> param = new HashMap<String, String>();
        param.put("blogId", blogId);
        httpConnVo.setParam(param);

        return httpConnVo;
    }

    public Map<String, Object> blogParser(Document doc){

        Map<String, Object> result = new HashMap<>();
        List<BlogMainVo> mList = new ArrayList<>();
        String html = doc.select("body ").text();
        HashMap<String, Object> rs = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        List<BlogBoardVo> bbList = new ArrayList<>();
        try {
            rs = new ObjectMapper().readValue(html, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        int totCnt = (int) rs.get("total");
        BlogMainVo bm = BlogMainVo.builder()
                .totCnt(totCnt)
                .build();

        List<Map<String, String>> itemList = (List<Map<String, String>>) rs.get("items");

        for(Map<String, String> map : itemList){
            String title = map.get("title");
            String link = map.get("link");;
            String bloggerName = map.get("bloggername");;
            String bloggerLink = map.get("bloggerlink");;
            Date postDate = null;
            try {
                postDate = format.parse(map.get("postdate"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String[] linkSplit = bloggerLink.split("/");

            String id = linkSplit[linkSplit.length-1];

            BlogBoardVo bb = BlogBoardVo.builder()
                    .blogId(id)
                    .blogVisitAvg(0)
                    .blogLink(link)
                    .blogName(bloggerName)
                    .postDate(postDate)
                    .blogTitle(title)
                    .blogMainVo(bm)
                    .build();

            bbList.add(bb);
        }


        result.put("blogMain", bm);
        result.put("blogBoard", bbList);



        return result;

    }

    public List<BlogVisitVo> visitParser(Document doc, BlogBoardVo blogBoardVo) {
        List<BlogVisitVo> vList = new ArrayList<>();
        Elements es = doc.select("visitorcnt");
        logger.info(es.toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String today = format.format(new Date());

        for(Element e : es){
            logger.info(e.toString());

            String date = e.attr("id");
            int cnt = Integer.parseInt(e.attr("cnt"));

            if(today.equals(date)){
                continue;
            }

            Date d1 = null;
            try {
                d1 = format.parse(date);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            BlogVisitVo bv = BlogVisitVo.builder()
                    .visitDate(d1)
                    .visitCnt(cnt)
                    .blogBoardVo(blogBoardVo)
                    .build();
            vList.add(bv);
        }

        return vList;
    }

    public Map<String, Object> blogXmlParser(Document doc){

        Map<String, Object> result = new HashMap<>();
        List<BlogMainVo> mList = new ArrayList<>();
        String html = doc.select("body ").text();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        List<BlogBoardVo> bbList = new ArrayList<>();
        int totCnt = Integer.parseInt(doc.select("total").text());
        BlogMainVo bm = BlogMainVo.builder()
                .totCnt(totCnt)
                .build();
        Elements items = doc.select("item");
        for(Element item : items){
            String title = item.select("title").text();
            String link = item.select("link").text();
            String bloggerName = item.select("bloggername").text();
            String bloggerLink = item.select("bloggerlink").text();
            Date postDate = null;
            try {
                postDate = format.parse(item.select("postdate").text());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String id = "";
            if(bloggerLink.indexOf("https://blog.naver.com") > -1){
                String[] linkSplit = bloggerLink.split("/");
                id = linkSplit[linkSplit.length-1];
            }

            logger.info("bloggerlink :" + bloggerLink);
            logger.info("blogid : " + id);
            BlogBoardVo bb = BlogBoardVo.builder()
                    .blogId(id)
                    .blogVisitAvg(0)
                    .blogLink(link)
                    .blogName(bloggerName)
                    .postDate(postDate)
                    .blogTitle(title)
                    .blogMainVo(bm)
                    .build();

            bbList.add(bb);
        }


        result.put("blogMain", bm);
        result.put("blogBoard", bbList);



        return result;

    }


    public Map<String, String> getBlogSetHeader(){
        Map<String, String> header = new HashMap<String,String>();
        header.put("X-Naver-Client-Id", BLOG_CLIENT_ID);
        header.put("X-Naver-Client-Secret", BLOG_CERTKEY);
        //header.put("Content-Type", "application/json");
        header.put("Content-Type", "text/xml");
        return header;
    }


    public KeywordMainVo findJob(){

        KeywordMainVo keywordMainVo = keywordMainRepository.findFirstByTotCntBetweenAndBlogMains_TotCntIsNullOrderBySearchTimeAsc(BLOG_SEARCH_RANGE_START, BLOG_SEARCH_RANGE_END);
        return keywordMainVo;
    }

    public int calcVisitAvg(List<BlogVisitVo> vList){
        int total = 0;
        for(BlogVisitVo bv: vList ){
            total += bv.getVisitCnt();
        }
        int result = 0;
        if(total > 0){
            result = total / vList.size();
        }
        return result;

    }

    public void blogProcess() throws InterruptedException {
        while(true){
            // 1.조회할 블로그를 찾아 온다.
            KeywordMainVo km = findJob();

            if(km == null){
                Thread.sleep(1000 * 60);
                continue;
            }

            // 2. 블로그 조회한다.
            HttpConnVo httpConnVo = searchBlog(km.getKeywordNm());

            //3. 블로그 http 조회한다.
            Document document = httpConnection.getConnection(httpConnVo);

            //4. 조회정보를 parser 한다.
            Map<String, Object> blogParser = blogXmlParser(document);
            BlogMainVo bm = (BlogMainVo) blogParser.get("blogMain");
            bm.setKeywordMainVo(km);
            blogMainRepository.save(bm);
            List<BlogBoardVo> bbList = (List<BlogBoardVo>) blogParser.get("blogBoard");
            //5. 게시글들의 상태를 조회 한다.
            for(BlogBoardVo bb : bbList){
                if(bb.getBlogId() == null || "".equals(bb.getBlogId()) )
                    continue;

                logger.info(bb.toString());
                HttpConnVo visitHttpVo = searchVisit(bb.getBlogId());
                Document doc = httpConnection.getConnection(visitHttpVo);
                List<BlogVisitVo> visitList = visitParser(doc, bb);
                int avg = calcVisitAvg(visitList);
                bb.setBlogVisitAvg(avg);
                blogBoardRepository.save(bb);
                blogVisitRepository.saveAll(visitList);
            }
            Thread.sleep(1000);
        }

    }
}
