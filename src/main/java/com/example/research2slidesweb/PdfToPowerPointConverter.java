package com.example.research2slidesweb;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PdfToPowerPointConverter {

    /**
     * Converts the uploaded PDF file to a PowerPoint presentation.
     *
     * @param pdfFile The uploaded PDF file.
     * @param design  The design style for the PowerPoint presentation.
     * @return The binary content of the generated PowerPoint presentation.
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public static byte[] convert(MultipartFile pdfFile, String design) throws IOException, InterruptedException, URISyntaxException {
        // Get the binary content of the uploaded PDF file
        byte[] pdfContent = pdfFile.getBytes();

        // Output folder where the JSON and images will be stored
        Path outputFolderPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "content", "output");
        Files.createDirectories(outputFolderPath);
        String outputFolder = outputFolderPath.toAbsolutePath().toString();

        // Create a TextExtraction object
        TextExtraction pdfExtractor = new TextExtraction(outputFolder);

        // Load the PDF document using the provided bytes that make up the PDF
        try (PDDocument document = PDDocument.load(pdfContent)) {
            // Extract Images & Text from the Document
            pdfExtractor.runExtraction(document, outputFolder);

            // Read the JSON file
            Path jsonFilePath = outputFolderPath.resolve("parsedPDF.json");
            JsonObject jsonObject;
            try (var bufferedReader = Files.newBufferedReader(jsonFilePath)) {
                JsonParser parser = new JsonParser();
                jsonObject = parser.parse(bufferedReader).getAsJsonObject();
            }

            // Extract the content from the "text" field in the JSON
            String extractedText = jsonObject.get("text").getAsString();

            // Parse text and break up into paragraphs and store in a JSON
            ArrayList<Slide> presentation = TextSegmenter.divide(extractedText);

            // Summarize paragraphs
            TextSummarizer.summarize(presentation);

            // Generate PowerPoint presentation
            return BPowerPointGenerator.create(outputFolder, presentation, design);
        }
    }
}
