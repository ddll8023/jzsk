package com.szy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.szy.entity.DictDetail;

public interface DictDetailService extends IService<DictDetail>{
    void delete(Long id);

    Boolean create(DictDetail dictDetail);
}
