package com.zz.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhangzheng on 2019/10/15.
 */
@RestController
@RequestMapping("/common/file")
public class FileController {
    @Value(("${local.filetransfer.nasStoragePath}"))
    private String nasStoragePath;

    @GetMapping("/downloadFile/{dateName}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("dateName") String dateName,
                                                 @PathVariable("fileName") String fileName) {
        System.out.println(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("charset", "utf-8");
        headers.add("Content-Disposition", "attachement;filename*=UTF-8''" + fileName);
        String realFilePath = nasStoragePath+ File.separator+dateName+ File.separator+fileName;
        FileSystemResource fileSystemResource = new FileSystemResource(realFilePath);
        Resource resource = null;
        try {
            resource = new InputStreamResource(fileSystemResource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.parseMediaType("application/x-msdownload"))
                .body(resource);
    }
}
