package com.qunar.qchat.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * create by hubo.hu (lex) at 2018/6/19
 */
public final class PostDataDumpFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(getClass());

    public void destroy() { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            request = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if(response instanceof HttpServletResponse) {
            response = new ResponseWrapper((HttpServletResponse) response);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            String param = this.getBodyString(request.getReader());
            request.setAttribute("postdata", param);

//            logger.info("请求参数" + param);
//            byte[] data = ((ResponseWrapper)response).toByteArray();
//            String result = new String(data,"UTF-8");
            String result = ((ResponseWrapper)response).getTextContent();
//            logger.info("返回结果" + result);
            request.setAttribute("result", result);
        }
    }


    public void init(FilterConfig filterConfig) throws ServletException { }

    private String getBodyString(BufferedReader br) {
        String inputLine;
        //String str = "";
        StringBuilder sb = new StringBuilder();
        try {
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return sb.toString();
    }
}