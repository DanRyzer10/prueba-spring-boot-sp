package com.danam.iroute.routest.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.MultipartFilter;

import java.io.IOException;

public class MultipartCheckFilter  implements Filter {



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException  {
        if(!request.getContentType().contains("multipart/form-data")){
            HttpServletResponse res = (HttpServletResponse) response;
           res.setStatus(400);
           res.setContentType("application/json");
           res.getWriter().write("{\"message\":\"Bad Request\"}");
           res.getWriter().flush();
            return;
        }

        chain.doFilter(request,response);

    }
}
