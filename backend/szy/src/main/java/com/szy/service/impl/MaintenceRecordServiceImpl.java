package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.MaintenceRecord;
import com.szy.mapper.MaintenceRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.service.MaintenceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-02-21
 */
@Service
@DS("gcdd")
public class MaintenceRecordServiceImpl extends ServiceImpl<MaintenceRecordMapper, MaintenceRecord> implements MaintenceRecordService {

    @Autowired
    MaintenceRecordMapper maintenceRecordMapper;
    /**
     * 从excel中导出全部信息
     * @return java.util.List<com.szy.entity.MaintenceRecord>
     * @author admin
     * @date 2024/06/11 11:45
     */
    @Override
    public List<MaintenceRecord> exportAll() {
        List<MaintenceRecord> records = maintenceRecordMapper.selectList(null);
        return records;
    }
}
