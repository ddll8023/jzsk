package com.szy.controller;

import com.szy.entity.DataNew;
import com.szy.service.IDataNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.szy.mapper.SensorPointMapper;
import com.szy.entity.SensorPoint;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import com.szy.mapper.DataNewMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import com.fasterxml.jackson.core.type.TypeReference;
import java.sql.Timestamp;

/**
 * <p>
 * data_new 表 前端控制器
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@RestController
@RequestMapping("/data-new")
public class DataNewController {

    @Autowired
    private IDataNewService dataNewService;

    @Autowired
    private SensorPointMapper sensorPointMapper;

    @Autowired
    private DataNewMapper dataNewMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    class DataNewVO extends DataNew {
        private String pointId; // 实际返回测点名称
        @Override
        public String getPointId() { return pointId; }
        public void setPointId(String pointId) { this.pointId = pointId; }
    }

    /**
     * 分页获取data_new表的数据，pointId用测点名称筛选
     */
    @GetMapping("/page")
    public Map<String, Object> page(
        @RequestParam(defaultValue = "1") int current,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String pointId, // 这里是测点名称
        @RequestParam(required = false) String startTime,
        @RequestParam(required = false) String endTime
    ) {
        try {
            Page<DataNew> page = new Page<>(current, size);
            QueryWrapper<DataNew> wrapper = new QueryWrapper<>();

            // 名称转ID
            if (pointId != null && !pointId.isEmpty()) {
                SensorPoint sensorPoint = sensorPointMapper.selectOne(
                    new QueryWrapper<SensorPoint>().eq("name", pointId)
                );
                if (sensorPoint != null) {
                    wrapper.eq("point_id", sensorPoint.getId());
                } else {
                    Map<String, Object> result = new HashMap<>();
                    result.put("total", 0);
                    result.put("records", new ArrayList<>());
                    return result;
                }
            }

            // 时间范围筛选（转为Timestamp）
            Timestamp start = null, end = null;
            try {
                if (startTime != null && !startTime.isEmpty()) {
                    start = Timestamp.valueOf(startTime);
                }
                if (endTime != null && !endTime.isEmpty()) {
                    end = Timestamp.valueOf(endTime);
                }
            } catch (Exception e) {
                // 解析异常处理
            }
            if (start != null && end != null) {
                wrapper.ge("time", start).le("time", end);
            }

            wrapper.orderByDesc("time");
            Page<DataNew> dataPage = dataNewMapper.selectPage(page, wrapper);
            List<DataNew> dataList = dataPage.getRecords();
            Set<Long> pointIds = dataList.stream()
                    .map(DataNew::getPointId)
                    .filter(id -> id != null && !id.isEmpty())
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());

            Map<Long, String> idNameMap = new HashMap<>();
            if (!pointIds.isEmpty()) {
                for (SensorPoint sp : sensorPointMapper.selectNameByIds(new ArrayList<>(pointIds))) {
                    idNameMap.put(sp.getId(), sp.getName());
                }
            }

            List<DataNewVO> voList = dataList.stream().map(data -> {
                DataNewVO vo = new DataNewVO();
                org.springframework.beans.BeanUtils.copyProperties(data, vo);
                String name = idNameMap.getOrDefault(data.getPointId() == null ? null : Long.valueOf(data.getPointId()), data.getPointId());
                vo.setPointId(name);
                return vo;
            }).collect(Collectors.toList());

            Map<String, Object> result = new HashMap<>();
            result.put("total", dataPage.getTotal());
            result.put("records", voList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("total", 0);
            error.put("records", new ArrayList<>());
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * 不分页，获取所有data_new表的数据，pointId用测点名称筛选
     */
    @GetMapping("/list")
    public List<DataNewVO> list(@RequestParam(required = false) String pointId,
                                @RequestParam(required = false) String startTime,
                                @RequestParam(required = false) String endTime) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DataNew> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();

        // 名称转ID
        if (pointId != null && !pointId.isEmpty()) {
            SensorPoint sensorPoint = sensorPointMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SensorPoint>().eq("name", pointId)
            );
            if (sensorPoint != null) {
                wrapper.eq("point_id", sensorPoint.getId());
            } else {
                // 名称不存在，返回空
                return new ArrayList<>();
            }
        }
        // 时间范围筛选（转为Timestamp）
        Timestamp start = null, end = null;
        try {
            if (startTime != null && !startTime.isEmpty()) {
                start = Timestamp.valueOf(startTime);
            }
            if (endTime != null && !endTime.isEmpty()) {
                end = Timestamp.valueOf(endTime);
            }
        } catch (Exception e) {
            // 解析异常处理
        }
        if (start != null && end != null) {
            wrapper.ge("time", start).le("time", end);
        }
        List<DataNew> dataList = dataNewMapper.selectList(wrapper);
        Set<Long> pointIds = dataList.stream()
            .map(DataNew::getPointId)
            .filter(id -> {
                if (id == null || id.isEmpty()) return false;
                try {
                    Long.parseLong(id);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            })
            .map(Long::valueOf)
            .collect(Collectors.toSet());
    
    Map<Long, String> idNameMap = new HashMap<>();
    if (!pointIds.isEmpty()) {
        for (SensorPoint sp : sensorPointMapper.selectNameByIds(new ArrayList<>(pointIds))) {
            idNameMap.put(sp.getId(), sp.getName());
        }
    }
    
    List<DataNewVO> voList = dataList.stream().map(data -> {
        DataNewVO vo = new DataNewVO();
        org.springframework.beans.BeanUtils.copyProperties(data, vo);
        String name = data.getPointId();
        if (data.getPointId() != null) {
            try {
                Long pid = Long.valueOf(data.getPointId());
                name = idNameMap.getOrDefault(pid, data.getPointId());
            } catch (NumberFormatException e) {
                // 不是数字，直接用原值
            }
        }
        vo.setPointId(name);
        return vo;
    }).collect(Collectors.toList());
    return voList;
}
    /**
     * 获取所有测点（站点）列表，用于下拉框
     */
    @GetMapping("/points")
    public List<Map<String, Object>> getPoints() {
        List<SensorPoint> points = sensorPointMapper.selectList(null);
        return points.stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", p.getId());
                    map.put("name", p.getName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // 时间和水位高程
@GetMapping("/time-water-elevation")
public List<Map<String, Object>> getTimeAndWaterElevation(
    @RequestParam(required = false) String pointId,
    @RequestParam(required = false) String startTime,
    @RequestParam(required = false) String endTime
) {
    return getTimeAndField(pointId, "resultData", "水位高程", startTime, endTime);
}

// 时间和水位
@GetMapping("/time-water-level")
public List<Map<String, Object>> getTimeAndWaterLevel(
    @RequestParam(required = false) String pointId,
    @RequestParam(required = false) String startTime,
    @RequestParam(required = false) String endTime
) {
    return getTimeAndField(pointId, "resultData", "水位", startTime, endTime);
}

// 时间和水压
@GetMapping("/time-water-pressure")
public List<Map<String, Object>> getTimeAndWaterPressure(
    @RequestParam(required = false) String pointId,
    @RequestParam(required = false) String startTime,
    @RequestParam(required = false) String endTime
) {
    return getTimeAndField(pointId, "resultData", "水压", startTime, endTime);
}

// 时间和模数
@GetMapping("/time-modulus")
public List<Map<String, Object>> getTimeAndModulus(
    @RequestParam(required = false) String pointId,
    @RequestParam(required = false) String startTime,
    @RequestParam(required = false) String endTime
) {
    return getTimeAndField(pointId, "originalData", "模数", startTime, endTime);
}

// 时间和温度
@GetMapping("/time-temperature")
public List<Map<String, Object>> getTimeAndTemperature(
    @RequestParam(required = false) String pointId,
    @RequestParam(required = false) String startTime,
    @RequestParam(required = false) String endTime
) {
    return getTimeAndField(pointId, "originalData", "温度", startTime, endTime);
}
    // 公共方法，pointId为测点名称，自动转ID
    private List<Map<String, Object>> getTimeAndField(String pointId, String dataField, String key, String startTime, String endTime) {
        // 构造缓存key（可选：可加时间范围到key里，防止缓存错乱）
        String redisKey = String.format("dataNew:timeField:%s:%s:%s:%s:%s",
            dataField, key, pointId == null ? "all" : pointId,
            startTime == null ? "null" : startTime,
            endTime == null ? "null" : endTime);
    
        // 1. 先查Redis（Redis不可用时降级直接查数据库）
        boolean redisAvailable = true;
        try {
            String json = stringRedisTemplate.opsForValue().get(redisKey);
            if (json != null && !json.isEmpty()) {
                try {
                    return objectMapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {});
                } catch (Exception e) {
                    stringRedisTemplate.delete(redisKey);
                }
            }
        } catch (Exception e) {
            // Redis连接失败，标记不可用，继续查数据库
            redisAvailable = false;
        }
    
        // 2. Redis没有，查数据库
        QueryWrapper<DataNew> wrapper = new QueryWrapper<>();
        if (pointId != null && !pointId.isEmpty()) {
            SensorPoint sensorPoint = sensorPointMapper.selectOne(
                new QueryWrapper<SensorPoint>().eq("name", pointId)
            );
            if (sensorPoint != null) {
                wrapper.eq("point_id", sensorPoint.getId());
            } else {
                return new ArrayList<>();
            }
        }
        // 时间范围筛选（转为Timestamp）
        Timestamp start = null, end = null;
        try {
            if (startTime != null && !startTime.isEmpty()) {
                start = Timestamp.valueOf(startTime);
            }
            if (endTime != null && !endTime.isEmpty()) {
                end = Timestamp.valueOf(endTime);
            }
        } catch (Exception e) {
            // 解析异常处理
        }
        if (start != null && end != null) {
            wrapper.ge("time", start).le("time", end);
        }
        List<DataNew> dataList = dataNewMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (DataNew data : dataList) {
            Map<String, Object> map = new HashMap<>();
            map.put("time", data.getTime());
            try {
                String dataJson = "resultData".equals(dataField) ? data.getResultData() : data.getOriginalData();
                if (dataJson != null) {
                    Map<String, Object> parsed = objectMapper.readValue(dataJson, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
                    map.put("value", parsed.getOrDefault(key, null));
                } else {
                    map.put("value", null);
                }
            } catch (Exception e) {
                map.put("value", null);
            }
            result.add(map);
        }
        // 3. 写入Redis缓存（只缓存有数据的情况，且Redis可用时）
        try {
            if (!result.isEmpty() && redisAvailable) {
                stringRedisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(result), 10, java.util.concurrent.TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            // 缓存异常可忽略
        }
        return result;
    }

    /**
     * 获取每个测点最新的水位高程
     */
    @GetMapping("/latest-water-elevation")
    public List<Map<String, Object>> getLatestWaterElevation() {
        List<DataNew> latestList = dataNewMapper.selectLatestForAllPoints();
        // 收集所有pointId
        Set<Long> pointIds = latestList.stream()
                .map(DataNew::getPointId)
                .filter(id -> id != null && !id.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        Map<Long, String> idNameMap = new HashMap<>();
        if (!pointIds.isEmpty()) {
            for (SensorPoint sp : sensorPointMapper.selectNameByIds(new ArrayList<>(pointIds))) {
                idNameMap.put(sp.getId(), sp.getName());
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (DataNew data : latestList) {
            Map<String, Object> map = new HashMap<>();
            map.put("pointId", data.getPointId());
            String name = data.getPointId();
            if (data.getPointId() != null) {
                try {
                    Long pid = Long.valueOf(data.getPointId());
                    name = idNameMap.getOrDefault(pid, data.getPointId());
                } catch (NumberFormatException e) {
                    // 不是数字，直接用原值
                }
            }
            map.put("pointName", name);
            map.put("time", data.getTime());
            // 解析resultData中的水位高程
            String waterElevation = null;
            try {
                if (data.getResultData() != null) {
                    Map<String, Object> parsed = objectMapper.readValue(data.getResultData(), new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
                    Object val = parsed.get("水位高程");
                    waterElevation = val == null ? null : val.toString();
                }
            } catch (Exception e) {
                // 解析异常
            }
            map.put("waterElevation", waterElevation);
            result.add(map);
        }
        return result;
    }
} 