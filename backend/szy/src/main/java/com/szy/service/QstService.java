package com.szy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.Qst;
import java.util.List;

public interface QstService extends IService<Qst> {
    
    List<Qst> selectAllWithJdbc();
    
    String testJdbcQuery();
} 