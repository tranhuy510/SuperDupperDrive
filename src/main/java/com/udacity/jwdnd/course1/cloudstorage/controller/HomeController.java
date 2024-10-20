package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialsService;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);

        model.addAttribute("files", fileService.getAllUploadedFiles(user.getUserId()));
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialsService.getCredentials(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }
}
