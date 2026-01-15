package com.szy.util;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.szy.common.lang.Const.VIDEO_PHOTO_URL;

@Data
@Component
public class FileNameUtil {

    public List<String> getPhotos(String code, Date queryDate) {
        List<String> imageList = new ArrayList<>();
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MMdd/");

        try {
            // 格式化日期
            String formattedDate = outputFormat.format(queryDate);
            // 构建完整的文件路径
            String fullPath = VIDEO_PHOTO_URL + formattedDate + code;
            // 获取目录下所有文件
            File directory = new File(fullPath);
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        // 假设您只需要文件名，不需要路径
                        imageList.add(formattedDate + code +  '/' + file.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageList;
    }
}
