package com.szy.service;

import com.szy.entity.IndividualPressureSites;
import com.szy.entity.InspectionRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
public interface InspectionRecordsService extends IService<InspectionRecords> {
    void updateInspectionRecords(InspectionRecords inspectionRecords);
    List<InspectionRecords> exportAll(String name);

    /**
     * 获取所有工程站点
     * @return java.util.List<java.lang.String>
     * @author admin
     * @date 2024/07/08 19:24
     */
    List<String> getAllProjects();

    void saveInspectionRecords(InspectionRecords inspectionRecords);

}
