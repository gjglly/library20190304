package com.zhku.zcj.library.service;


import org.apache.http.client.HttpClient;

import java.io.IOException;

public interface CrawlerService {

    public void crawlJdBookList(HttpClient httpClient, String[] url,String sortType,String resourceType) throws IOException;
}
