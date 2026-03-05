package com.szy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.mapper.PersonMapper;
import com.szy.pojo.dto.PersonDTO;
import com.szy.pojo.dto.PersonQueryDTO;
import com.szy.pojo.entity.Person;
import com.szy.pojo.vo.PersonVO;
import com.szy.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 人员服务实现
 */
@Service
@RequiredArgsConstructor
@DS("jcxx")
public class PersonServiceImpl implements PersonService {

    private final PersonMapper personMapper;

    @Override
    public PageInfo<PersonVO> list(PersonQueryDTO queryDTO) {
        // 设置分页默认值
        int currentPage = queryDTO.getCurrentPage() == null ? 1 : queryDTO.getCurrentPage();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(currentPage, pageSize);
        List<Person> persons = personMapper.selectList(queryDTO.getName());
        List<PersonVO> voList = persons.stream()
                .map(person -> BeanUtil.copyProperties(person, PersonVO.class))
                .collect(Collectors.toList());
        return new PageInfo<>(voList);
    }

    @Override
    public PersonVO getById(Long id) {
        Person person = personMapper.selectById(id);
        if (person == null) {
            throw new BusinessException("人员不存在");
        }
        return BeanUtil.copyProperties(person, PersonVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonVO save(PersonDTO dto) {
        Person person = BeanUtil.copyProperties(dto, Person.class);
        personMapper.insert(person);
        return BeanUtil.copyProperties(person, PersonVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonVO update(PersonDTO dto) {
        Person existing = personMapper.selectById(dto.getId());
        if (existing == null) {
            throw new BusinessException("人员不存在");
        }
        Person person = BeanUtil.copyProperties(dto, Person.class);
        personMapper.update(person);
        return BeanUtil.copyProperties(person, PersonVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        Person existing = personMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("人员不存在");
        }
        personMapper.deleteById(id);
    }
}
