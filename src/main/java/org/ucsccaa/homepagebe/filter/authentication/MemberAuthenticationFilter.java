package org.ucsccaa.homepagebe.filter.authentication;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/member/*")
public class MemberAuthenticationFilter extends AbstractAuthenticationFilter {

    @Override
    protected boolean filterable(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }
}
