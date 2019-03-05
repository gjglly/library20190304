package com.zhku.zcj.library.service;

import com.zhku.zcj.library.model.BookInfo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookService {

    List<BookInfo> findAllBook();
}
