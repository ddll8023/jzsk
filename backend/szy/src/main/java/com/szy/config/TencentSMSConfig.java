package com.szy.config;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 黄晓辉
 * @description 腾讯云平台短信服务配置类
 * @date 2022/11/20 19:40:14
 **/
@Configuration
@Slf4j
public class TencentSMSConfig {
    @Value("${tencent.secretId}")
    private String tencentSecretId;

    @Value("${tencent.secretKey}")
    private String tencentSecretKey;

    /**
     * @param
     * @return com.tencentcloudapi.sms.v20210111.SmsClient
     * @author 黄晓辉
     * @date 2022/11/20 19:41
     * @description 取腾讯短信接口客户端
     **/
    public SmsClient getSmsClient() {
        Credential credential = new Credential(tencentSecretId, tencentSecretKey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);
        httpProfile.setEndpoint("sms.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);

        return new SmsClient(credential, "ap-guangzhou", clientProfile);
    }
}
