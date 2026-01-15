package com.szy.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
public class PictureUtil {
    public static byte[] insertPic(MultipartFile file) {
        byte[] data = null;
        try {
            InputStream ins = file.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = ins.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            data = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
