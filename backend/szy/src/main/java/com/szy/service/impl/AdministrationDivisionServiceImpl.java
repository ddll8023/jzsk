package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.AdministrationDivision;
import com.szy.mapper.AdministrationDivisionMapper;
import com.szy.service.AdministrationDivisionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-01-13
 */
@Service
@DS("gcdd")
public class AdministrationDivisionServiceImpl extends ServiceImpl<AdministrationDivisionMapper, AdministrationDivision> implements AdministrationDivisionService {
    @Resource
    AdministrationDivisionMapper administrationDivisionMapper;

    @Override
    public List<AdministrationDivision> exportAll() {
        List<AdministrationDivision> administrationDivisions = administrationDivisionMapper.selectList(null);
        return administrationDivisions;
    }
}
