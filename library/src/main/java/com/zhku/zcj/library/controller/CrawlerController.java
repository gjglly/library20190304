package com.zhku.zcj.library.controller;

import com.zhku.zcj.library.service.CrawlerService;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@RestController
public class CrawlerController {


    @Resource
    CrawlerService crawlerService;

    @GetMapping("/hello")
    public String Hello(){
        Date t = new Date();
        HttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager());; //创建HttpClient
//        String url = "http://book.jd.com/booktop/0-0-0.html?category=20002-0-0-0-10001-1#comfort"; //种子
//        String url1 = "https://book.jd.com/booktop/0-0-0.html?category=3263-0-0-0-10001-10007-1#comfort"; //种子
//        String url22 = "https://book.jd.com/booktop/0-0-0.html?category=20001-0-0-0-10001-1#comfort"; //种子
//        String url223 = "https://book.jd.com/booktop/0-0-0.html?category=20003-0-0-0-10001-1#comfort"; //种子
//        String url2233 = "https://book.jd.com/booktop/0-0-0.html?category=3267-0-0-0-10001-1#comfort"; //种子
//        String url22334 = "https://book.jd.com/booktop/0-0-0.html?category=3267-0-0-0-10001-1#comfort"; //种子

//        String url223335 = "https://book.jd.com/booktop/0-0-0.html?category=20008-0-0-0-10001-1#comfort"; //种子
//        String url2233356 = "https://book.jd.com/booktop/0-0-0.html?category=4758-0-0-0-10001-1#comfort"; //种子
//        String url = "https://p.3.cn/prices/mgets?type=1&skuIds=J_11757834&callback=jQuery4548071&_=1550899552042"; //种子

        try {

            String[] jdWeekUrl = new String[5];
            String[] jdMonthUrl = new String[5];
            String[] ddWeekUrl = new String[5];
            String[] ddMonthUrl = new String[5];
            for (int i = 1;i<=5;i++) {
                jdWeekUrl[i-1] = "https://book.jd.com/booktop/0-0-0.html?category=3287-0-0-0-10002-"+i+"#comfort";
                jdMonthUrl[i-1] = "https://book.jd.com/booktop/0-0-0.html?category=3287-0-0-0-10002-"+i+"#comfort";
                ddWeekUrl[i-1] = "http://bang.dangdang.com/books/bestsellers/01.54.00.00.00.00-recent7-0-0-1-"+i;
                ddMonthUrl[i-1] = "http://bang.dangdang.com/books/bestsellers/01.54.00.00.00.00-recent30-0-0-1-"+i;
            }
            crawlerService.crawlJdBookList(client,jdWeekUrl,"week","0");
            crawlerService.crawlJdBookList(client,jdMonthUrl,"month","0");
            crawlerService.crawlJdBookList(client,ddWeekUrl,"week","1");
            crawlerService.crawlJdBookList(client,ddMonthUrl,"month","1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date t2 = new Date();
        return t+":"+t2;
    }
}
