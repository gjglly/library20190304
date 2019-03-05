package com.zhku.zcj.library.controller;

import com.zhku.zcj.library.model.BookInfo;
import com.zhku.zcj.library.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
//@RequestMapping("book")
public class BookController {

    @Resource
    BookService bookService;

    @RequestMapping("selectAll")
    public String findAll() {
        List<BookInfo> list = bookService.findAllBook();
        return "index";
    }
}
