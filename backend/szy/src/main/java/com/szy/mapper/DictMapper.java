package com.szy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.criteria.DictQueryCriteria;
import com.szy.common.vo.DictVO;
import com.szy.entity.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {

    List<String> getAllNames();

    /**
     * 根据字典名称获得字典详情信息的标签
     * @param name
     * @return
     */
    List<String> getAllLabels(String name);

    /**
     * 查询所有字典数据
     * @param criteria
     * @return
     */
    List<Dict> queryAll(@Param("criteria") DictQueryCriteria criteria);

    /**
     * 统计数量
     * @param criteria
     * @return
     */
    Long countAll(@Param("criteria") DictQueryCriteria criteria);

    List<DictVO> getAllLabelValues(String name);
}
