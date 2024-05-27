package com.howtodoinjava.app.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class CompressingClientHttpRequestInterceptor
    implements ClientHttpRequestInterceptor {

  public ClientHttpResponse intercept(HttpRequest req, byte[] body, ClientHttpRequestExecution exec)
      throws IOException {
    HttpHeaders httpHeaders = req.getHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_ENCODING, "gzip");
    httpHeaders.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
    return exec.execute(req, compress(body));
  }

  public static byte[] compress(byte[] body) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos)) {
      gzipOutputStream.write(body);
    }
    return baos.toByteArray();
  }
}
