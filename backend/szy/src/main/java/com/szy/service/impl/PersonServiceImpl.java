package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.entity.Person;
import com.szy.entity.WarningInformation;
import com.szy.mapper.PersonMapper;
import com.szy.service.PersonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-02-23
 */
@Service
@DS("jcxx")
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {
    @Resource
    private PersonMapper personMapper;

    @Override
    public List<Person> exportAll(String name) {
        QueryWrapper<Person> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            wrapper.like("name", name);
        }
//        wrapper.orderByDesc("date");
        List<Person> personList = personMapper.selectList(wrapper);
        return personList;
    }

}
