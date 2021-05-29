package org.ucsccaa.homepagebe.filter.authorization;

import org.springframework.core.annotation.Order;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.NoExecuteAccessException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.NoWriteAccessException;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/article/*")
@Order
public class ArticleAuthorizationFilter extends AbstractAuthorizationFilter {
    @Override
    protected boolean filterable(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getMethod();
        if ("GET".equals(method)) {
            return false;
        }
        String token = request.getHeader("authorization");
        return !authentication.getValue(token, "isAdmin", Boolean.class);

    }

    @Override
    protected void verify(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("authorization");
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        Integer uid = authentication.getValue(token, "uid", Integer.class);
        if ("POST".equals(method) || "PUT".equals(method)) {
            throw new NoWriteAccessException(String.valueOf(uid), method, url);
        } else if ("DELETE".equals(method)) {
            throw new NoExecuteAccessException(String.valueOf(uid), method, url);
        }
    }
}
