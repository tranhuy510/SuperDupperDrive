package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.util.UserUtil;
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
    private final EncryptionService encryptionService;
    private final UserUtil userUtil;

    @GetMapping("/home")
    public String index(Authentication authentication, Model model) {
        Integer userId = userUtil.getCurrentUserId(authentication);

        model.addAttribute("files", fileService.getAllUploadedFiles(userId));
        model.addAttribute("notes", noteService.getNotes(userId));
        model.addAttribute("credentials", credentialsService.getCredentials(userId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }
}
