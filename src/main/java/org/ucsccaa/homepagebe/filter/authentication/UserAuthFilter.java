package org.ucsccaa.homepagebe.filter.authentication;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/user/*")
public class UserAuthFilter extends AbstractAuthFilter {

    @Override
    protected boolean filterable(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getMethod();
        return !"POST".equals(method);
    }
}
