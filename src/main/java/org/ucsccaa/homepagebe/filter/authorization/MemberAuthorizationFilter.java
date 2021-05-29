package org.ucsccaa.homepagebe.filter.authorization;

import org.ucsccaa.homepagebe.exceptions.customizedExceptions.NoReadAccessException;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/member/*")
public class MemberAuthorizationFilter extends AbstractAuthorizationFilter {
    @Override
    protected boolean filterable(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("authorization");
        return !authentication.getValue(token, "isAdmin", Boolean.class);
    }

    @Override
    protected void verify(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("authorization");
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String[] arr = url.split("/");
        Integer visitId = Integer.valueOf(arr[arr.length-1]);
        Integer uid = authentication.getValue(token, "uid", Integer.class);
        if (!uid.equals(visitId)) {
            throw new NoReadAccessException(String.valueOf(uid), method, url);
        }
    }
}
