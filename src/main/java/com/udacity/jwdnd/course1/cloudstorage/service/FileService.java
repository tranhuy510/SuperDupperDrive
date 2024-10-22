package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;

    public void addFile(MultipartFile fileUpload, int userid) throws IOException {
        File file = new File();

        file.setContentType(fileUpload.getContentType());
        file.setFileData(fileUpload.getBytes());
        file.setFilename(fileUpload.getOriginalFilename());
        file.setFileSize(Long.toString(fileUpload.getSize()));
        file.setUserId(userid);

        fileMapper.storeFile(file);
    }

    public List<File> getAllUploadedFiles(Integer userId){
        return fileMapper.getAllFilesByUserId(userId);
    }

    public boolean isExistFile(String filename, Integer userId) {
        File file = fileMapper.getFileByUserIdAndName(userId, filename);

        return file != null;
    }

    public void deleteFile(int fileId) {
        fileMapper.deleteFile(fileId);
    }

    public File getFileById(Integer fileId){
        return fileMapper.getFileById(fileId);
    }
}
