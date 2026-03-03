package com.szy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.mapper.DeptMapper;
import com.szy.pojo.dto.DeptDTO;
import com.szy.pojo.dto.DeptQueryDTO;
import com.szy.pojo.entity.Department;
import com.szy.pojo.vo.DeptVO;
import com.szy.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现
 */
@Service
@RequiredArgsConstructor
@DS("jcxx")
public class DeptServiceImpl implements DeptService {

    private final DeptMapper deptMapper;

    @Override
    public PageInfo<DeptVO> list(DeptQueryDTO queryDTO) {
        // 设置分页默认值
        int currentPage = queryDTO.getCurrentPage() == null ? 1 : queryDTO.getCurrentPage();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(currentPage, pageSize);
        List<Department> departments = deptMapper.selectList(queryDTO.getDepartmentName());
        List<DeptVO> voList = departments.stream()
                .map(dept -> BeanUtil.copyProperties(dept, DeptVO.class))
                .collect(Collectors.toList());
        return new PageInfo<>(voList);
    }

    @Override
    public DeptVO getById(Long id) {
        Department department = deptMapper.selectById(id);
        if (department == null) {
            throw new BusinessException("部门不存在");
        }
        return BeanUtil.copyProperties(department, DeptVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptVO save(DeptDTO dto) {
        Department department = BeanUtil.copyProperties(dto, Department.class);
        deptMapper.insert(department);
        return BeanUtil.copyProperties(department, DeptVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptVO update(DeptDTO dto) {
        Department existing = deptMapper.selectById(dto.getId());
        if (existing == null) {
            throw new BusinessException("部门不存在");
        }
        Department department = BeanUtil.copyProperties(dto, Department.class);
        deptMapper.update(department);
        return BeanUtil.copyProperties(department, DeptVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        Department existing = deptMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("部门不存在");
        }
        deptMapper.deleteById(id);
    }
}
