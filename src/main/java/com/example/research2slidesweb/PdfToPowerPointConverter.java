package com.example.research2slidesweb;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PdfToPowerPointConverter {

    /**
     * Functions as our main method essentially, all other methods are called from here to complete the conversion
     * Process and return a completed pdf
     *
     * @param pdfFile
     * @param design
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static byte[] convert(MultipartFile pdfFile, String design) throws IOException, InterruptedException, URISyntaxException {

        // Get the binary content of the uploaded PDF file
        byte[] pdfContent = pdfFile.getBytes();

        // Where results of pdf extraction will be output
        String projectRoot = System.getProperty("user.dir");
        String outputFolder = projectRoot + "/content/output"; // Where results of pdf extraction will be output

        // images and json are stored here
        File folder = new File(outputFolder);
        folder.mkdirs();
        outputFolder = folder.getAbsolutePath();

        // create a TextExtraction Object
        TextExtraction pdfExtractor = new TextExtraction(resourcesDirectory);

        // Load the PDF document using the provided bytes that makeup the pdf
        try (PDDocument document = PDDocument.load(pdfContent)) {

            // Extract Images & Text from the Document
            pdfExtractor.runExtraction(document, projectRoot);

            // Read the JSON file
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(new FileReader(resourcesDirectory + "/output/parsedPDF.json")).getAsJsonObject();

            // Extract the content from the "text" field
            String extractedText = jsonObject.get("text").getAsString();

            // parse text and break up into paragraphs and store in a json
            ArrayList<Slide> presentation = TextSegmenter.divide(extractedText);

            // summarize
            TextSummarizer.summarize(presentation);

            // generate PowerPoint
            return BPowerPointGenerator.create(resourcesDirectory, presentation, design);

        }

    }
}
