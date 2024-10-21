package com.udacity.jwdnd.course1.cloudstorage.util;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserMapper userMapper;

    public Integer getCurrentUserId(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return userMapper.getUser(username).getUserId();
    }
}

