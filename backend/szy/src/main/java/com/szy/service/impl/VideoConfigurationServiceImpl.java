package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.szy.common.vo.VideoCheckVO;
import com.szy.entity.VideoConfiguration;
import com.szy.mapper.VideoConfigurationMapper;
import com.szy.service.VideoConfigurationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-02-24
 */
@Service
@DS("yjxx")
public class VideoConfigurationServiceImpl extends ServiceImpl<VideoConfigurationMapper, VideoConfiguration> implements VideoConfigurationService {
    @Autowired
    private VideoConfigurationMapper videoConfigurationMapper;
    @Override
    public List<String> getAllTypes() {
        return videoConfigurationMapper.getAllTypes();
    }

    @Override
    public List<String> getAllTowns() {
        return videoConfigurationMapper.getAllTowns();
    }

    @Override
    public List<String> getVillagesByTown(String town) {
        return videoConfigurationMapper.getVillagesByTown(town);
    }

    @Override
    public List<Map<String, Object>> getTree() {
        LambdaQueryWrapper<VideoConfiguration> queryWrapper = new LambdaQueryWrapper<VideoConfiguration>();
        queryWrapper.like(VideoConfiguration::getTown, "水厂")
                .or().like(VideoConfiguration::getTown, "竖井泵站")
                .or().like(VideoConfiguration::getTown, "管理站");
        List<VideoConfiguration> cameras = videoConfigurationMapper.selectList(queryWrapper); // 只获取水厂，竖井泵站，管理站的树
        Map<String, Object> treeMap = new HashMap<>();
        List<Map<String, Object>> rootList = new ArrayList<>();

        for (VideoConfiguration camera : cameras) {
            String town = camera.getTown();
            String village = camera.getVillage();
            Map<String, Object> cameraMap = new HashMap<>();
            cameraMap.put("name", camera.getName());
            cameraMap.put("id", camera.getId());

            if (!treeMap.containsKey(town)) {
                Map<String, Object> townMap = new HashMap<>();
                List<Map<String, Object>> villageList = new ArrayList<>();
                townMap.put("name", town);
                townMap.put("children", villageList);
                rootList.add(townMap);
                treeMap.put(town, villageList);
            }

            List<Map<String, Object>> villageList = (List<Map<String, Object>>) treeMap.get(town);
            if (!villageList.stream().anyMatch(c -> ((String) ((Map<String, Object>) c).get("name")).equals(village))) {
                Map<String, Object> villageMap = new HashMap<>();
                villageMap.put("name", village);
                villageMap.put("children", new ArrayList<>());
                villageList.add(villageMap);
            }

            ((List<Map<String, Object>>) ((Map<String, Object>) villageList.get(villageList.size() - 1)).get("children")).add(cameraMap);
        }

        return rootList;
    }

    @Override
    public List<String> getAllNames() {
        return videoConfigurationMapper.getAllNames();
    }

    @Override
    public Map<String, String> getAllNamesAndCodes() {
        Map<String, String> map = new HashMap<>();
        List<String> nameList = videoConfigurationMapper.getAllNames();
        List<String> codeList = videoConfigurationMapper.getAllCodes();
        for (int i = 0; i < nameList.size(); i++) {
            map.put(nameList.get(i), codeList.get(i));
        }
        return map;
    }


}
