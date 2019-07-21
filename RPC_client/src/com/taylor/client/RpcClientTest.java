package com.taylor.client;

import com.taylor.entity.Book;
import com.taylor.service.BookService;

import java.math.BigDecimal;
import java.net.InetSocketAddress;

/**
 * @ClassName RpcClientTest
 * @Description TODO
 * @Author lidengtai
 * @Date 2019/6/30 17:58
 * @Version 1.0
 */
public class RpcClientTest {
    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient();
        BookService server = rpcClient.getRemoteProxy(BookService.class, new InetSocketAddress("localhost", 12345));
        Book book = new Book();
        book.setBookid(1001);
        book.setBookName("论语");
        book.setAuthor("孔子");
        book.setSize(32);
        book.setMoeny(new BigDecimal(25));
        String s = server.addBook(book);
        System.out.println("s = " + s);
    }
}
