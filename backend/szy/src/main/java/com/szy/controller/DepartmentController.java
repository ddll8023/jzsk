package com.szy.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Department;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  部门管理控制器
 * </p>
 *
 * @author l
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController{
    /**
     * 查询指定部门信息
     * @param id 部门id
     * @return 成功信息，部门json；失败信息，错误提示
     */
    @PreAuthorize("hasAuthority('xtgl_bmgl')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        Department department = departmentService.getById(id);
        if(department==null)
            return Result.fail(ResponseCode.NOT_EXIST,"查询部门信息不存在");
        return Result.ok(department);
    }
    /**
     * 部门列表
     * @param currentPage 当前页
     * @param pageSize 每页记录数
     * @return 成功信息，部门json
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('xtgl_bmgl')")
    public Result list(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize){
        Page<Department> page = new Page<>(currentPage,pageSize);
        Page<Department> departmentPage = departmentService.page(page);
        return Result.ok(departmentPage);
    }

    /**
     * 新增部门
     * @param department 部门json
     * @return 成功信息，部门json
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('xtgl_bmgl')")
    public Result save(@Validated @RequestBody Department department){
        departmentService.save(department);
        return Result.ok(department);
    }

    /**
     * 删除部门
     * @param id 部门id数组
     * @return 成功信息
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('xtgl_bmgl')")
    public Result delete(@PathVariable Long id){
        Department byId = departmentService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除部门不存在");
        }
        departmentService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 更新部门信息
     * @param department 部门json，要id
     * @return 成功信息
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('xtgl_bmgl')")
    public Result update(@RequestBody Department department){
        Department byId = departmentService.getById(department.getId());
        if(byId==null)
            return Result.fail(ResponseCode.NOT_EXIST,"待更新部门不存在");
        departmentService.updateById(department);
        return Result.ok(department);
    }

    /**
     * 分页返回根据输入部门名字查询的信息
     * @param name 部门名
     * @return 成功信息，分页部门信息json
     */
    @GetMapping("/list-by-name")
    @PreAuthorize("hasAuthority('xtgl_bmgl')")
    public Result listByName(String name){
        Page<Department> pageData = departmentService.page(getPage(),new QueryWrapper<Department>().like(StrUtil.isNotBlank(name),"department_name",name));
        return Result.ok(pageData);
    }
}
