package com.szy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.MaintenceRecord;

import java.util.List;

public interface MaintenceRecordService extends IService<MaintenceRecord> {
    /**
     * 从excel中将信息导出
     * @return java.util.List<com.szy.entity.MaintenceRecord>
     * @author admin
     * @date 2024/06/11 11:45
     */
    List<MaintenceRecord> exportAll();
}
