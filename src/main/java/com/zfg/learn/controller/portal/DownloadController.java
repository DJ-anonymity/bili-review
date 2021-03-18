package com.zfg.learn.controller.portal;

import com.zfg.learn.common.Const;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 下载控制器
 */
@Controller
@RequestMapping("/Download")
public class DownloadController {

    @GetMapping("/chromeExtend")
    public ResponseEntity<byte[]> chromeExtend() throws IOException {
        File file = new File(Const.Path.CHROME_EXTENSION);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDispositionFormData("attachment", URLEncoder.encode("chrome插件.zip", "UTF-8"));
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.OK);
    }

}
