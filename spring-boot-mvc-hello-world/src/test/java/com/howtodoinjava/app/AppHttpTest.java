package com.howtodoinjava.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Starts the application on a random port and sends real HTTP requests, as a browser or curl would.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppHttpTest {

  @LocalServerPort
  int port;

  final HttpClient client = HttpClient.newHttpClient();

  // A controller that returns a view name without a template, to show the error
  @Controller
  static class MissingTemplateController {
    @GetMapping("/missing")
    String missing() {
      return "missing-page";
    }
  }

  @TestConfiguration
  static class Config {
    @Bean
    MissingTemplateController missingTemplateController() {
      return new MissingTemplateController();
    }
  }

  HttpResponse<String> get(String path) throws Exception {
    HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
        .header("Accept", "text/html")
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println("GET " + path + " -> " + response.statusCode() + "\n" + response.body());
    return response;
  }

  @Test
  void helloPage() throws Exception {
    HttpResponse<String> response = get("/hello?name=Lokesh");
    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.body()).contains("<h1>Hello, Lokesh!</h1>")
        .contains("<link rel=\"stylesheet\" href=\"/css/style.css\">");
  }

  @Test
  void staticFilesAreServed() throws Exception {
    HttpResponse<String> css = get("/css/style.css");
    assertThat(css.statusCode()).isEqualTo(200);
    assertThat(css.headers().firstValue("Content-Type")).hasValue("text/css");

    HttpResponse<String> index = get("/");
    assertThat(index.statusCode()).isEqualTo(200);
    assertThat(index.body()).contains("<a href=\"/hello\">Say hello</a>");
  }

  @Test
  void unknownUrlShowsWhitelabel404() throws Exception {
    HttpResponse<String> response = get("/helo");
    assertThat(response.statusCode()).isEqualTo(404);
    assertThat(response.body()).contains("Whitelabel Error Page")
        .contains("type=Not Found, status=404");
  }

  @Test
  void missingTemplateShowsWhitelabel500() throws Exception {
    HttpResponse<String> response = get("/missing");
    assertThat(response.statusCode()).isEqualTo(500);
    assertThat(response.body()).contains("Whitelabel Error Page")
        .contains("type=Internal Server Error, status=500");
  }
}
