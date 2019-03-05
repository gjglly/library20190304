package com.zhku.zcj.library.mapper;

import com.zhku.zcj.library.model.BookInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookInfoMapper {

    int deleteByPrimaryKey(String bookId);

    int insert(BookInfo record);

    int insertSelective(BookInfo record);

    BookInfo selectByPrimaryKey(String bookId);

    BookInfo selectByIsbn(String isbn);

    List<BookInfo> selectBookListBySortTypeAndResource(@Param("sortType")String sortType, @Param("resource")String resource);

    List<BookInfo> selectAll();

    int updateByPrimaryKeySelective(BookInfo record);

    int updateByPrimaryKey(BookInfo record);

    int deleteBySortTypeAndResource(@Param("sortType")String sortType, @Param("resource")String resource);
}