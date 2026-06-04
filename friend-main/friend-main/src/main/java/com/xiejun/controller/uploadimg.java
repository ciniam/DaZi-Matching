//package com.xiejun.controller;
//
//import com.xiejun.utils.QiniuOssUtils;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//
//
//@Controller
//@RequestMapping("upload")
//@CrossOrigin(origins = {"http://localhost:3000/","http://124.221.169.181/"})
//public class uploadimg {
//    @PostMapping("/img")
//    @ResponseBody
//    public void up1img(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
//        System.out.println("收到了请求上传单张图片==");
//        System.out.println(file);
//        if (file.isEmpty()) {
//            return;
//        }
//        String fileName = file.getOriginalFilename();
//        InputStream inputStream = file.getInputStream();
//        QiniuOssUtils utils = new QiniuOssUtils();
//
//        String upload = utils.upload(inputStream, fileName);
//        System.out.println(upload);
//    }
//}

package com.xiejun.controller;

import com.xiejun.common.BaseResponse;
import com.xiejun.common.ErrorCode;
import com.xiejun.common.ResultUtils;
import com.xiejun.model.domain.User;
import com.xiejun.service.UserService;
import com.xiejun.utils.QiniuOssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("upload")
@CrossOrigin(origins = {"http://localhost:3000/","http://124.221.169.181/"})
public class uploadimg {

    @Resource
    private QiniuOssUtils qiniuOssUtils;

    @Resource
    private UserService userService;

    @PostMapping("/img")
    @ResponseBody
    public BaseResponse up1img(@RequestParam(value = "file", required = false) MultipartFile file,
                               HttpServletRequest request) {
        try {
            log.info("收到了请求上传单张图片==");

            if (file == null || file.isEmpty()) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件不能为空");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件名无效");
            }

            if (!isImageFile(originalFilename)) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "只支持上传图片格式文件(jpg、png、gif等)");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件大小不能超过5MB");
            }

            String fileName = generateFileName(originalFilename);
            InputStream inputStream = file.getInputStream();

            String uploadUrl = qiniuOssUtils.upload(inputStream, fileName);
            log.info("上传成功,URL: {}", uploadUrl);

            // 不在此处更新用户头像，只返回上传后的URL
            // 头像更新将在 /user/update 接口中统一处理
            return ResultUtils.success(uploadUrl);

        } catch (IOException e) {
            log.error("文件上传失败", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("上传过程发生异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "上传失败: " + e.getMessage());
        }
    }


    private boolean isImageFile(String filename) {
        String lowerCase = filename.toLowerCase();
        return lowerCase.endsWith(".jpg") ||
                lowerCase.endsWith(".jpeg") ||
                lowerCase.endsWith(".png") ||
                lowerCase.endsWith(".gif") ||
                lowerCase.endsWith(".bmp") ||
                lowerCase.endsWith(".webp");
    }

    private String generateFileName(String originalFilename) {
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
