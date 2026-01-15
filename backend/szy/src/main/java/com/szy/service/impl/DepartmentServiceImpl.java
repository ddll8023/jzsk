package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.Department;
import com.szy.mapper.DepartmentMapper;
import com.szy.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-01-13
 */
@Service
@DS("jcxx")
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

}
