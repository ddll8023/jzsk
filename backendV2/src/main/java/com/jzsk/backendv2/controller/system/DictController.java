package com.jzsk.backendv2.controller.system;

import com.jzsk.backendv2.pojo.dto.IdRequestDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictDetailUpdateDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictOptionQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.dict.DictUpdateDTO;
import com.jzsk.backendv2.pojo.vo.ApiResult;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.TreeOptionVO;
import com.jzsk.backendv2.pojo.vo.system.dict.DictDetailVO;
import com.jzsk.backendv2.pojo.vo.system.dict.DictVO;
import com.jzsk.backendv2.service.system.DictDetailService;
import com.jzsk.backendv2.service.system.DictService;
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
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${jzsk.v2.api-prefix:/api}/dicts")
@Tag(name = "字典管理", description = "提供字典管理和字典选项查询接口")
public class DictController {

    private final DictService dictService;
    private final DictDetailService dictDetailService;

    /**
     * 分页查询字典
     * 权限：登录即可访问
     */
    @Operation(summary = "分页查询字典", description = "分页查询字典摘要列表")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResultVO<DictVO>>> page(
            @Valid DictPageQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(dictService.page(queryDTO), "查询成功"));
    }

    /**
     * 查询字典详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询字典详情", description = "根据ID查询单个字典摘要")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<DictVO>> getById(
            @Parameter(description = "字典ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(dictService.getById(id), "查询成功"));
    }

    /**
     * 创建字典
     * 权限：登录即可访问
     */
    @Operation(summary = "创建字典", description = "创建新的字典")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/create")
    public ResponseEntity<ApiResult<DictVO>> create(
            @Parameter(description = "字典创建请求", required = true)
            @Valid @RequestBody DictCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dictService.create(request), "创建成功"));
    }

    /**
     * 更新字典
     * 权限：登录即可访问
     */
    @Operation(summary = "更新字典", description = "更新指定字典")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/update")
    public ResponseEntity<ApiResult<DictVO>> update(
            @Parameter(description = "字典更新请求", required = true)
            @Valid @RequestBody DictUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dictService.update(request), "更新成功"));
    }

    /**
     * 删除字典
     * 权限：登录即可访问
     */
    @Operation(summary = "删除字典", description = "删除指定字典及其详情")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "字典删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        dictService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }

    /**
     * 查询字典扁平选项
     * 权限：登录即可访问
     */
    @Operation(summary = "查询字典扁平选项", description = "按字典名称查询扁平选项列表")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/options/list")
    public ResponseEntity<ApiResult<List<OptionVO>>> listOptions(
            @Valid DictOptionQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(dictService.listOptions(queryDTO), "查询成功"));
    }

    /**
     * 查询字典树形选项
     * 权限：登录即可访问
     */
    @Operation(summary = "查询字典树形选项", description = "按字典名称查询树形选项列表")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/options/tree")
    public ResponseEntity<ApiResult<List<TreeOptionVO>>> treeOptions(
            @Valid DictOptionQueryDTO queryDTO) {
        return ResponseEntity.ok(ApiResult.success(dictService.treeOptions(queryDTO), "查询成功"));
    }

    /**
     * 查询字典详情列表
     * 权限：登录即可访问
     */
    @Operation(summary = "查询字典详情列表", description = "根据字典ID查询该字典下的所有详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResult<List<DictDetailVO>>> getDetails(
            @Parameter(description = "字典ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(dictService.getDetailsById(id), "查询成功"));
    }

    /**
     * 查询字典详情
     * 权限：登录即可访问
     */
    @Operation(summary = "查询字典详情", description = "根据ID查询单个字典详情")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{dictId}/items/{itemId}")
    public ResponseEntity<ApiResult<DictDetailVO>> getDetailById(
            @Parameter(description = "字典ID", required = true, example = "1")
            @PathVariable Long dictId,
            @Parameter(description = "字典详情ID", required = true, example = "1")
            @PathVariable Long itemId) {
        return ResponseEntity.ok(ApiResult.success(dictDetailService.getById(itemId), "查询成功"));
    }

    /**
     * 创建字典详情
     * 权限：登录即可访问
     */
    @Operation(summary = "创建字典详情", description = "在指定字典下创建新的详情项")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/{dictId}/items/create")
    public ResponseEntity<ApiResult<DictDetailVO>> createDetail(
            @Parameter(description = "字典ID", required = true, example = "1")
            @PathVariable Long dictId,
            @Parameter(description = "字典详情创建请求", required = true)
            @Valid @RequestBody DictDetailCreateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dictDetailService.create(request), "创建成功"));
    }

    /**
     * 更新字典详情
     * 权限：登录即可访问
     */
    @Operation(summary = "更新字典详情", description = "更新指定字典详情")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/{dictId}/items/update")
    public ResponseEntity<ApiResult<DictDetailVO>> updateDetail(
            @Parameter(description = "字典详情更新请求", required = true)
            @Valid @RequestBody DictDetailUpdateDTO request) {
        return ResponseEntity.ok(ApiResult.success(dictDetailService.update(request), "更新成功"));
    }

    /**
     * 删除字典详情
     * 权限：登录即可访问
     */
    @Operation(summary = "删除字典详情", description = "删除指定字典详情")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/{dictId}/items/delete")
    public ResponseEntity<ApiResult<Void>> deleteDetail(
            @Parameter(description = "字典详情删除请求", required = true)
            @Valid @RequestBody IdRequestDTO request) {
        dictDetailService.delete(request.getId());
        return ResponseEntity.ok(ApiResult.successMessage("删除成功"));
    }
}
