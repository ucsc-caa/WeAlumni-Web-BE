package org.ucsccaa.homepagebe.filter.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.authentication.Authentication;
import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public abstract class AbstractAuthorizationFilter implements Filter {

    protected abstract boolean filterable(HttpServletRequest request, HttpServletResponse response);
    protected abstract void verify(HttpServletRequest request, HttpServletResponse response);

    @Autowired
    protected Authentication authentication;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try {
            if (filterable(httpServletRequest, httpServletResponse)) {
                verify(httpServletRequest, httpServletResponse);
            }
        } catch (GenericServiceException e) {
            ExceptionHandler exceptionHandler = e.getExceptionHandler();
            httpServletResponse.setStatus(exceptionHandler.getStatusCodeValue());
            httpServletResponse.setContentType("application/json");
            response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(exceptionHandler.getResponseEntity().getBody()));
            return;
        } catch (Exception e) {
            ExceptionHandler exceptionHandler = ExceptionHandler.SERVER_ERROR.setMessage(e.getMessage());
            httpServletResponse.setStatus(exceptionHandler.getStatusCodeValue());
            httpServletResponse.setContentType("application/json");
            response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(exceptionHandler.getResponseEntity().getBody()));
            return;
        }
        chain.doFilter(request, response);
    }
}
