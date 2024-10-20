package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid= #{userid}")
    List<Credential> getCredentialsByUserId(Integer userid);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credential getCredentialById(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userId) VALUES (#{url}, #{username}, #{key}, #{password},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void insertCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password= #{password} WHERE credentialId = #{credentialId}")
    void updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    int deleteCredential(int credentialId);
}
