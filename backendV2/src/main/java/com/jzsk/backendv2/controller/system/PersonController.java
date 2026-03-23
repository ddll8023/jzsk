package com.jzsk.backendv2.controller.system;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.system.person.PersonCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.person.PersonPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.person.PersonUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.person.PersonVO;
import com.jzsk.backendv2.service.system.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 人员信息控制器
 * 用途：提供人员信息管理的RESTful API接口
 * 遵循KISS原则：简单清晰的接口设计
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/persons")
@Tag(name = "人员信息管理", description = "提供人员信息增删改查接口")
@SecurityRequirement(name = "JWT")
public class PersonController {

    private final PersonService personService;

    /**
     * 分页查询人员列表
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询人员列表", description = "分页查询人员列表，支持按姓名条件筛选")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<PersonVO>>> page(
            @Valid PersonPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(personService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询人员详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询人员详情", description = "根据ID查询单个人员详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<PersonVO>> getById(
            @Parameter(description = "人员ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(personService.getById(id), "查询成功"));
    }

    /**
     * 创建人员
     * 权限：登录即可访问
     */
    @Operation(summary = "创建人员", description = "创建新人员信息")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<PersonVO>> create(
            @Parameter(description = "人员创建请求", required = true)
            @Valid @RequestBody PersonCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(personService.create(request), "创建成功"));
    }

    /**
     * 更新人员
     * 权限：登录即可访问
     */
    @Operation(summary = "更新人员", description = "更新指定人员信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<PersonVO>> update(
            @Parameter(description = "人员更新请求", required = true)
            @Valid @RequestBody PersonUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(personService.update(request), "更新成功"));
    }

    /**
     * 删除人员
     * 权限：登录即可访问
     */
    @Operation(summary = "删除人员", description = "删除指定人员信息（物理删除）")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "人员删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        personService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }
}
