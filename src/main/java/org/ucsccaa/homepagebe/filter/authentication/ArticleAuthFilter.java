package org.ucsccaa.homepagebe.filter.authentication;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/articles/*")
public class ArticleAuthFilter extends AbstractAuthFilter {

    @Override
    protected boolean filterable(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getMethod();
        return !"GET".equals(method);
    }
}
