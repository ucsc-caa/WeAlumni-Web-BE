package org.ucsccaa.homepagebe.filter.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public abstract class AbstractAuthenticationFilter implements Filter {
    protected abstract boolean filterable(HttpServletRequest request, HttpServletResponse response);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected Authentication authentication;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (filterable(httpServletRequest, httpServletResponse)) {
            try {
                String token = httpServletRequest.getHeader("authorization");
                authentication.validateToken(token);
            } catch (GenericServiceException e) {
                ExceptionHandler exceptionHandler = e.getExceptionHandler();
                httpServletResponse.setStatus(exceptionHandler.getStatusCodeValue());
                httpServletResponse.setContentType("application/json");
                response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(exceptionHandler.getResponseEntity().getBody()));
                logger.error("Failed in AuthenticationFilter: url - {}, errorMsg - {}", httpServletRequest.getRequestURL().toString(), e.getExceptionHandler().getMessage());
                return;
            } catch (Exception e) {
                ExceptionHandler exceptionHandler = ExceptionHandler.SERVER_ERROR.setMessage(e.getMessage());
                httpServletResponse.setStatus(exceptionHandler.getStatusCodeValue());
                httpServletResponse.setContentType("application/json");
                response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(exceptionHandler.getResponseEntity().getBody()));
                logger.error("Failed in AuthenticationFilter: url - {}, errorMsg - {}", httpServletRequest.getRequestURL().toString(), e.getMessage());
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
