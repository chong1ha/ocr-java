package com.example.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-10-11 오전 11:26
 */
@Controller
public class FileUploadController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("chooseFile") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "파일이 비어 있습니다.");
            return "redirect:/";
        }

        // 파일 저장 로직
        try {
            // 파일 이름 가져오기
            String fileName = file.getOriginalFilename();
            File destinationFile = new File("C:/Users/gunha/OneDrive/바탕 화면" + File.separator + fileName);
            file.transferTo(destinationFile); // 파일 저장

            redirectAttributes.addFlashAttribute("message", "파일 '" + fileName + "'이 성공적으로 업로드되었습니다.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/";
    }
}
