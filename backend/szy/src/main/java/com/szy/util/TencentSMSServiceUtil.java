package com.szy.util;

import com.szy.config.TencentSMSConfig;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author 黄晓辉
 * @description 腾讯短信服务类
 * @date 2022/11/20 19:42:10
 **/
@Service
@Slf4j
public class TencentSMSServiceUtil {
    @Autowired
    private TencentSMSConfig smsConfig;

    @Value("${tencent.sms.signName}")
    private String tencentSmsSignName;

    @Value("${tencent.sms.appId}")
    private String tencentSmsAppId;

    @Value("${tencent.sms.appKey}")
    private String tencentSmsAppKey;

    /**
     * 新数据消息通知
     */
    @Value("${tencent.sms.template.newMDataInfoSmsId}")
    private String tencentSmsTemplateNewMDataInfoSmsId;

    /***
     * @param userPhoneNumber 用户电话号码
     * @param smsContent  短信模板
     * @return java.lang.Boolean
     *
     * @author 黄晓辉
     * @date 2022/11/20 19:48
     * @description 向用户发送有关数据的统计信息
     **/
    public Boolean sendUserNewDataInfo(String userPhoneNumber, String... smsContent) {
        return sendSms(userPhoneNumber, tencentSmsTemplateNewMDataInfoSmsId, smsContent);
    }

    private boolean sendSms(String userPhoneNumber, String templateId, String[] templateParamSet) {
        SendSmsRequest smsRequest = new SendSmsRequest();

        // 配置此项后，短信最前面会显示：【signName名字】
        smsRequest.setSignName(tencentSmsSignName);
        smsRequest.setSmsSdkAppId(tencentSmsAppId);

        smsRequest.setTemplateId(templateId);
        smsRequest.setTemplateParamSet(templateParamSet);

        String fullPhone = "+86" + userPhoneNumber;
        smsRequest.setPhoneNumberSet(new String[]{fullPhone});

        // SmsClient调用SendSms方法发送短信
        try {
            SendSmsResponse response = smsConfig.getSmsClient().SendSms(smsRequest);
            log.info("短信发送后的回调结果：" + SendSmsResponse.toJsonString(response));
            return true;
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return false;
        }
    }
}
