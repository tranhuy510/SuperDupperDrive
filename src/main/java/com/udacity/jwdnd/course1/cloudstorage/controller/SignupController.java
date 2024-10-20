package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
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
    public String signup() {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        String signupErrorMessage = StringUtils.EMPTY;

        if (userService.isExistUsername(user.getUsername())) {
            signupErrorMessage = "The username already exists.";
        }

        if (signupErrorMessage.isEmpty()) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded <= 0) {
                signupErrorMessage = "Something went wrong! Please try again.";
            } else {
                redirectAttributes.addFlashAttribute("signupSuccess", true);
                return "redirect:/login";
            }
        }

        model.addAttribute("signupErrorMessage", signupErrorMessage);

        return "signup";
    }
}
