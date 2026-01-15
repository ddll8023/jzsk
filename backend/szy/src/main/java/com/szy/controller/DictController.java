package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.lang.Result;
import com.szy.common.vo.DictVO;
import com.szy.common.vo.TownVillageVO;
import com.szy.entity.Dict;
import com.szy.util.PageResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {

    @GetMapping("/list")
    public Result queryDict(@RequestParam(value = "blurry", required = false) String blurry,
                            @RequestParam(value = "currentPage") Integer currentPage,
                            @RequestParam(value = "pageSize") Integer pageSize){
        Page<Dict> page = new Page<>(currentPage, pageSize);
        PageResult<Dict> dictPageResult = dictService.queryAll(blurry, page);
        return Result.ok(dictPageResult);
    }

    /**
     * 新增
     * @param dict
     * @return
     */
    @PostMapping("/save")
    public Result saveDict(@RequestBody Dict dict){
        List<String> names = dictService.getAllNames();
        if(names.contains(dict.getName())){
            return Result.fail("该字典名称已存在");
        }
        dictService.create(dict);
        return Result.ok();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
//    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result getById(@PathVariable("id") Long id){
        Dict dict = dictService.getById(id);
        if(dict == null){
            return Result.fail("该字典不存在");
        }
        return Result.ok(dict);
    }

    /**
     * 更新
     * @param dict
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Dict dict){
        dictService.updateById(dict);
        return Result.ok();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Result deleteById(@PathVariable("id") Long id){
        dictService.delete(id);
        return Result.ok();
    }

    @GetMapping("/kinds")
//    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result getKindsByName(@RequestParam("name") String name){
        List<String> nameList = dictService.getKinds(name);
        if(nameList.isEmpty()){
            return Result.fail(name + "数据项名称错误");
        }
        return Result.ok(nameList);
    }

    @GetMapping("/LVs")
//    @PreAuthorize("hasAuthority('xtgl_yhgl')")
    public Result getLVByName(@RequestParam("name") String name){
        List<DictVO> dictVOS = dictService.getLVByName(name);
        if(dictVOS.isEmpty()){
            return Result.fail(name + "数据项名称错误");
        }
        return Result.ok(dictVOS);
    }

    @GetMapping("/tvKinds")
    public Result getKindsIter(@RequestParam("name") String name){
        List<TownVillageVO> townVillageVOList = new ArrayList<>();
        List<String> townList = dictService.getKinds(name);
        for (String town : townList) {
            List<String> villageList = dictService.getKinds(town);
            TownVillageVO townVillageVO = new TownVillageVO(town, villageList);
            townVillageVOList.add(townVillageVO);
        }
        return Result.ok(townVillageVOList);
    }
}
