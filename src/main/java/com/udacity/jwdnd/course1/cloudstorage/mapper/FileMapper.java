package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid= #{userId} ")
    List<File> getAllFilesByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE userId = #{userId} AND filename = #{filename}")
    File getFileByUserIdAndName(Integer userId, String filename);

    @Insert("INSERT INTO FILES (filename, contentType, fileSize, userId, fileData) VALUES (#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void storeFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(int fileId);
}
