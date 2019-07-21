package com.taylor.service.impl;

import com.taylor.entity.Book;
import com.taylor.service.BookService;

/**
 * @ClassName BookServiceImpl
 * @Description TODO
 * @Author lidengtai
 * @Date 2019/6/30 15:22
 * @Version 1.0
 */
public class BookServiceImpl implements BookService {
    @Override
    public String addBook(Book book) {
        System.out.println(book);
        return "添加的"+book+"图书成功";
    }
}
