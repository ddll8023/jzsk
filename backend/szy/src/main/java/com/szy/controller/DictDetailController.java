package com.szy.controller;

import com.szy.common.lang.Result;
import com.szy.entity.DictDetail;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dict-detail")
public class DictDetailController extends BaseController{

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
//    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result getById(@PathVariable Long id){
        DictDetail dictDetail = dictDetailService.getById(id);
        if(dictDetail == null){
            return Result.fail("该字典详情不存在");
        }
        return Result.ok(dictDetail);
    }

    /**
     * 更新
     * @param dictDetail
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result update(@RequestBody DictDetail dictDetail){
        dictDetailService.updateById(dictDetail);
        return Result.ok();
    }

    /**
     * 删除字典详情
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result deleteById(@PathVariable Long id){
        dictDetailService.delete(id);
        return Result.ok();
    }

    /**
     * 新增字典详情
     * @param dictDetail
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result create(@RequestBody DictDetail dictDetail) {
        if(dictDetail.getId() != null){
            return Result.fail("新数据详情不能有id");
        }
        if(!dictDetailService.create(dictDetail)){
            return Result.fail("该字典详情已存在");
        };
        return Result.ok();
    }
}
