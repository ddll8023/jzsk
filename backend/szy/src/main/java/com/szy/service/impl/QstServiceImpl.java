package com.szy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.entity.Qst;
import com.szy.mapper.QstMapper;
import com.szy.service.QstService;
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
public class QstServiceImpl extends ServiceImpl<QstMapper, Qst> implements QstService {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public List<Qst> selectAllWithJdbc() {
        List<Qst> result = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT TM, QST_M1_Ia, QST_M1_Ib, QST_M1_Ic, QST_M1_Ua, QST_M1_Ub, QST_M1_Uc, QST_M1_Uab, QST_M1_Ubc, QST_M1_Uca, QST_M1_KD, QST_M1_KDSD, QST_M2_Ia, QST_M2_Ib, QST_M2_Ic, QST_M2_Ua, QST_M2_Ub, QST_M2_Uc, QST_M2_Uab, QST_M2_KD, QST_M2_KDSD FROM QST ORDER BY TM DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    Qst qst = new Qst();
                    qst.setTM(rs.getString("TM"));
                    qst.setQST_M1_Ia(rs.getDouble("QST_M1_Ia"));
                    qst.setQST_M1_Ib(rs.getDouble("QST_M1_Ib"));
                    qst.setQST_M1_Ic(rs.getDouble("QST_M1_Ic"));
                    qst.setQST_M1_Ua(rs.getDouble("QST_M1_Ua"));
                    qst.setQST_M1_Ub(rs.getDouble("QST_M1_Ub"));
                    qst.setQST_M1_Uc(rs.getDouble("QST_M1_Uc"));
                    qst.setQST_M1_Uab(rs.getDouble("QST_M1_Uab"));
                    qst.setQST_M1_Ubc(rs.getDouble("QST_M1_Ubc"));
                    qst.setQST_M1_Uca(rs.getDouble("QST_M1_Uca"));
                    qst.setQST_M1_KD(rs.getDouble("QST_M1_KD"));
                    qst.setQST_M1_KDSD(rs.getDouble("QST_M1_KDSD"));
                    qst.setQST_M2_Ia(rs.getDouble("QST_M2_Ia"));
                    qst.setQST_M2_Ib(rs.getDouble("QST_M2_Ib"));
                    qst.setQST_M2_Ic(rs.getDouble("QST_M2_Ic"));
                    qst.setQST_M2_Ua(rs.getDouble("QST_M2_Ua"));
                    qst.setQST_M2_Ub(rs.getDouble("QST_M2_Ub"));
                    qst.setQST_M2_Uc(rs.getDouble("QST_M2_Uc"));
                    qst.setQST_M2_Uab(rs.getDouble("QST_M2_Uab"));
                    qst.setQST_M2_KD(rs.getDouble("QST_M2_KD"));
                    qst.setQST_M2_KDSD(rs.getDouble("QST_M2_KDSD"));
                    
                    result.add(qst);
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
        result.append("=== QST JDBC原生查询测试 ===\n");
        
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT TOP 1 TM, QST_M1_Ia, QST_M1_Ib, QST_M1_Ic, QST_M1_Ua, QST_M1_Ub, QST_M1_Uc, QST_M1_Uab, QST_M1_Ubc, QST_M1_Uca, QST_M1_KD, QST_M1_KDSD, QST_M2_Ia, QST_M2_Ib, QST_M2_Ic, QST_M2_Ua, QST_M2_Ub, QST_M2_Uc, QST_M2_Uab, QST_M2_KD, QST_M2_KDSD FROM QST ORDER BY TM DESC";
            
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