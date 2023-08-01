# Research2SlidesWebApp

Research2SlidesWeb is a Java-based web application that converts research papers in PDF format into PowerPoint presentations. It utilizes Apache PDFBox for PDF text extraction, Apache POI for PowerPoint generation, and various other libraries for text summarization and image handling.

## Features

- Extracts text and images from uploaded PDF files.
- Segments the extracted text into slides based on logical sections and headings.
- Summarizes each slide's content using an NLP-based text summarization model.
- Generates PowerPoint slides with the summarized content and associated images.

## Setup and Requirements

1. Install Java: Make sure you have Java Development Kit (JDK) 8 or above installed on your system.

2. Clone the Repository: Clone this GitHub repository to your local machine.

3. Build and Run: Open the project in your favorite Java IDE, Ensure you have the spring boot framework configured as well as Maven and open the project. Run the application then type the following URL into your browser (http://localhost:8080/).

4. Access the Web Application: Navigate to the web application's URL in your web browser to start using the PDF to PowerPoint conversion service.

## Usage

1. Upload PDF: Choose a PDF file from your local machine and upload it to the web application.

2. Select Design: Choose a design template for the PowerPoint presentation.

3. Convert: Click the "Convert" button to start the conversion process.

4. Download: Once the conversion is complete, you can download the generated PowerPoint presentation.

## Dependencies

- Apache PDFBox: https://pdfbox.apache.org/
- Apache POI: https://poi.apache.org/
- Gson: https://github.com/google/gson
- AsyncHttpClient: https://github.com/AsyncHttpClient/async-http-client

## Contributing

Contributions to this project are welcome! Feel free to submit bug reports, feature requests, or pull requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

