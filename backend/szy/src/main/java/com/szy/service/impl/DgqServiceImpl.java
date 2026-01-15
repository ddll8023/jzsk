package com.szy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.Dgq;
import com.szy.mapper.DgqMapper;
import com.szy.service.DgqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.ArrayList;

@Service
public class DgqServiceImpl extends ServiceImpl<DgqMapper, Dgq> implements DgqService {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public List<Dgq> selectAllWithCustomSql() {
        return baseMapper.selectAllWithCustomSql();
    }
    
    @Override
    public List<Dgq> selectTop10WithCustomSql() {
        return baseMapper.selectTop10WithCustomSql();
    }
    
    @Override
    public String testJdbcQuery() {
        StringBuilder result = new StringBuilder();
        result.append("=== JDBC原生查询测试 ===\n");
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT TOP 1 TM, DGQ_M1_Ia, DGQ_M1_Ib, DGQ_M1_Ic, DGQ_M1_Ua, DGQ_M1_Ub, DGQ_M1_Uc, DGQ_M1_Uab, DGQ_M1_Ubc, DGQ_M1_Uca, DGQ_M1_KD, DGQ_M1_KDSD FROM DGQ ORDER BY TM DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                result.append("列数: ").append(columnCount).append("\n");
                
                // 显示列信息
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnType = metaData.getColumnTypeName(i);
                    int columnSize = metaData.getColumnDisplaySize(i);
                    result.append("列").append(i).append(": ").append(columnName)
                          .append(" (").append(columnType).append("(").append(columnSize).append("))").append("\n");
                }
                
                result.append("\n");
                
                if (rs.next()) {
                    result.append("数据行:\n");
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        String valueStr = (value == null) ? "NULL" : value.toString();
                        result.append(columnName).append(": ").append(valueStr).append("\n");
                    }
                } else {
                    result.append("没有查询到数据\n");
                }
            }
        } catch (Exception e) {
            result.append("JDBC查询异常: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString();
    }
    
    @Override
    public List<Dgq> selectAllWithJdbc() {
        List<Dgq> result = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT TM, DGQ_M1_Ia, DGQ_M1_Ib, DGQ_M1_Ic, DGQ_M1_Ua, DGQ_M1_Ub, DGQ_M1_Uc, DGQ_M1_Uab, DGQ_M1_Ubc, DGQ_M1_Uca, DGQ_M1_KD, DGQ_M1_KDSD FROM DGQ ORDER BY TM DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    Dgq dgq = new Dgq();
                    dgq.setTM(rs.getString("TM"));
                    dgq.setDGQ_M1_Ia(rs.getDouble("DGQ_M1_Ia"));
                    dgq.setDGQ_M1_Ib(rs.getDouble("DGQ_M1_Ib"));
                    dgq.setDGQ_M1_Ic(rs.getDouble("DGQ_M1_Ic"));
                    dgq.setDGQ_M1_Ua(rs.getDouble("DGQ_M1_Ua"));
                    dgq.setDGQ_M1_Ub(rs.getDouble("DGQ_M1_Ub"));
                    dgq.setDGQ_M1_Uc(rs.getDouble("DGQ_M1_Uc"));
                    dgq.setDGQ_M1_Uab(rs.getDouble("DGQ_M1_Uab"));
                    dgq.setDGQ_M1_Ubc(rs.getDouble("DGQ_M1_Ubc"));
                    dgq.setDGQ_M1_Uca(rs.getDouble("DGQ_M1_Uca"));
                    dgq.setDGQ_M1_KD(rs.getDouble("DGQ_M1_KD"));
                    dgq.setDGQ_M1_KDSD(rs.getDouble("DGQ_M1_KDSD"));
                    
                    result.add(dgq);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
} 