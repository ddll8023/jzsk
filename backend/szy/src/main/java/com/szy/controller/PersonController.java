package com.szy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Organization;
import com.szy.entity.Person;
import com.szy.entity.PumpStation;
import com.szy.entity.WarningInformation;
import com.szy.util.EasyPoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 管理人员信息 前端控制器
 * </p>
 *
 * @author l
 * @since 2022-02-23
 */
@RestController
@RequestMapping("/person")
public class PersonController extends BaseController {
    /**
     * 查询管理人员信息
     *
     * @param id 管理人员信息id
     * @return 成功信息，管理人员信息json；失败信息，错误提示
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('glxx_ryxx')")
    public Result info(@PathVariable Long id) {
        Person person = personService.getById(id);
        if (person == null){
            return Result.fail(ResponseCode.NOT_EXIST, "查询管理人员信息不存在");
        }
        return Result.ok(person);
    }

    /**
     * 管理人员信息列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return 成功信息，管理人员信息json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('glxx_ryxx')")
    public Result list(@RequestParam("currentPage") Integer currentPage,
                       @RequestParam("pageSize") Integer pageSize,
                       @RequestParam(value = "name", required = false) String name
                       ) {
        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            queryWrapper.like("name", name);
        }
        Page<Person> page = new Page<>(currentPage, pageSize);
        Page<Person> personPage = personService.page(page, queryWrapper);
        return Result.ok(personPage);
    }

    /**
     * 新增管理人员信息
     *
     * @param person 管理人员信息json
     * @return 成功信息，管理人员信息json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('glxx_ryxx')")
    public Result save(@Validated @RequestBody Person person) {
        personService.save(person);
        return Result.ok(person);
    }

    /**
     * 删除管理人员信息
     *
     * @param id 管理人员信息id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('glxx_ryxx')")
    public Result delete(@PathVariable Long id) {
        Person byId = personService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除管理人员信息不存在");
        }
        personService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新管理人员信息
     *
     * @param person 管理人员信息json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('glxx_ryxx')")
    public Result update(@RequestBody Person person) {
        Person byId = personService.getById(person.getId());
        if (byId == null)
            return Result.fail(ResponseCode.NOT_EXIST, "待更新管理人员信息不存在");
        personService.updateById(person);
        return Result.ok(person);
    }

    /**
     * 从excel中批量导入人员信息
     *
     * @param multipartFile MultipartFile文件
     * @return 成功信息
     */
    @PostMapping("/import-excel")
    @PreAuthorize("hasAuthority('gcxx_jcxx')")
    public Result importExcel(@RequestParam("file") MultipartFile multipartFile) throws ParseException {
        List<Person> personList = EasyPoiUtil.importExcel(multipartFile, 0, 1, Person.class);
        for (Person person : personList) {
            //获取实体类的属性是否为空，如果为空则返回错误
            if (person.checkForEmptyFields()) {
                return Result.fail(ResponseCode.FAIL, "您上传的表的属性对应不上或者有字段为空，请重新上传");
            }

            personService.save(person);
        }
        return Result.ok(personList);
    }

    /**
     * 导出到excel的全部信息
     *
     * @return 泵站列表
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAuthority('glxx_ryxx')")
    public Result exportAll(@RequestParam(value = "name", required = false) String name) {
        name = (name != null) ? name : "";
        List<Person> personList = personService.exportAll(name);
        return Result.ok(personList);
    }
}
