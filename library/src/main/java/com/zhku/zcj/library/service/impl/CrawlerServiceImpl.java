package com.zhku.zcj.library.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhku.zcj.library.mapper.BookInfoMapper;
import com.zhku.zcj.library.model.BookInfo;
import com.zhku.zcj.library.service.CrawlerService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.awt.print.Book;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CrawlerServiceImpl implements CrawlerService {

    @Resource
    BookInfoMapper bookMapper;

    @Override
    public void crawlJdBookList(HttpClient httpClient, String[] url,String sortType,String resourceType) throws IOException {

        //先删除部分数据避免主键重复
        bookMapper.deleteBySortTypeAndResource(sortType,resourceType);
        //用来接收解析的数据
        List<BookInfo> bookData = new ArrayList<BookInfo>();
        try {
            for (int i = 0;i<url.length;i++) {
                if ("0".equals(resourceType)) {
                    bookData = jdUrlParser(httpClient,url[i],sortType);
                }else if ("1".equals(resourceType)) {
                    bookData = ddUrlParser(httpClient,url[i],sortType);
                }

                List<BookInfo> list = new ArrayList<>();
                if ("1".equals(resourceType)) {
                    list = bookMapper.selectBookListBySortTypeAndResource(sortType,"0");
                }
                for (BookInfo book : bookData) {
                    boolean insertOrNot = true;
                    for (BookInfo bookInfo:list) {
                        if (book.getIsbn().equals(bookInfo.getIsbn())) {
                            bookInfo.setSortNum((book.getSortNum()+bookInfo.getSortNum()/2));
                            bookMapper.updateByPrimaryKey(bookInfo);
                            insertOrNot = false;
                            break;
                        }
                    }
                    if (insertOrNot) {
                        bookMapper.insert(book);
                    }
                }

                System.out.println("finsih " + sortType + resourceType + i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<BookInfo> getJdData(HttpClient httpClient,String html,String sortType) throws Exception {
        List<BookInfo> datas = new ArrayList<BookInfo>();
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("ul[class=clearfix]").select("li[class]");
        String typeStr = doc.select("div[class=w]").select("div[class=breadcrumb]").select("span").html();
        String type = typeStr.split(" ")[1];
        for (Element element : elements) {
            String sortNum = element.select("div[class=p-num]").html();
            String coverImg = element.select("div[class=p-img]").select("a").select("img").attr("src");
            String detailURL = element.select("div[class=p-img]").select("a").attr("href");
            String author = element.select("div[class=p-detail]").select("dl").eq(0).select("dd").select("a").html();
            String bookName = element.select("div[class=p-detail]").select("a").attr("title");
            String publisher = element.select("div[class=p-detail]").select("dl").eq(1).select("dd").select("a").html();
            String priceURL = element.select("div[class=p-detail]").select("dl").eq(2).select("dd").select("del").attr("data-price-id");

            String publishTime="";
            String ISBN="";
            String id="";
            String summary="";
            String authorBrief="";
            Double price= 0.0;

            if (priceURL!="" && priceURL!=null) {
                priceURL = "https://p.3.cn/prices/mgets?type=1&skuIds=J_"+priceURL;
                HttpResponse response = paresUrl(httpClient, priceURL);
                //获取响应状态码
                int StatusCode = response.getStatusLine().getStatusCode();
                //如果状态响应码为200，则获取html实体内容或者json文件
                if(StatusCode == 200) {
                    String entity = EntityUtils.toString(response.getEntity(), "utf-8");
                    Document docDetail = Jsoup.parse(entity);
                    String jsonStr = docDetail.select("body").html();
                    JSONArray jsonObject = JSONObject.parseArray(jsonStr);
                    String priceStr = jsonObject.getJSONObject(0).getString("m");
                    price = Double.parseDouble(priceStr);
                }
            }

            //爬取详情页的内容
            if (detailURL!=null && detailURL!="") {
                detailURL = "http:"+detailURL;
                HttpResponse response = paresUrl(httpClient, detailURL);
                //获取响应状态码
                int StatusCode = response.getStatusLine().getStatusCode();
                //如果状态响应码为200，则获取html实体内容或者json文件
                if(StatusCode == 200){
                    String entity = EntityUtils.toString (response.getEntity(),"utf-8");
                    Document docDetail = Jsoup.parse(entity);
                    Elements ele = docDetail.select("div[class=w]").select("div[class=detail]")
                            .select("div[class=ETab]").select("div[class=tab-con]").select("div[data-tab=item]");
                    Elements ele1 = ele .select("div[class=p-parameter]").select("ul[class=p-parameter-list]");
                    ISBN = ele1.select("li").eq(1).attr("title");
                    id = ele1.select("li").eq(3).attr("title");
                    for (int i=6;i<10;i++) {
                        publishTime = ele1.select("li").eq(i).html();
                        if (publishTime.indexOf("出版时间")>-1) {
                            publishTime = ele1.select("li").eq(i).attr("title");
                            break;
                        }
                    }


                    Elements ele2 = ele.select("div[class=detail-content]").select("div[class=detail-content-wrap]")
                            .select("div[class=detail-content-item]").select("div[class=J-detail-content]");

                    authorBrief = ele2.select("div[id=detail-tag-id-3]").select("div[class=item-mc]")
                            .select("div[class=book-detail-content]").select("p").html();

                    summary = ele2.select("div[id=detail-tag-id-4]").select("div[class=item-mc]")
                            .select("div[class=book-detail-content]").select("p").eq(0).html();
                }
            }
            BookInfo book = new BookInfo();
            book.setPublisher(publisher);
            book.setPublishTime(publishTime);
            book.setIsbn(ISBN);
            book.setBookId(sortType+"_"+id);
            book.setAuthor(author);
            book.setSortType(sortType);
            book.setAuthorBrief(authorBrief);
            book.setBookName(bookName);
            book.setCoverImg(coverImg);
            book.setCreateTime(new Date());
            book.setPrice(price);
            book.setSortNum(Integer.parseInt(sortNum));
            book.setSummary(summary);
            book.setType(type);
            book.setResource("0");
            datas.add(book);
        }
        return datas;
    }

    public List<BookInfo> getDdData(HttpClient httpClient,String html,String sortType) throws Exception {
        List<BookInfo> datas = new ArrayList<BookInfo>();
        Document doc = Jsoup.parse(html);
//        Elements elements = doc.select("ul[class=bang_list_mode]").select("li[class]");
        Elements elements = doc.select("ul[class=bang_list clearfix bang_list_mode]").select("li");
        String type = doc.select("div[class=bang_wrapper]").select("div[class=bang_wrapper]").select("span").eq(3).html();
        for (Element element : elements) {
            String sortNum = element.select("div").eq(0).html().split("\\.")[0];
            String coverImg = element.select("div[class=pic]").select("a").select("img").attr("src");
            String author = element.select("div[class=publisher_info]").eq(0).select("a").eq(0).html();
            String bookName = element.select("div[class=name]").select("a").html();
            String publisher = element.select("div[class=publisher_info]").eq(1).select("a").html();
            String price = element.select("div[class=price]").select("p").eq(0).select("span").eq(1).html();
            String publishTime= element.select("div[class=publisher_info]").eq(1).select("span").html();
            String detailURL = element.select("div[class=pic]").select("a").attr("href");
            String ISBN="";
            String id="";
            String summary="";
            String authorBrief="";

            //爬取详情页的内容
            if (detailURL!=null && detailURL!="") {
                HttpResponse response = paresUrl(httpClient, detailURL);
                //获取响应状态码
                int StatusCode = response.getStatusLine().getStatusCode();
                //如果状态响应码为200，则获取html实体内容或者json文件
                if(StatusCode == 200){
                    String entity = EntityUtils.toString (response.getEntity(),"gbk");
                    Document docDetail = Jsoup.parse(entity);
                    Elements ele = docDetail.select("div[class=t_box_left]").select("div[class=pro_content]")
                            .select("ul[class=key clearfix]");
                    String ISBNStr = ele.select("li").eq(4).html().split("ISBN")[1];
                    ISBN = ISBNStr.substring(1,ISBNStr.length());
                    id = ISBN;

                }
            }
            BookInfo book = new BookInfo();
            book.setPublisher(publisher);
            book.setPublishTime(publishTime);
            book.setIsbn(ISBN);
            book.setBookId(sortType+"_"+id);
            book.setAuthor(author);
            book.setSortType(sortType);
            book.setAuthorBrief(authorBrief);
            book.setBookName(bookName);
            book.setCoverImg(coverImg);
            book.setCreateTime(new Date());
            price = price.substring(1,price.length());
            book.setPrice(Double.parseDouble(price));
            book.setSortNum(Integer.parseInt(sortNum));
            book.setSummary(summary);
            book.setType(type);
            book.setResource("1");
            datas.add(book);
        }
        return datas;
    }

    public List<BookInfo> jdUrlParser (HttpClient client, String url,String sortType) throws Exception {
        //用来接收解析的数据
        List<BookInfo> JingdongData = new ArrayList<BookInfo>();
        //获取网站响应的html，这里调用了HTTPUtils类
        HttpResponse response = paresUrl(client, url);
        //获取响应状态码
        int StatusCode = response.getStatusLine().getStatusCode();
        //如果状态响应码为200，则获取html实体内容或者json文件
        if(StatusCode == 200){
            String entity = EntityUtils.toString (response.getEntity(),"gbk");
            JingdongData = getJdData(client,entity,sortType);
            EntityUtils.consume(response.getEntity());
        }else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
        return JingdongData;
    }

    public List<BookInfo> ddUrlParser (HttpClient client, String url,String sortType) throws Exception {
        //用来接收解析的数据
        List<BookInfo> dangDangData = new ArrayList<BookInfo>();
        //获取网站响应的html，这里调用了HTTPUtils类
        HttpResponse response = paresUrl(client, url);
        //获取响应状态码
        int StatusCode = response.getStatusLine().getStatusCode();
        //如果状态响应码为200，则获取html实体内容或者json文件
        if(StatusCode == 200){
            String entity = EntityUtils.toString (response.getEntity(),"gb2312");
            dangDangData = getDdData(client,entity,sortType);
            EntityUtils.consume(response.getEntity());
        }else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
        return dangDangData;
    }

    public HttpResponse paresUrl(HttpClient httpClient, String url) throws Exception{
        HttpGet getMethod = new HttpGet(url);
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        try {
            //执行get方法
            response = httpClient.execute(getMethod);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static JSONObject getJsonObj(String src, String code) {
        InputStreamReader reader = null;
        BufferedReader in = null;
        try {
            URL url = new URL(src);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(1000);
            reader = new InputStreamReader(connection.getInputStream(), code);
            in = new BufferedReader(reader);
            String line = null;        //每行内容
            int lineFlag = 0;        //标记: 判断有没有数据
            StringBuffer content = new StringBuffer();
            while ((line = in.readLine()) != null) {
                content.append(line);
                lineFlag++;
            }
            return lineFlag == 0 ? null : JSONObject.parseObject(content.toString());
        } catch (SocketTimeoutException e) {
            System.out.println("连接超时!!!");
            return null;
        } catch (JSONException e) {
            System.out.println("网站响应不是json格式，无法转化成JSONObject!!!");
            return null;
        } catch (Exception e) {
            System.out.println("连接网址不对或读取流出现异常!!!");
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("关闭流出现异常!!!");
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("关闭流出现异常!!!");
                }
            }
        }
    }
}
