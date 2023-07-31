package com.example.research2slidesweb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class TextSegmenter {

    private static final int MIN_WORDS_PER_SECTION = 50;

    public static ArrayList<Slide> divide(String extractedText) throws JsonProcessingException, InterruptedException {
        ArrayList<Slide> presentation = new ArrayList<>();
        int sectionCount = 1;
        int pageCount = 0;

        String[] sections = extractedText.split("\\n\\n"); // Split by double newlines to find sections

        for (String section : sections) {
            String[] lines = section.trim().split("\\n");
            String firstLine = getFirstLine(lines);

            // Determine if the page ends during the paragraph and if so add 1 to page count
            for (String line : lines) {
                if (line.startsWith("***START OF PAGE")) {
                    pageCount++;
                }
            }

            // trim titles that are too long
            int maxWordLimit = 6;

            String[] words = firstLine.split("\\s+"); // Split the string by whitespace characters

            if (words.length > maxWordLimit) {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < maxWordLimit; i++) {
                    result.append(words[i]).append(" ");
                }
                firstLine = result.toString().trim(); // Trim any extra whitespace at the end
            }

            // Count the words in the current paragraph
            int paragraphWordCount = section.split("\\s+").length;

            // Check if the word count in the section meets the minimum requirement
            if (paragraphWordCount >= MIN_WORDS_PER_SECTION) {
                presentation.add(new Slide(sectionCount, pageCount, firstLine, section));
                sectionCount++;
            }
        }

        // Save the entire presentation as JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(presentation);
        String fileName = System.getProperty("user.dir") + "/src/main/resources/content/output/presentation.json";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        matchImages(presentation);
        return presentation;
    }

    private static String getFirstLine(String[] lines) {
        for (String line : lines) {
            // Remove numbers, colons, periods, etc., and leading spaces
            String firstLine = line.replaceAll("[0-9:.*]", "").replaceAll("^\\s+", "").trim();
            if (!firstLine.isEmpty()) {
                // Ensure it doesn't use page start or end for the first title
                if (firstLine.startsWith("START OF PAGE") || firstLine.startsWith("END OF PAGE")) {
                    return lines[1].replaceAll("[^a-zA-Z\\s]", "").replaceAll("^\\s+", "").trim();
                }
                return firstLine;
            }
        }
        return "";
    }

    public static void matchImages(ArrayList<Slide> presentation) {
        ArrayList<Integer> imageArray = TextExtraction.getImageArray();
        int temp = 1;
        int i = 0;

        for (Slide slide : presentation) {
            if (slide.PageNum > temp) {
                temp++;
                for (int x = 0; x < imageArray.size(); x++) {
                    if (imageArray.get(x) == (temp - 1))
                        presentation.get(i - 1).Image.add(x);
                }
            }
            i++;
        }
    }
}
