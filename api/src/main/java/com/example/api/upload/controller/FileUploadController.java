package com.example.api.upload.controller;

import com.example.api.upload.dto.FileUploadRequest;
import com.example.api.upload.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * File 업로드 관련 컨트롤러
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-11 오전 11:26
 */
@Tag(
        name = "OCR Image Upload"
        , description = "업로드 관련 API"
)
@Controller
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /**
     * 파일 업로드 페이지 반환
     *
     * @return index view
     */
    @Operation(
            summary = "메인 페이지"
            , description = "OCR 이미지 업로드를 위한 메인 페이지"
    )
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 업로드한 파일 처리
     *
     * @param files 업로드할 파일용 MultipartFile 객체
     * @param redirectAttributes redirect 시에 플래시 속성을 전달하는 객체
     * @return 업로드 후, redirect 할 URL
     */
    @Operation(
            summary = "이미지 파일 업로드"
            , description = "사용자가 업로드한 파일들 처리"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "파일 업로드 실패 (예: 비어있는 파일, 허용되지 않는 형식)"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping(
            value = "/upload"
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String uploadFile(
            @Parameter(
                    name = "chooseFiles"
                    , description = "업로드할 파일들"
                    , required = true
            ) @RequestParam("chooseFiles") List<MultipartFile> files,
                             RedirectAttributes redirectAttributes) throws Exception {

        long timestamp = System.currentTimeMillis();

        try {
            String userId = "testUser";
            String category = "testCategory";

            FileUploadRequest uploadData = new FileUploadRequest(
                    userId,
                    category,
                    timestamp,
                    files
            );

            String message = fileUploadService.uploadFiles(uploadData);
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/";
    }
}
