package com.qunar.qchat.filter;

import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * create by hubo.hu (lex) at 2018/6/19
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseWrapper.class);

    ByteArrayOutputStream _stream = new ByteArrayOutputStream();
    PrintWriter _pw=new PrintWriter(_stream);

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }


    @Override
    public ServletResponse getResponse() {
        return this;
    }

    /**
     * 覆盖getWriter()方法，将字符流缓冲到本地
     */
//    @Override
//    public PrintWriter getWriter() throws IOException {
//        return _pw;
//    }
    /**
     * 覆盖getOutputStream()方法，将字节流缓冲到本地
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream(){
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            private TeeOutputStream tee = new TeeOutputStream(ResponseWrapper.super.getOutputStream(), _stream);

            @Override
            public void write(int b) throws IOException {
                tee.write(b);
            }
        };
    }
    /**
     * 把缓冲区内容写入输出流后关闭
     */
    public void flush(){
        try {
            _pw.flush();
            _pw.close();
            _stream.flush();
            _stream.close();
        } catch (IOException e) {
            LOGGER.error("catch error {}", ExceptionUtils.getStackTrace(e));
        }
    }
    /**
     * 获取字节流
     * @return
     */
    public ByteArrayOutputStream getByteArrayOutputStream(){
        return _stream;
    }
    /**
     * 将换出区内容转为文本输出
     * @return
     */
    public String getTextContent() {
        flush();
        return _stream.toString();
    }
}