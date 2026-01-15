package com.szy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.icon;
import com.szy.mapper.IconMapper;
import com.szy.mapper.ImpoundmentMapper;
import com.szy.service.IconService;
import com.szy.service.ImpoundmentService;
import org.springframework.stereotype.Service;


@Service
public class IconServiceImpl extends ServiceImpl<IconMapper, icon> implements IconService {
}
