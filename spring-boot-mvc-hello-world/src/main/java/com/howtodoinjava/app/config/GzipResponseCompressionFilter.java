package com.howtodoinjava.app.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

@WebFilter(urlPatterns = {"/pattern-1", "/pattern-2" , "/*"})
public class GzipResponseCompressionFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    if (acceptsGZipEncoding(httpRequest)) {
      CustomHttpServletResponseWrapper gzipResponse = new CustomHttpServletResponseWrapper(httpResponse);
      gzipResponse.setHeader("Content-Encoding", "gzip");
      chain.doFilter(request, gzipResponse);
      gzipResponse.close();
    } else {
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {
  }

  private boolean acceptsGZipEncoding(HttpServletRequest request) {
    String acceptEncoding = request.getHeader("Accept-Encoding");
    return acceptEncoding != null && acceptEncoding.contains("gzip");
  }
}

class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

  private GZIPOutputStream gzipOutputStream;
  private ServletOutputStream outputStream;
  private PrintWriter writer;

  public CustomHttpServletResponseWrapper(HttpServletResponse response) throws IOException {
    super(response);
    this.gzipOutputStream = new GZIPOutputStream(response.getOutputStream());
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    if (this.outputStream == null) {
      this.outputStream = new CustomServletOutputStream(this.gzipOutputStream);
    }
    return this.outputStream;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    if (this.writer == null) {
      this.writer = new PrintWriter(new OutputStreamWriter(this.gzipOutputStream, getCharacterEncoding()));
    }
    return this.writer;
  }

  @Override
  public void flushBuffer() throws IOException {
    if (this.writer != null) {
      this.writer.flush();
    }
    if (this.outputStream != null) {
      this.outputStream.flush();
    }
    this.gzipOutputStream.flush();
  }

  public void close() throws IOException {
    this.gzipOutputStream.close();
  }
}

class CustomServletOutputStream extends ServletOutputStream {

  private final GZIPOutputStream gzipOutputStream;

  public CustomServletOutputStream(GZIPOutputStream gzipOutputStream) {
    this.gzipOutputStream = gzipOutputStream;
  }

  @Override
  public boolean isReady() {
    return true;
  }

  @Override
  public void setWriteListener(WriteListener writeListener) {
  }

  @Override
  public void write(int b) throws IOException {
    this.gzipOutputStream.write(b);
  }
}