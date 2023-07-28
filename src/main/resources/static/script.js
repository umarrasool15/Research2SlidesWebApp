document.addEventListener("DOMContentLoaded", function() {
    
    const uploadBox = document.getElementById("uploadBox");
    const designSection = document.getElementById("designSection");
    const convertBtn = document.getElementById("convertBtn");
    const loading = document.getElementById("loading");
    const downloadLink = document.getElementById("downloadLink");
    let selectedDesign = null;
    let pdfFile = null;

    // Event listeners for file upload and drag-and-drop
    uploadBox.addEventListener("dragover", (e) => {
        e.preventDefault();
        uploadBox.classList.add("highlight");
    });

    uploadBox.addEventListener("dragleave", () => {
        uploadBox.classList.remove("highlight");
    });

    uploadBox.addEventListener("drop", (e) => {
        e.preventDefault();
        uploadBox.classList.remove("highlight");
        handleFileUpload(e.dataTransfer.files[0]);
    });

    document.getElementById("pdfInput").addEventListener("change", (e) => {
        handleFileUpload(e.target.files[0]);
    });

    // Handle file upload
    function handleFileUpload(file) {
        if (file && file.type === "application/pdf") {
            pdfFile = file;
            uploadBox.innerHTML = `<p>Selected file: ${file.name}</p>`;
            designSection.style.display = "block";
        } else {
            alert("Please select a valid PDF file.");
        }
    }

    // Event listener for design selection
    const designOptions = document.querySelectorAll('input[type="radio"]');
    designOptions.forEach((option) => {
        option.addEventListener("change", (e) => {
            selectedDesign = e.target.value;
        });
    });

    // Event listener for conversion button click
    convertBtn.addEventListener("click", () => {
        if (selectedDesign && pdfFile) {
            loading.style.display = "flex";
            convertBtn.disabled = true;

            // Prepare data to send in the request
            const formData = new FormData();
            formData.append("design", selectedDesign);
            formData.append("pdfFile", pdfFile);

            // Send the API request using Axios
            axios
                .post("/api/convert", formData, {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                    responseType: "arraybuffer", // Tell Axios to handle response as binary data
                })
                .then((response) => {
                    loading.style.display = "none";
                    convertBtn.disabled = false;

                    // Create a Blob from the response data
                    const blob = new Blob([response.data], {
                        type: "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                    });

                    // Create a download link for the converted PowerPoint
                    downloadLink.href = URL.createObjectURL(blob);
                    downloadLink.download = "converted_presentation.pptx";
                    downloadLink.style.display = "block";
                })
                .catch((error) => {
                    loading.style.display = "none";
                    convertBtn.disabled = false;
                    console.error("Error occurred during conversion:", error);
                    alert("Error occurred during conversion. Please try again later.");
                });
        } else {
            alert("Please select a design and upload a PDF file.");
        }
    });
});