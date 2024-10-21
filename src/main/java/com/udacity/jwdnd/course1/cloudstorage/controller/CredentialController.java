package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialService credentialsService;
    private final UserMapper userMapper;
    private final UserUtil userUtil;

    @PostMapping
    public String createOrUpdateCredential(Authentication authentication, Credential credential) {
        Integer userId = userUtil.getCurrentUserId(authentication);

        if (credential.getCredentialId() != null) {
            credentialsService.updateCredential(credential);
        } else {
            credentialsService.createCredential(credential, userId);
        }

        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteCredentials(@RequestParam("id") Integer credentialId, RedirectAttributes redirectAttributes){
        if(credentialId != null && credentialId > 0){
            credentialsService.deleteCredential(credentialId);
            return "redirect:/result?success";
        }

        redirectAttributes.addAttribute("error", "Cannot delete credential with credentialId = " + credentialId);
        return "redirect:/result?error";
    }
}
