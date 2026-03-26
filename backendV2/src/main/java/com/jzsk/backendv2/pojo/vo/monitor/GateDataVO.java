package com.jzsk.backendv2.pojo.vo.monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * 闸门数据VO
 * 闸门数据视图对象，字段名与前端保持一致（下划线格式）
 */
@Schema(name = "闸门数据VO", description = "闸门实时/历史数据视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GateDataVO {

    @Schema(description = "时间", example = "2024-01-01 12:00:00")
    private String tm;

    // ==================== 东干渠(DGQ) 字段 ====================
    @Schema(description = "东干渠M1 A相电流", example = "10.5")
    private Double dgq_M1_Ia;
    @Schema(description = "东干渠M1 B相电流", example = "10.3")
    private Double dgq_M1_Ib;
    @Schema(description = "东干渠M1 C相电流", example = "10.4")
    private Double dgq_M1_Ic;
    @Schema(description = "东干渠M1 A相电压", example = "220.0")
    private Double dgq_M1_Ua;
    @Schema(description = "东干渠M1 B相电压", example = "221.0")
    private Double dgq_M1_Ub;
    @Schema(description = "东干渠M1 C相电压", example = "219.0")
    private Double dgq_M1_Uc;
    @Schema(description = "东干渠M1 AB线电压", example = "380.0")
    private Double dgq_M1_Uab;
    @Schema(description = "东干渠M1 BC线电压", example = "381.0")
    private Double dgq_M1_Ubc;
    @Schema(description = "东干渠M1 CA线电压", example = "379.0")
    private Double dgq_M1_Uca;
    @Schema(description = "东干渠M1 开度", example = "50.0")
    private Double dgq_M1_KD;
    @Schema(description = "东干渠M1 开度设定", example = "50.0")
    private Double dgq_M1_KDSD;

    // ==================== 电站蝶阀(DZDF) 字段 ====================
    @Schema(description = "电站蝶阀M1 A相电流", example = "10.5")
    private Double dzdf_M1_Ia;
    @Schema(description = "电站蝶阀M1 B相电流", example = "10.3")
    private Double dzdf_M1_Ib;
    @Schema(description = "电站蝶阀M1 C相电流", example = "10.4")
    private Double dzdf_M1_Ic;
    @Schema(description = "电站蝶阀M1 A相电压", example = "220.0")
    private Double dzdf_M1_Ua;
    @Schema(description = "电站蝶阀M1 B相电压", example = "221.0")
    private Double dzdf_M1_Ub;
    @Schema(description = "电站蝶阀M1 C相电压", example = "219.0")
    private Double dzdf_M1_Uc;
    @Schema(description = "电站蝶阀M1 AB线电压", example = "380.0")
    private Double dzdf_M1_Uab;
    @Schema(description = "电站蝶阀M1 BC线电压", example = "381.0")
    private Double dzdf_M1_Ubc;
    @Schema(description = "电站蝶阀M1 CA线电压", example = "379.0")
    private Double dzdf_M1_Uca;
    @Schema(description = "电站蝶阀M1 液位", example = "12.5")
    private Double dzdf_M1_YW;
    @Schema(description = "电站蝶阀M1 流量", example = "100.0")
    private Double dzdf_M1_FIT;
    @Schema(description = "电站蝶阀M1 累计流量", example = "10000.0")
    private BigDecimal dzdf_M1_FIT_TOL;

    // ==================== 取水塔(QST) 字段 ====================
    @Schema(description = "取水塔M1 A相电流", example = "10.5")
    private Double qst_M1_Ia;
    @Schema(description = "取水塔M1 B相电流", example = "10.3")
    private Double qst_M1_Ib;
    @Schema(description = "取水塔M1 C相电流", example = "10.4")
    private Double qst_M1_Ic;
    @Schema(description = "取水塔M1 A相电压", example = "220.0")
    private Double qst_M1_Ua;
    @Schema(description = "取水塔M1 B相电压", example = "221.0")
    private Double qst_M1_Ub;
    @Schema(description = "取水塔M1 C相电压", example = "219.0")
    private Double qst_M1_Uc;
    @Schema(description = "取水塔M1 AB线电压", example = "380.0")
    private Double qst_M1_Uab;
    @Schema(description = "取水塔M1 开度", example = "50.0")
    private Double qst_M1_KD;
    @Schema(description = "取水塔M1 开度设定", example = "50.0")
    private Double qst_M1_KDSD;
    @Schema(description = "取水塔M2 A相电流", example = "10.5")
    private Double qst_M2_Ia;
    @Schema(description = "取水塔M2 B相电流", example = "10.3")
    private Double qst_M2_Ib;
    @Schema(description = "取水塔M2 C相电流", example = "10.4")
    private Double qst_M2_Ic;
    @Schema(description = "取水塔M2 A相电压", example = "220.0")
    private Double qst_M2_Ua;
    @Schema(description = "取水塔M2 B相电压", example = "221.0")
    private Double qst_M2_Ub;
    @Schema(description = "取水塔M2 C相电压", example = "219.0")
    private Double qst_M2_Uc;
    @Schema(description = "取水塔M2 AB线电压", example = "380.0")
    private Double qst_M2_Uab;
    @Schema(description = "取水塔M2 开度", example = "50.0")
    private Double qst_M2_KD;
    @Schema(description = "取水塔M2 开度设定", example = "50.0")
    private Double qst_M2_KDSD;

    // ==================== 西干渠(XGQ) 字段 ====================
    @Schema(description = "西干渠M1 A相电流", example = "10.5")
    private Double xgq_M1_Ia;
    @Schema(description = "西干渠M1 B相电流", example = "10.3")
    private Double xgq_M1_Ib;
    @Schema(description = "西干渠M1 C相电流", example = "10.4")
    private Double xgq_M1_Ic;
    @Schema(description = "西干渠M1 A相电压", example = "220.0")
    private Double xgq_M1_Ua;
    @Schema(description = "西干渠M1 B相电压", example = "221.0")
    private Double xgq_M1_Ub;
    @Schema(description = "西干渠M1 C相电压", example = "219.0")
    private Double xgq_M1_Uc;
    @Schema(description = "西干渠M1 AB线电压", example = "380.0")
    private Double xgq_M1_Uab;
    @Schema(description = "西干渠M1 BC线电压", example = "381.0")
    private Double xgq_M1_Ubc;
    @Schema(description = "西干渠M1 CA线电压", example = "379.0")
    private Double xgq_M1_Uca;
    @Schema(description = "西干渠M1 开度", example = "50.0")
    private Double xgq_M1_KD;
    @Schema(description = "西干渠M1 开度设定", example = "50.0")
    private Double xgq_M1_KDSD;
    @Schema(description = "西干渠M2 A相电流", example = "10.5")
    private Double xgq_M2_Ia;
    @Schema(description = "西干渠M2 B相电流", example = "10.3")
    private Double xgq_M2_Ib;
    @Schema(description = "西干渠M2 C相电流", example = "10.4")
    private Double xgq_M2_Ic;
    @Schema(description = "西干渠M2 A相电压", example = "220.0")
    private Double xgq_M2_Ua;
    @Schema(description = "西干渠M2 B相电压", example = "221.0")
    private Double xgq_M2_Ub;
    @Schema(description = "西干渠M2 C相电压", example = "219.0")
    private Double xgq_M2_Uc;
    @Schema(description = "西干渠M2 AB线电压", example = "380.0")
    private Double xgq_M2_Uab;
    @Schema(description = "西干渠M2 开度", example = "50.0")
    private Double xgq_M2_KD;
    @Schema(description = "西干渠M2 开度设定", example = "50.0")
    private Double xgq_M2_KDSD;

    // ==================== 溢洪道(YHD) 字段 ====================
    @Schema(description = "溢洪道M1 A相电流", example = "10.5")
    private Double yhd_M1_Ia;
    @Schema(description = "溢洪道M1 B相电流", example = "10.3")
    private Double yhd_M1_Ib;
    @Schema(description = "溢洪道M1 C相电流", example = "10.4")
    private Double yhd_M1_Ic;
    @Schema(description = "溢洪道M1 A相电压", example = "220.0")
    private Double yhd_M1_Ua;
    @Schema(description = "溢洪道M1 B相电压", example = "221.0")
    private Double yhd_M1_Ub;
    @Schema(description = "溢洪道M1 C相电压", example = "219.0")
    private Double yhd_M1_Uc;
    @Schema(description = "溢洪道M1 AB线电压", example = "380.0")
    private Double yhd_M1_Uab;
    @Schema(description = "溢洪道M1 BC线电压", example = "381.0")
    private Double yhd_M1_Ubc;
    @Schema(description = "溢洪道M1 CA线电压", example = "379.0")
    private Double yhd_M1_Uca;
    @Schema(description = "溢洪道M1 开度", example = "50.0")
    private Double yhd_M1_KD;
    @Schema(description = "溢洪道M1 开度设定", example = "50.0")
    private Double yhd_M1_KDSD;
    @Schema(description = "溢洪道M2 A相电流", example = "10.5")
    private Double yhd_M2_Ia;
    @Schema(description = "溢洪道M2 B相电流", example = "10.3")
    private Double yhd_M2_Ib;
    @Schema(description = "溢洪道M2 C相电流", example = "10.4")
    private Double yhd_M2_Ic;
    @Schema(description = "溢洪道M2 A相电压", example = "220.0")
    private Double yhd_M2_Ua;
    @Schema(description = "溢洪道M2 B相电压", example = "221.0")
    private Double yhd_M2_Ub;
    @Schema(description = "溢洪道M2 C相电压", example = "219.0")
    private Double yhd_M2_Uc;
    @Schema(description = "溢洪道M2 AB线电压", example = "380.0")
    private Double yhd_M2_Uab;
    @Schema(description = "溢洪道M2 BC线电压", example = "381.0")
    private Double yhd_M2_Ubc;
    @Schema(description = "溢洪道M2 CA线电压", example = "379.0")
    private Double yhd_M2_Uca;
    @Schema(description = "溢洪道M2 开度", example = "50.0")
    private Double yhd_M2_KD;
    @Schema(description = "溢洪道M2 开度设定", example = "50.0")
    private Double yhd_M2_KDSD;
    @Schema(description = "溢洪道M3 A相电流", example = "10.5")
    private Double yhd_M3_Ia;
    @Schema(description = "溢洪道M3 B相电流", example = "10.3")
    private Double yhd_M3_Ib;
    @Schema(description = "溢洪道M3 C相电流", example = "10.4")
    private Double yhd_M3_Ic;
    @Schema(description = "溢洪道M3 A相电压", example = "220.0")
    private Double yhd_M3_Ua;
    @Schema(description = "溢洪道M3 B相电压", example = "221.0")
    private Double yhd_M3_Ub;
    @Schema(description = "溢洪道M3 C相电压", example = "219.0")
    private Double yhd_M3_Uc;
    @Schema(description = "溢洪道M3 AB线电压", example = "380.0")
    private Double yhd_M3_Uab;
    @Schema(description = "溢洪道M3 BC线电压", example = "381.0")
    private Double yhd_M3_Ubc;
    @Schema(description = "溢洪道M3 CA线电压", example = "379.0")
    private Double yhd_M3_Uca;
    @Schema(description = "溢洪道M3 开度", example = "50.0")
    private Double yhd_M3_KD;
    @Schema(description = "溢洪道M3 开度设定", example = "50.0")
    private Double yhd_M3_KDSD;
}
