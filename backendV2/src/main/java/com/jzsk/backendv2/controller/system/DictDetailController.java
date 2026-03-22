package com.jzsk.backendv2.controller.system;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.system.dict.DictDetailVO;
import com.jzsk.backendv2.service.system.DictDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/dict-details")
@Tag(name = "字典详情管理", description = "提供字典详情管理接口")
public class DictDetailController {

    private final DictDetailService dictDetailService;

    @Operation(summary = "查询字典详情", description = "根据ID查询单个字典详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<DictDetailVO>> getById(
            @Parameter(description = "字典详情ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(dictDetailService.getById(id), "查询成功"));
    }

    @Operation(summary = "创建字典详情", description = "创建新的字典详情")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<DictDetailVO>> create(
            @Parameter(description = "字典详情创建请求", required = true)
            @Valid @RequestBody DictDetailCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dictDetailService.create(request), "创建成功"));
    }

    @Operation(summary = "更新字典详情", description = "更新指定字典详情")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<DictDetailVO>> update(
            @Parameter(description = "字典详情更新请求", required = true)
            @Valid @RequestBody DictDetailUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dictDetailService.update(request), "更新成功"));
    }

    @Operation(summary = "删除字典详情", description = "删除指定字典详情")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'system:manage')")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "字典详情删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        dictDetailService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }
}
