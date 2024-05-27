package com.howtodoinjava.web;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;

@Controller
public class ExcelDownloadController {

  private static final String FILE_DIRECTORY = "c:/temp/";

  @GetMapping("/download/excel/{fileName}")
  public ResponseEntity<?> downloadFile(@PathVariable String fileName) {

    // Construct file path
    String filePath = FILE_DIRECTORY + fileName;

    try {
      // Check if file exists
      Path file = Paths.get(filePath);
      if (!Files.exists(file)) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "File Not Found");
        pd.setType(URI.create("http://my-app-host.com/errors/not-found"));
        pd.setTitle("Report does not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
      }

      // Check file size
      long fileSize = Files.size(file);
      if (fileSize > 10 * 1024 * 1024) { // 10 MB
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "File Size Exceeded");
        pd.setType(URI.create("http://my-app-host.com/errors/report-size-limits"));
        pd.setTitle("Report size exceed the limit");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
      }

      // Read file into InputStreamResource
      InputStreamResource resource = new InputStreamResource(Files.newInputStream(file));

      // Set response headers
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

      return ResponseEntity.ok()
          .headers(headers)
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(resource);
    } catch (IOException e) {
      // Handle file reading error
      ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");
      pd.setType(URI.create("http://my-app-host.com/errors/misc"));
      pd.setTitle("Server Error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }
  }

  @GetMapping("/generate/excel")
  public ResponseEntity<?> generateExcel() {

    try {
      // Read file into InputStreamResource
      String filePath = getExcelReport();
      Path path = Paths.get(filePath);
      String fileName = path.getFileName().toString();
      InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));

      // Set response headers
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

      return ResponseEntity.ok()
          .headers(headers)
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(resource);
    } catch (IOException e) {
      // Handle file reading error
      ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");
      pd.setType(URI.create("http://my-app-host.com/errors/misc"));
      pd.setTitle("Server Error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }
  }

  private String getExcelReport() throws IOException {
    String fileName = "report-" + LocalDateTime.now().getNano() + ".xlsx";
    String fileLocation = "c:/temp/" + fileName;

    try (OutputStream os = new FileOutputStream(fileLocation)) {

      Workbook wb = new Workbook(os, fileName, "1.0");
      Worksheet ws = wb.newWorksheet("Sheet 1");

      ws.value(0, 0, "Column 1");
      ws.value(0, 1, "Column 2");
      ws.value(0, 2, "Column 3");
      ws.value(0, 3, "Column 4");
      ws.value(0, 4, "Column 5");
      ws.value(0, 5, "Column 6");
      ws.value(0, 6, "Column 7");
      ws.value(0, 7, "Column 8");
      ws.value(0, 8, "Column 9");
      ws.value(0, 9, "Column 10");

      for (int i = 1; i < 10; i++) {

        String value = "data-" + i;
        ws.value(i, 0, i);
        ws.value(i, 1, value);
        ws.value(i, 2, value);
        ws.value(i, 3, value);
        ws.value(i, 4, value);
        ws.value(i, 5, value);
        ws.value(i, 6, value);
        ws.value(i, 7, value);
        ws.value(i, 8, value);
        ws.value(i, 9, value);
      }

      wb.finish();

    } catch (Exception e) {
      throw new RuntimeException("Error while creating the Excel Report", e);
    }
    return fileLocation;
  }
}
