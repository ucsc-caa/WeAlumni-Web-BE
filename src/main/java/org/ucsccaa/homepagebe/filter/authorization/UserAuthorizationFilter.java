package org.ucsccaa.homepagebe.filter.authorization;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/user/*")
public class UserAuthorizationFilter extends AbstractAuthorizationFilter {

    @Override
    protected boolean filterable(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getMethod();
        return !"POST".equals(method);
    }

    @Override
    protected void verify(HttpServletRequest request, HttpServletResponse response) {

    }
}
