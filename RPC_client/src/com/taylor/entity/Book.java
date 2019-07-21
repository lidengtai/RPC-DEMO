package com.taylor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName Book
 * @Description TODO
 * @Author lidengtai
 * @Date 2019/6/30 15:13
 * @Version 1.0
 */
public class Book implements Serializable{

    private Integer bookid;
    private String bookName;
    private String author;
    private Integer size;
    private BigDecimal moeny;

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public BigDecimal getMoeny() {
        return moeny;
    }

    public void setMoeny(BigDecimal moeny) {
        this.moeny = moeny;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookid=" + bookid +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", size=" + size +
                ", moeny=" + moeny +
                '}';
    }
}
