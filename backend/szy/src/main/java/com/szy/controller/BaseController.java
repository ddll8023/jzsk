package com.szy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.entity.Dict;
import com.szy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.ServletRequestUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 基类控制器
 */
public class BaseController {
    @Autowired
    HttpServletRequest req;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleAuthorityService roleAuthorityService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    AdministrationDivisionService administrationDivisionService;

    @Autowired
    WaterSupplyProjectService waterSupplyProjectService;

    @Autowired
    PumpStationService pumpStationService;

    @Autowired
    WaterDistributorService waterDistributorService;


    @Autowired
    ReservoirService reservoirService;

    @Autowired
    WaterworksService waterworksService;

    @Autowired
    ImpoundmentService impoundmentService;

    @Autowired
    SurfaceWaterSourcesService surfaceWaterSourcesService;

    @Autowired
    GroundSourceWaterService groundSourceWaterService;

    @Autowired
    IndividualPressureSitesService individualPressureSitesService;

    @Autowired
    IndividualFlowSitesService individualFlowSitesService;

    @Autowired
    MeasuringStationService measuringStationService;
    @Autowired
    MeasuringItemService measuringItemService;

    @Autowired
    PersonService personService;

    @Autowired
    InspectionRecordsService inspectionRecordsService;

    @Autowired
    EventsService eventsService;

    @Autowired
    WarningInformationService warningInformationService;

    @Autowired
    WarningIndicatorSettingService warningIndicatorSettingService;

    @Autowired
    VideoConfigurationService videoConfigurationService;

    @Autowired
    MaintenceRecordService maintenceRecordService;

    @Autowired
    WaterLevelService waterLevelService;

    @Autowired
    FlowService flowService;

    @Autowired
    WaterQualityService waterQualityService;

    @Autowired
    LineService lineService;

    @Autowired
    HerbService herbService;

    @Autowired
    FloatingBoatService floatingBoatService;

    @Autowired
    DictService dictService;

    @Autowired
    DictDetailService dictDetailService;

    @Autowired
    TownService townService;

    @Autowired
    IconService iconService;

    @Autowired
    PumpService pumpService;
    /**
     * 获取页码
     * @return Page(current,size)
     */
    public Page getPage(){
        int current = ServletRequestUtils.getIntParameter(req,"current",1);
        int size = ServletRequestUtils.getIntParameter(req,"size",10);
        return new Page(current,size);
    }

}
