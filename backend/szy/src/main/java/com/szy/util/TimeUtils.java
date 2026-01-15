package com.szy.util;

import com.szy.entity.WarningInformation;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeUtils {
    /**
     * 计算时间差
     * 返回格式为几天几小时
     * @param warningInformation
     * @return
     */
//    public static String calStayTime(WarningInformation warningInformation){
//        LocalDateTime startTime = LocalDateTime.ofInstant(warningInformation.getStartTime().toInstant(), ZoneId.systemDefault());
//        LocalDateTime overTime = LocalDateTime.ofInstant(warningInformation.getOverTime().toInstant(), ZoneId.systemDefault());
//        Duration duration = Duration.between(startTime,overTime);
//        long days = duration.toDays();
//        long hours = duration.toHours() % 24;
//        String stayTime = days + "天" + hours + "小时";
//        return stayTime;
//    }

    /**
     * 计算持续实现
     * @param warningInformation
     * @return java.lang.String
     * @author admin
     * @date 2024/06/05 22:33
     */
    public static String calStayTime(WarningInformation warningInformation){
        LocalDateTime startTime = LocalDateTime.ofInstant(warningInformation.getStartTime().toInstant(), ZoneId.systemDefault());
        LocalDateTime overTime = LocalDateTime.ofInstant(warningInformation.getOverTime().toInstant(), ZoneId.systemDefault());
        Duration duration = Duration.between(startTime, overTime);
        long days = duration.toDays();
        long totalHours = duration.toHours();
        long hours = totalHours % 24; // 剩余的小时数
        long minutes = duration.toMinutes() % 60; // 剩余的分钟数

        String stayTime = days + "天" + hours + "小时" + minutes + "分钟";
        return stayTime;
    }
}
