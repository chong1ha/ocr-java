/**
 * 지정된 Drop 영역에 파일을 드래그 앤 드롭하여 업로드 <br>
 * 드래그 앤 드롭 이벤트 처리 및 업로드된 이미지 파일 미리보기
 *
 * @param {string} dropAreaId - 드롭 영역의 HTML 요소 ID
 */
function DropFile(dropAreaId) {

    let dropArea = document.getElementById(dropAreaId);

    let selectedFile = null;
    let saveButton = document.getElementById('saveImage');

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    // Drop 영역에 파일이 들어올 때, 강조 표시
    function highlight(e) {
        preventDefaults(e);
        dropArea.classList.add("highlight");
    }

    // Drop 영역에서 파일이 나갈 때, 강조 표시
    function unhighlight(e) {
        preventDefaults(e);
        dropArea.classList.remove("highlight");
    }

    // 파일을 Drop 했을 때, 호출
    function handleDrop(e) {
        unhighlight(e);
        let dt = e.dataTransfer;
        let files = dt.files;

        handleFiles(files);
    }

    // Drop된 파일을 처리
    function handleFiles(files) {
        files = [...files];
        files.forEach(previewFile);
    }

    // 파일 미리보기용
    function previewFile(file) {
        // 이미지 타입인지 확인
        if (file.type.startsWith("image/")) {
            renderFile(file);

            selectedFile = file;
            saveButton.disabled = false;
        } else {
            alert("Only image files are allowed");
        }
    }

    // 파일 렌더링
    function renderFile(file) {
        let reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            let img = dropArea.getElementsByClassName("preview")[0];
            img.src = reader.result;
            img.style.display = "block";  // 미리보기 이미지를 보이도록 설정
        };
    }

    // 이미지 파일을 서버로 업로드
    async function saveImage() {

        if (selectedFile) {
            let formData = new FormData();
            formData.append("chooseFiles", selectedFile);

            try {
                let response = await fetch("/upload", {
                    method: "POST",
                    body: formData
                });

                if (response.ok) {
                    alert("Image uploaded successfully");
                    // 저장 후 Save 버튼 비활성화
                    saveButton.disabled = true;
                } else {
                    alert("Failed to upload image");
                }
            } catch (error) {
                console.error("Error occurred during upload:", error);
                alert("An error occurred during the upload");
            }
        }
    }

    // Drag 관련 EventListener
    dropArea.addEventListener("dragenter", highlight, false);
    dropArea.addEventListener("dragover", highlight, false);
    dropArea.addEventListener("dragleave", unhighlight, false);
    dropArea.addEventListener("drop", handleDrop, false);

    // Save Button Click
    saveButton.addEventListener("click", saveImage);

    return {
        handleFiles,
        saveImage
    };
}

const dropFile = new DropFile("drop-file");
