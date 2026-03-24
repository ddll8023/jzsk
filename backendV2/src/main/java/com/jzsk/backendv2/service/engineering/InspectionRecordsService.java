package com.jzsk.backendv2.service.engineering;

import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsCreateDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsSolveDTO;
import com.jzsk.backendv2.pojo.dto.engineering.InspectionRecordsUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.engineering.InspectionRecordsExcelVO;
import com.jzsk.backendv2.pojo.vo.engineering.InspectionRecordsVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 巡检记录服务接口
 * 职责：提供巡检记录的CRUD和列表查询功能
 */
public interface InspectionRecordsService {

    /**
     * 分页查询巡检记录
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    PageResultVO<InspectionRecordsVO> page(InspectionRecordsPageQueryDTO queryDTO);

    /**
     * 查询巡检记录详情
     * @param id 巡检记录ID
     * @return 巡检记录VO
     */
    InspectionRecordsVO getById(Long id);

    /**
     * 创建巡检记录
     * @param request 创建请求
     * @return 巡检记录VO
     */
    InspectionRecordsVO create(InspectionRecordsCreateDTO request);

    /**
     * 更新巡检记录
     * @param request 更新请求
     * @return 巡检记录VO
     */
    InspectionRecordsVO update(InspectionRecordsUpdateDTO request);

    /**
     * 删除巡检记录
     * @param id 巡检记录ID
     */
    void delete(Long id);

    /**
     * 处理巡检记录（标记为已处理）
     * @param request 处理请求
     * @return 巡检记录VO
     */
    InspectionRecordsVO solve(InspectionRecordsSolveDTO request);

    /**
     * 导出巡检记录列表（返回Excel数据）
     * @return 巡检记录导出数据列表
     */
    List<InspectionRecordsExcelVO> listForExport();

    /**
     * 上传巡检图片
     * @param image 图片文件
     * @return 文件访问URL
     */
    String uploadImage(MultipartFile image);
}
