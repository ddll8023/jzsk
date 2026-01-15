package com.szy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.Dgq;
import java.util.List;

public interface DgqService extends IService<Dgq> {
    
    List<Dgq> selectAllWithCustomSql();
    
    List<Dgq> selectTop10WithCustomSql();
    
    String testJdbcQuery();
    
    List<Dgq> selectAllWithJdbc();
} 