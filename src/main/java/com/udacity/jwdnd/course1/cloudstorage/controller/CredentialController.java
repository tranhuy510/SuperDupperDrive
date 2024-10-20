package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
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

    @PostMapping
    public String handleAddUpdateCredential(Authentication authentication, Credential credential) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);
        Integer userId = user.getUserId();

        if (credential.getCredentialId() != null) {
            credentialsService.editCredentials(credential);
        } else {
            credentialsService.addCredential(credential, userId);
        }

        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteCredentials(@RequestParam("id") int credentialId, Authentication authentication, RedirectAttributes redirectAttributes){
        String loggedInUserName = (String) authentication.getPrincipal();

        if(credentialId > 0){
            credentialsService.deleteCredential(credentialId);
            return "redirect:/result?success";
        }

        redirectAttributes.addAttribute("error", "Unable to delete the credentials.");
        return "redirect:/result?error";
    }
}
