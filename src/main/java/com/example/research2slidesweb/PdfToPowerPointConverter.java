package com.example.research2slidesweb;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PdfToPowerPointConverter {

    public static byte[] convert(MultipartFile pdfFile) throws IOException, InterruptedException {
        // Get the binary content of the uploaded PDF file
        byte[] pdfContent = pdfFile.getBytes();

        // Where results of pdf extraction will be output
        String projectRoot = System.getProperty("user.dir");
        String outputFolder = projectRoot + "/pdfextraction/content/output"; // Where results of pdf extraction will be output

        // images and json are stored here
        File folder = new File(outputFolder);
        folder.mkdirs();
        outputFolder = folder.getAbsolutePath();

        // create a TextExtraction Object
        TextExtraction pdfExtractor = new TextExtraction(outputFolder);

        // Load the PDF document using the provided input stream
        try (PDDocument document = PDDocument.load(pdfContent)) {

            // Extract Images & Text from the Document
            pdfExtractor.runExtraction(document, projectRoot);

            // Read the JSON file
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(new FileReader(projectRoot + "/pdfextraction/content/output/parsedPDF.json")).getAsJsonObject();

            // Extract the content from the "text" field
            String extractedText = jsonObject.get("text").getAsString();

            // parse text and break up into paragraphs and store in a json
            ArrayList<Slide> presentation = TextSegmenter.divide(extractedText);

            // summarize
            TextSummarizer.summarize(presentation);

            // generate PowerPoint
            return BPowerPointGenerator.create(projectRoot, presentation);
        }
    }
}
