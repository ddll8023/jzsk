package com.jzsk.backendv2.service.impl.engineering;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.mapper.engineering.InspectionRecordsMapper;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsSolveDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsUpdateDTO;
import com.jzsk.backendv2.pojo.entity.engineering.InspectionRecordsEntity;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.InspectionRecordsExcelVO;
import com.jzsk.backendv2.pojo.vo.engineering.InspectionRecordsVO;
import com.jzsk.backendv2.service.engineering.InspectionRecordsService;
import com.jzsk.backendv2.utils.AliOSSUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 巡检记录服务实现类
 * 职责：提供巡检记录的CRUD和列表查询功能
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InspectionRecordsServiceImpl implements InspectionRecordsService {

    private final InspectionRecordsMapper inspectionRecordsMapper;
    private final AliOSSUtils aliOSSUtils;

    private static final String ABNORMAL_NONE = "无异常";
    private static final String SOLVE_PROCESSED = "已处理";
    private static final String SOLVE_UNPROCESSED = "未处理";
    private static final String SITUATION_NORMAL = "一切正常";

    @Override
    public PageResultVO<InspectionRecordsVO> page(InspectionRecordsPageQueryDTO queryDTO) {
        log.info("分页查询巡检记录，请求参数：{}", queryDTO);

        long page = queryDTO.getPage();
        long size = queryDTO.getSize();
        long offset = (page - 1) * size;

        List<InspectionRecordsEntity> entities = inspectionRecordsMapper.selectPage(queryDTO, offset, size);
        long total = inspectionRecordsMapper.countPage(queryDTO);

        List<InspectionRecordsVO> voList = entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        log.info("分页查询巡检记录成功，总记录数：{}，当前页记录数：{}", total, voList.size());
        return PageResultVO.of(voList, total, page, size);
    }

    @Override
    public InspectionRecordsVO getById(Long id) {
        log.info("查询巡检记录详情，ID：{}", id);
        InspectionRecordsEntity entity = inspectionRecordsMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(404, "巡检记录不存在");
        }
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InspectionRecordsVO create(InspectionRecordsCreateDTO request) {
        log.info("创建巡检记录，请求参数：{}", request);

        InspectionRecordsEntity entity = new InspectionRecordsEntity();
        entity.setProject(request.getProject());
        entity.setLongitude(request.getLongitude());
        entity.setLatitude(request.getLatitude());
        entity.setType(request.getType());
        entity.setAbnormal(request.getAbnormal());
        entity.setImage(request.getImage());
        entity.setPerson(request.getPerson());
        entity.setDate(request.getDate());

        // 业务逻辑：若异常情况为"无异常"，则自动设置处理状态和巡检情况
        if (ABNORMAL_NONE.equals(request.getAbnormal())) {
            entity.setSolve(SOLVE_PROCESSED);
            entity.setSituation(SITUATION_NORMAL);
        } else {
            entity.setSolve(SOLVE_UNPROCESSED);
        }

        inspectionRecordsMapper.insert(entity);
        log.info("创建巡检记录成功，ID：{}", entity.getId());
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InspectionRecordsVO update(InspectionRecordsUpdateDTO request) {
        log.info("更新巡检记录，请求参数：{}", request);

        InspectionRecordsEntity existing = inspectionRecordsMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "巡检记录不存在");
        }

        InspectionRecordsEntity entity = new InspectionRecordsEntity();
        entity.setId(request.getId());
        entity.setProject(request.getProject());
        entity.setLongitude(request.getLongitude());
        entity.setLatitude(request.getLatitude());
        entity.setType(request.getType());
        entity.setAbnormal(request.getAbnormal());
        entity.setSituation(request.getSituation());
        if (StringUtils.hasText(request.getSolve())) {
            entity.setSolve(request.getSolve());
        }
        entity.setImage(request.getImage());
        entity.setPerson(request.getPerson());
        entity.setDate(request.getDate());

        inspectionRecordsMapper.update(entity);
        log.info("更新巡检记录成功，ID：{}", request.getId());
        return getById(request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("删除巡检记录，ID：{}", id);
        InspectionRecordsEntity existing = inspectionRecordsMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "巡检记录不存在");
        }
        inspectionRecordsMapper.deleteById(id);
        log.info("删除巡检记录成功，ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InspectionRecordsVO solve(InspectionRecordsSolveDTO request) {
        log.info("处理巡检记录，ID：{}", request.getId());

        InspectionRecordsEntity existing = inspectionRecordsMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(404, "巡检记录不存在");
        }

        InspectionRecordsEntity entity = new InspectionRecordsEntity();
        entity.setId(request.getId());
        entity.setSolve(SOLVE_PROCESSED);

        inspectionRecordsMapper.update(entity);
        log.info("处理巡检记录成功，ID：{}", request.getId());
        return getById(request.getId());
    }

    @Override
    public List<InspectionRecordsExcelVO> listForExport() {
        log.info("查询巡检记录导出数据");
        List<InspectionRecordsEntity> entities = inspectionRecordsMapper.selectAllForExport();
        return entities.stream()
                .map(this::convertToExcelVO)
                .collect(Collectors.toList());
    }

    @Override
    public String uploadImage(MultipartFile image) {
        log.info("上传巡检图片，文件名：{}", image.getOriginalFilename());
        if (image.isEmpty()) {
            throw new BusinessException(400, "图片不能为空");
        }
        try {
            String url = aliOSSUtils.upload(image);
            log.info("上传巡检图片成功，文件URL：{}", url);
            return url;
        } catch (IOException e) {
            log.error("上传巡检图片失败：{}", e.getMessage(), e);
            throw new BusinessException(500, "图片上传失败：" + e.getMessage());
        }
    }

    private InspectionRecordsVO convertToVO(InspectionRecordsEntity entity) {
        InspectionRecordsVO vo = new InspectionRecordsVO();
        vo.setId(entity.getId());
        vo.setProject(entity.getProject());
        vo.setLongitude(entity.getLongitude());
        vo.setLatitude(entity.getLatitude());
        vo.setType(entity.getType());
        vo.setAbnormal(entity.getAbnormal());
        vo.setSituation(entity.getSituation());
        vo.setSolve(entity.getSolve());
        vo.setImage(entity.getImage());
        vo.setPerson(entity.getPerson());
        vo.setDate(entity.getDate());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    private InspectionRecordsExcelVO convertToExcelVO(InspectionRecordsEntity entity) {
        InspectionRecordsExcelVO vo = new InspectionRecordsExcelVO();
        vo.setId(entity.getId());
        vo.setProject(entity.getProject());
        vo.setLongitude(entity.getLongitude());
        vo.setLatitude(entity.getLatitude());
        vo.setType(entity.getType());
        vo.setAbnormal(entity.getAbnormal());
        vo.setSituation(entity.getSituation());
        vo.setSolve(entity.getSolve());
        vo.setPerson(entity.getPerson());
        vo.setDate(entity.getDate());
        return vo;
    }
}
