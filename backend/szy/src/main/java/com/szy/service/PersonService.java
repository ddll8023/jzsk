package com.szy.service;

import com.szy.entity.Person;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-02-23
 */
public interface PersonService extends IService<Person> {

    List<Person> exportAll(String name);

}
