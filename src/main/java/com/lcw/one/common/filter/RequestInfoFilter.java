package com.lcw.one.common.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "RequestInfoFilter")
public class RequestInfoFilter implements Filter {
    private static final Log logger = LogFactory.getLog(RequestInfoFilter.class);

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        logger.info("==================== RequestInfoFilter Start ====================");
        long strtTime = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        try {
        	String uri = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        	logger.info(request.getMethod() + " : " + uri);
            logHeaders(request);

            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        logger.info("耗时：" + (endTime - strtTime) + " ms");
        logger.info("==================== RequestInfoFilter End ====================");
    }

    private void logHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headName = headers.nextElement();
            if(StringUtils.isNotEmpty(headName)) {
                headerMap.put(headName, request.getHeader(headName));
            }
        }
        headerMap.put("RemoteHost", request.getRemoteHost() + ":" + request.getRemotePort());

        logger.info(headerMap);
    }
}
