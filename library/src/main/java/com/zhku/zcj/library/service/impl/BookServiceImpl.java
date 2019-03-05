package com.zhku.zcj.library.service.impl;

import com.zhku.zcj.library.mapper.BookInfoMapper;
import com.zhku.zcj.library.model.BookInfo;
import com.zhku.zcj.library.service.BookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Resource
    BookInfoMapper bookInfoMapper;

    @Override
    public List<BookInfo> findAllBook() {
        return bookInfoMapper.selectAll();
    }
}
