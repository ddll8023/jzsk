package com.szy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.Xgq;
import com.szy.mapper.XgqMapper;
import com.szy.service.XgqService;
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
public class XgqServiceImpl extends ServiceImpl<XgqMapper, Xgq> implements XgqService {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public List<Xgq> selectAllWithJdbc() {
        List<Xgq> result = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT TM, XGQ_M1_Ia, XGQ_M1_Ib, XGQ_M1_Ic, XGQ_M1_Ua, XGQ_M1_Ub, XGQ_M1_Uc, XGQ_M1_Uab, XGQ_M1_Ubc, XGQ_M1_Uca, XGQ_M1_KD, XGQ_M1_KDSD, XGQ_M2_Ia, XGQ_M2_Ib, XGQ_M2_Ic, XGQ_M2_Ua, XGQ_M2_Ub, XGQ_M2_Uc, XGQ_M2_Uab, XGQ_M2_KD, XGQ_M2_KDSD FROM XGQ ORDER BY TM DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    Xgq xgq = new Xgq();
                    xgq.setTM(rs.getString("TM"));
                    xgq.setXGQ_M1_Ia(rs.getDouble("XGQ_M1_Ia"));
                    xgq.setXGQ_M1_Ib(rs.getDouble("XGQ_M1_Ib"));
                    xgq.setXGQ_M1_Ic(rs.getDouble("XGQ_M1_Ic"));
                    xgq.setXGQ_M1_Ua(rs.getDouble("XGQ_M1_Ua"));
                    xgq.setXGQ_M1_Ub(rs.getDouble("XGQ_M1_Ub"));
                    xgq.setXGQ_M1_Uc(rs.getDouble("XGQ_M1_Uc"));
                    xgq.setXGQ_M1_Uab(rs.getDouble("XGQ_M1_Uab"));
                    xgq.setXGQ_M1_Ubc(rs.getDouble("XGQ_M1_Ubc"));
                    xgq.setXGQ_M1_Uca(rs.getDouble("XGQ_M1_Uca"));
                    xgq.setXGQ_M1_KD(rs.getDouble("XGQ_M1_KD"));
                    xgq.setXGQ_M1_KDSD(rs.getDouble("XGQ_M1_KDSD"));
                    xgq.setXGQ_M2_Ia(rs.getDouble("XGQ_M2_Ia"));
                    xgq.setXGQ_M2_Ib(rs.getDouble("XGQ_M2_Ib"));
                    xgq.setXGQ_M2_Ic(rs.getDouble("XGQ_M2_Ic"));
                    xgq.setXGQ_M2_Ua(rs.getDouble("XGQ_M2_Ua"));
                    xgq.setXGQ_M2_Ub(rs.getDouble("XGQ_M2_Ub"));
                    xgq.setXGQ_M2_Uc(rs.getDouble("XGQ_M2_Uc"));
                    xgq.setXGQ_M2_Uab(rs.getDouble("XGQ_M2_Uab"));
                    xgq.setXGQ_M2_KD(rs.getDouble("XGQ_M2_KD"));
                    xgq.setXGQ_M2_KDSD(rs.getDouble("XGQ_M2_KDSD"));
                    
                    result.add(xgq);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    @Override
    public String testJdbcQuery() {
        StringBuilder result = new StringBuilder();
        result.append("=== XGQ JDBC原生查询测试 ===\n");
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT TOP 1 TM, XGQ_M1_Ia, XGQ_M1_Ib, XGQ_M1_Ic, XGQ_M1_Ua, XGQ_M1_Ub, XGQ_M1_Uc, XGQ_M1_Uab, XGQ_M1_Ubc, XGQ_M1_Uca, XGQ_M1_KD, XGQ_M1_KDSD, XGQ_M2_Ia, XGQ_M2_Ib, XGQ_M2_Ic, XGQ_M2_Ua, XGQ_M2_Ub, XGQ_M2_Uc, XGQ_M2_Uab, XGQ_M2_KD, XGQ_M2_KDSD FROM XGQ ORDER BY TM DESC";
            
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
} 