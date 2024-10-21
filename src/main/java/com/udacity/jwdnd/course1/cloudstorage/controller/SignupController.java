package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    @GetMapping
    public String index() {
        return "signup";
    }

    @PostMapping()
    public String signup(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        String signupErrorMessage = validateUser(user);

        if (signupErrorMessage == null) {
            if (userService.createUser(user) > 0) {
                redirectAttributes.addFlashAttribute("signupSuccess", true);
                return "redirect:/login";
            } else {
                signupErrorMessage = "Something went wrong! Please try again.";
            }
        }

        model.addAttribute("signupErrorMessage", signupErrorMessage);
        return "signup";
    }

    private String validateUser(User user) {
        if (userService.isExistUsername(user.getUsername())) {
            return "The username already exists.";
        }
        return null; // No errors
    }
}
