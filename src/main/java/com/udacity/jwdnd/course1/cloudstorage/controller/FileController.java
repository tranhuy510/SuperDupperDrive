package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final UserMapper userMapper;

    @PostMapping
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        String uploadFileError = StringUtils.EMPTY;

        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);

        if (fileUpload.isEmpty()) {
            uploadFileError = "Please select a non-empty file.";
        }

        if (fileService.isExistFile(fileUpload.getOriginalFilename(), user.getUserId())) {
            uploadFileError = "The file already exists.";

        }

        if(uploadFileError.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", uploadFileError);
            return "redirect:/result?error";
        }

        fileService.addFile(fileUpload, user.getUserId());
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int fileId, Authentication authentication, RedirectAttributes redirectAttributes){
        String loggedInUserName = (String) authentication.getPrincipal();

        if(fileId > 0){
            fileService.deleteFile(fileId);
            return "redirect:/result?success";
        }

        redirectAttributes.addAttribute("error", "Unable to delete the file.");
        return "redirect:/result?error";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file.getFilename()+"\"")
                .body(new ByteArrayResource(file.getFileData()));
    }
}
