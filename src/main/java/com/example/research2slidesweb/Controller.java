package com.example.research2slidesweb;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class Controller {

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertPdfToPowerPoint(@RequestBody MultipartFile pdfFile) {
        try {

            // Call the PdfToPowerPointConverter class to perform the conversion
            byte[] convertedPresentation = PdfToPowerPointConverter.convert(pdfFile);

            // Set the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "converted_presentation.pptx");

            // Return the converted presentation as a ResponseEntity
            return ResponseEntity.ok().headers(headers).body(convertedPresentation);


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

            // Return an error response if conversion fails
            return ResponseEntity.status(500).body(("Error occurred during conversion: " + e.getMessage().getBytes()).getBytes());
        }
    }
}
