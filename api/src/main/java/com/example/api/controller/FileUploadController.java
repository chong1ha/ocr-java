package com.example.api.controller;

import com.example.core.util.ImgFileValidator;
import com.example.core.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

/**
 * File 업로드 관련 컨트롤러
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-11 오전 11:26
 */
@Controller
public class FileUploadController {

    /**
     * 파일 업로드 페이지 반환
     *
     * @return index view
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 업로드한 파일 처리
     *
     * @param file 업로드할 파일용 MultipartFile 객체
     * @param redirectAttributes redirect 시에 플래시 속성을 전달하는 객체
     * @return 업로드 후, redirect 할 URL
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("chooseFile") MultipartFile file,
                             RedirectAttributes redirectAttributes) {

        // 비어있는지
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "파일이 비어 있습니다.");
            return "redirect:/";
        }

        String fileName = file.getOriginalFilename();
        if (StringUtil.isEmpty(fileName)) {
            redirectAttributes.addFlashAttribute("message", "파일 이름이 유효하지 않습니다.");
            return "redirect:/";
        }

        ImgFileValidator validator = new ImgFileValidator();

        String imgFilePath = "D:" + File.separator + fileName;
        try {
            File destinationFile = new File(imgFilePath);
            file.transferTo(destinationFile);

            if (!validator.isValidImg(imgFilePath)) {
                destinationFile.delete(); // 파일 삭제
                redirectAttributes.addFlashAttribute("message", "허용되지 않는 파일 형식입니다.");
                return "redirect:/";
            }

            // success
            redirectAttributes.addFlashAttribute("message", "파일 '" + fileName + "'이 성공적으로 업로드되었습니다.");
        } catch (IOException e) {
            // fail
            redirectAttributes.addFlashAttribute("message", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/";
    }
}
