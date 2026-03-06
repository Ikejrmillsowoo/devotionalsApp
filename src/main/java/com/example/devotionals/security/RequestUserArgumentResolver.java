package com.example.devotionals.security;

import com.example.devotionals.repo.UserRepository;
import com.example.devotionals.web.BadRequestException;
import com.example.devotionals.web.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.*;
//import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import java.util.UUID;

@Component
public class RequestUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository users;

    public RequestUserArgumentResolver(UserRepository users) {
        this.users = users;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(RequestUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);
        String header = req.getHeader("X-User-Id");
        if (header == null || header.isBlank()) {
            throw new BadRequestException("Missing X-User-Id header");
        }

        UUID userId;
        try {
            userId = UUID.fromString(header.trim());
        } catch (Exception e) {
            throw new BadRequestException("Invalid X-User-Id UUID");
        }

        if (!users.existsById(userId)) {
            // For dev convenience: auto-create user if not exists
            // If you prefer strict: throw NotFoundException instead.
            users.save(new com.example.devotionals.entity.User(userId));
        }

        return new RequestUser(userId);
    }
}