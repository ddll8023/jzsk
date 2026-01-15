package com.szy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.szy.entity.Dgq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DgqMapper extends BaseMapper<Dgq> {
    
    @Select("SELECT TM, DGQ_M1_Ia, DGQ_M1_Ib, DGQ_M1_Ic, DGQ_M1_Ua, DGQ_M1_Ub, DGQ_M1_Uc, DGQ_M1_Uab, DGQ_M1_Ubc, DGQ_M1_Uca, DGQ_M1_KD, DGQ_M1_KDSD FROM DGQ ORDER BY TM DESC")
    List<Dgq> selectAllWithCustomSql();
    
    @Select("SELECT TOP 10 TM, DGQ_M1_Ia, DGQ_M1_Ib, DGQ_M1_Ic, DGQ_M1_Ua, DGQ_M1_Ub, DGQ_M1_Uc, DGQ_M1_Uab, DGQ_M1_Ubc, DGQ_M1_Uca, DGQ_M1_KD, DGQ_M1_KDSD FROM DGQ ORDER BY TM DESC")
    List<Dgq> selectTop10WithCustomSql();
} 