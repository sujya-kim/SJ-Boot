package com.sujya.prj.auth.security.jwt;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class SkipPathRequestMatcher implements RequestMatcher {

    private OrRequestMatcher matcher;
    private RequestMatcher requestMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath){
        List<RequestMatcher> m = pathsToSkip.stream().map(path ->{
            return new AntPathRequestMatcher(path);
        }).collect(Collectors.toList());

        matcher = new OrRequestMatcher(m);
        requestMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String url = request.getRequestURL().toString();

        if(matcher.matches(request)){
            return false;
        }
        return requestMatcher.matches(request) ? true : false;
    }
}
