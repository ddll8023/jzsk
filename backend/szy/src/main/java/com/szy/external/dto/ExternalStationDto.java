package com.szy.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalStationDto {
    private long alarmLevel;
    private Long alarmState;
    private String bindDevice;
    private String collectTime;
    private DeviceInfoList[] deviceInfoList;
    private DeviceVideoList[] deviceVideoList;
    private String h;
    private String icon;
    private String image;
    private String lat;
    private String lon;
    private String monDate;
    private String name;
    private ParamValue paramValue;
    private long projectId;
    private long stationId;
    private String stationMethods;
    private String stationName;
    private long stationType;
    private long stationTypeCode;

    // New fields added based on full JSON response
    private long id;
    private int coordinateType;
    private boolean convert;
    private String bindBaseStation;
    private BindSensor[] bindSensor;
    private double offsetX;
    private double offsetY;
    private String stationTypeGroup;
    private Object bindDeviceList; // Type might need refinement
    private Object stationIndex; // Type might need refinement
    private int mainBaseStation;
    private String monitorItemConfig;
    private String spatialZ;
    private String spatialY;
    private Object protocolDto; // Type might need refinement
    private String spatialX;
    private int alarmStatus;
    private Object alarmStations; // Type might need refinement
    private Object[] bindStationMonitorConfig; // Type might need refinement
    private int status;
    private String planeZ;
    private String planeX;
    private String planeY;
    private String description;
    private String referenceType; // This is a top-level field as well
    private String remark;
    private int delFlag;
    private String monitorMethod;
    private DynamicValue dynamicValue;
    private AlarmValue alarmValue;
    private String updateBy;
    private long stationConfigId;
    private Object ipcsData; // Type might need refinement
    private String sn;
    private double rotateW;
    private Object pictureY; // Type might need refinement
    private int workStatus;
    private Object pictureX; // Type might need refinement
    private Object offlineHour; // Type might need refinement
    private String updateTime;
    private String stationTypeName;
    private String createBy;
    private Object stationTypeGroupCode; // Type might need refinement
    private String createTime;
    private Object x; // Type might need refinement
    private Object y; // Type might need refinement
    private Object z; // Type might need refinement
    private double initElevation;
    private double scalingM;

    public long getAlarmLevel() { return alarmLevel; }
    public void setAlarmLevel(long value) { this.alarmLevel = value; }

    public Long getAlarmState() { return alarmState; }
    public void setAlarmState(Long value) { this.alarmState = value; }

    public String getBindDevice() { return bindDevice; }
    public void setBindDevice(String value) { this.bindDevice = value; }

    public String getCollectTime() { return collectTime; }
    public void setCollectTime(String value) { this.collectTime = value; }

    public DeviceInfoList[] getDeviceInfoList() { return deviceInfoList; }
    public void setDeviceInfoList(DeviceInfoList[] value) { this.deviceInfoList = value; }

    public DeviceVideoList[] getDeviceVideoList() { return deviceVideoList; }
    public void setDeviceVideoList(DeviceVideoList[] value) { this.deviceVideoList = value; }

    public String getH() { return h; }
    public void setH(String value) { this.h = value; }

    public String getIcon() { return icon; }
    public void setIcon(String value) { this.icon = value; }

    public String getImage() { return image; }
    public void setImage(String value) { this.image = value; }

    public String getLat() { return lat; }
    public void setLat(String value) { this.lat = value; }

    public String getLon() { return lon; }
    public void setLon(String value) { this.lon = value; }

    public String getMonDate() { return monDate; }
    public void setMonDate(String value) { this.monDate = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public ParamValue getParamValue() { return paramValue; }
    public void setParamValue(ParamValue value) { this.paramValue = value; }

    public long getProjectId() { return projectId; }
    public void setProjectId(long value) { this.projectId = value; }

    public long getStationId() { return stationId; }
    public void setStationId(long value) { this.stationId = value; }

    public String getStationMethods() { return stationMethods; }
    public void setStationMethods(String value) { this.stationMethods = value; }

    public String getStationName() { return stationName; }
    public void setStationName(String value) { this.stationName = value; }

    public long getStationType() { return stationType; }
    public void setStationType(long value) { this.stationType = value; }

    public long getStationTypeCode() { return stationTypeCode; }
    public void setStationTypeCode(long value) { this.stationTypeCode = value; }

    // Getters and setters for new fields
    public long getId() { return id; }
    public void setId(long value) { this.id = value; }

    public int getCoordinateType() { return coordinateType; }
    public void setCoordinateType(int value) { this.coordinateType = value; }

    public boolean getConvert() { return convert; }
    public void setConvert(boolean value) { this.convert = value; }

    public String getBindBaseStation() { return bindBaseStation; }
    public void setBindBaseStation(String value) { this.bindBaseStation = value; }

    public BindSensor[] getBindSensor() { return bindSensor; }
    public void setBindSensor(BindSensor[] value) { this.bindSensor = value; }

    public double getOffsetX() { return offsetX; }
    public void setOffsetX(double value) { this.offsetX = value; }

    public double getOffsetY() { return offsetY; }
    public void setOffsetY(double value) { this.offsetY = value; }

    public String getStationTypeGroup() { return stationTypeGroup; }
    public void setStationTypeGroup(String value) { this.stationTypeGroup = value; }

    public Object getBindDeviceList() { return bindDeviceList; }
    public void setBindDeviceList(Object value) { this.bindDeviceList = value; }

    public Object getStationIndex() { return stationIndex; }
    public void setStationIndex(Object value) { this.stationIndex = value; }

    public int getMainBaseStation() { return mainBaseStation; }
    public void setMainBaseStation(int value) { this.mainBaseStation = value; }

    public String getMonitorItemConfig() { return monitorItemConfig; }
    public void setMonitorItemConfig(String value) { this.monitorItemConfig = value; }

    public String getSpatialZ() { return spatialZ; }
    public void setSpatialZ(String value) { this.spatialZ = value; }

    public String getSpatialY() { return spatialY; }
    public void setSpatialY(String value) { this.spatialY = value; }

    public Object getProtocolDto() { return protocolDto; }
    public void setProtocolDto(Object value) { this.protocolDto = value; }

    public String getSpatialX() { return spatialX; }
    public void setSpatialX(String value) { this.spatialX = value; }

    public int getAlarmStatus() { return alarmStatus; }
    public void setAlarmStatus(int value) { this.alarmStatus = value; }

    public Object getAlarmStations() { return alarmStations; }
    public void setAlarmStations(Object value) { this.alarmStations = value; }

    public Object[] getBindStationMonitorConfig() { return bindStationMonitorConfig; }
    public void setBindStationMonitorConfig(Object[] value) { this.bindStationMonitorConfig = value; }

    public int getStatus() { return status; }
    public void setStatus(int value) { this.status = value; }

    public String getPlaneZ() { return planeZ; }
    public void setPlaneZ(String value) { this.planeZ = value; }

    public String getPlaneX() { return planeX; }
    public void setPlaneX(String value) { this.planeX = value; }

    public String getPlaneY() { return planeY; }
    public void setPlaneY(String value) { this.planeY = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String value) { this.referenceType = value; }

    public String getRemark() { return remark; }
    public void setRemark(String value) { this.remark = value; }

    public int getDelFlag() { return delFlag; }
    public void setDelFlag(int value) { this.delFlag = value; }

    public String getMonitorMethod() { return monitorMethod; }
    public void setMonitorMethod(String value) { this.monitorMethod = value; }

    public DynamicValue getDynamicValue() { return dynamicValue; }
    public void setDynamicValue(DynamicValue value) { this.dynamicValue = value; }

    public AlarmValue getAlarmValue() { return alarmValue; }
    public void setAlarmValue(AlarmValue value) { this.alarmValue = value; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String value) { this.updateBy = value; }

    public long getStationConfigId() { return stationConfigId; }
    public void setStationConfigId(long value) { this.stationConfigId = value; }

    public Object getIpcsData() { return ipcsData; }
    public void setIpcsData(Object value) { this.ipcsData = value; }

    public String getSn() { return sn; }
    public void setSn(String value) { this.sn = value; }

    public double getRotateW() { return rotateW; }
    public void setRotateW(double value) { this.rotateW = value; }

    public Object getPictureY() { return pictureY; }
    public void setPictureY(Object value) { this.pictureY = value; }

    public int getWorkStatus() { return workStatus; }
    public void setWorkStatus(int value) { this.workStatus = value; }

    public Object getPictureX() { return pictureX; }
    public void setPictureX(Object value) { this.pictureX = value; }

    public Object getOfflineHour() { return offlineHour; }
    public void setOfflineHour(Object value) { this.offlineHour = value; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String value) { this.updateTime = value; }

    public String getStationTypeName() { return stationTypeName; }
    public void setStationTypeName(String value) { this.stationTypeName = value; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String value) { this.createBy = value; }

    public Object getStationTypeGroupCode() { return stationTypeGroupCode; }
    public void setStationTypeGroupCode(Object value) { this.stationTypeGroupCode = value; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String value) { this.createTime = value; }

    public Object getX() { return x; }
    public void setX(Object value) { this.x = value; }

    public Object getY() { return y; }
    public void setY(Object value) { this.y = value; }

    public Object getZ() { return z; }
    public void setZ(Object value) { this.z = value; }

    public double getInitElevation() { return initElevation; }
    public void setInitElevation(double value) { this.initElevation = value; }

    public double getScalingM() { return scalingM; }
    public void setScalingM(double value) { this.scalingM = value; }

    // Nested classes for paramValue
    public static class ParamValue {
        private long deformMode;
        @JsonProperty("CalculationTime")
        private long calculationTime;
        private boolean advancedConfigEnable;
        @JsonProperty("TropMode")
        private long tropMode;
        private CoordTrans coordTrans;
        private String referenceType;
        @JsonProperty("StaticMonitoringEnable")
        private boolean staticMonitoringEnable;
        private long dynamicSaveInterval;
        @JsonProperty("SmallDeforMode")
        private long smallDeforMode;
        @JsonProperty("EleCutOff")
        private long eleCutOff;
        @JsonProperty("StaticMode")
        private long staticMode;
        private long memsEnable;
        @JsonProperty("Constellations")
        private int[] constellations;
        @JsonProperty("SolveFrequency")
        private long solveFrequency;
        private long offsetX;
        private long dataQuality;
        private long offsetY;
        private long dynamicMode;
        @JsonProperty("IonoMode")
        private long ionoMode;
        @JsonProperty("SampleInterval")
        private long sampleInterval;
        private long rotateW;
        private long averagingDuration;
        private long scalingM;

        // Getters and Setters for ParamValue fields
        public long getDeformMode() { return deformMode; }
        public void setDeformMode(long value) { this.deformMode = value; }

        public long getCalculationTime() { return calculationTime; }
        public void setCalculationTime(long value) { this.calculationTime = value; }

        public boolean getAdvancedConfigEnable() { return advancedConfigEnable; }
        public void setAdvancedConfigEnable(boolean value) { this.advancedConfigEnable = value; }

        public long getTropMode() { return tropMode; }
        public void setTropMode(long value) { this.tropMode = value; }

        public CoordTrans getCoordTrans() { return coordTrans; }
        public void setCoordTrans(CoordTrans value) { this.coordTrans = value; }

        public String getReferenceType() { return referenceType; }
        public void setReferenceType(String value) { this.referenceType = value; }

        public boolean getStaticMonitoringEnable() { return staticMonitoringEnable; }
        public void setStaticMonitoringEnable(boolean value) { this.staticMonitoringEnable = value; }

        public long getDynamicSaveInterval() { return dynamicSaveInterval; }
        public void setDynamicSaveInterval(long value) { this.dynamicSaveInterval = value; }

        public long getSmallDeforMode() { return smallDeforMode; }
        public void setSmallDeforMode(long value) { this.smallDeforMode = value; }

        public long getEleCutOff() { return eleCutOff; }
        public void setEleCutOff(long value) { this.eleCutOff = value; }

        public long getStaticMode() { return staticMode; }
        public void setStaticMode(long value) { this.staticMode = value; }

        public long getMemsEnable() { return memsEnable; }
        public void setMemsEnable(long value) { this.memsEnable = value; }

        public int[] getConstellations() { return constellations; }
        public void setConstellations(int[] value) { this.constellations = value; }

        public long getSolveFrequency() { return solveFrequency; }
        public void setSolveFrequency(long value) { this.solveFrequency = value; }

        public long getOffsetX() { return offsetX; }
        public void setOffsetX(long value) { this.offsetX = value; }

        public long getDataQuality() { return dataQuality; }
        public void setDataQuality(long value) { this.dataQuality = value; }

        public long getOffsetY() { return offsetY; }
        public void setOffsetY(long value) { this.offsetY = value; }

        public long getDynamicMode() { return dynamicMode; }
        public void setDynamicMode(long value) { this.dynamicMode = value; }

        public long getIonoMode() { return ionoMode; }
        public void setIonoMode(long value) { this.ionoMode = value; }

        public long getSampleInterval() { return sampleInterval; }
        public void setSampleInterval(long value) { this.sampleInterval = value; }

        public long getRotateW() { return rotateW; }
        public void setRotateW(long value) { this.rotateW = value; }

        public long getAveragingDuration() { return averagingDuration; }
        public void setAveragingDuration(long value) { this.averagingDuration = value; }

        public long getScalingM() { return scalingM; }
        public void setScalingM(long value) { this.scalingM = value; }
    }

    public static class CoordTrans {
        private FourParams fourParams;
        private ElevationFit elevationFit;
        private TgoHorizontal tgoHorizontal;
        private Projection projection;
        private Tool tool;

        // Getters and Setters for CoordTrans fields
        public FourParams getFourParams() { return fourParams; }
        public void setFourParams(FourParams value) { this.fourParams = value; }

        public ElevationFit getElevationFit() { return elevationFit; }
        public void setElevationFit(ElevationFit value) { this.elevationFit = value; }

        public TgoHorizontal getTgoHorizontal() { return tgoHorizontal; }
        public void setTgoHorizontal(TgoHorizontal value) { this.tgoHorizontal = value; }

        public Projection getProjection() { return projection; }
        public void setProjection(Projection value) { this.projection = value; }

        public Tool getTool() { return tool; }
        public void setTool(Tool value) { this.tool = value; }
    }

    public static class FourParams {
        private Params params;
        private boolean enabled;

        // Getters and Setters
        public Params getParams() { return params; }
        public void setParams(Params value) { this.params = value; }

        public boolean getEnabled() { return enabled; }
        public void setEnabled(boolean value) { this.enabled = value; }
    }

    public static class ElevationFit {
        private Params2 params;
        private boolean enabled;

        // Getters and Setters
        public Params2 getParams() { return params; }
        public void setParams(Params2 value) { this.params = value; }

        public boolean getEnabled() { return enabled; }
        public void setEnabled(boolean value) { this.enabled = value; }
    }

    public static class TgoHorizontal {
        private Params3 params;
        private boolean enabled;

        // Getters and Setters
        public Params3 getParams() { return params; }
        public void setParams(Params3 value) { this.params = value; }

        public boolean getEnabled() { return enabled; }
        public void setEnabled(boolean value) { this.enabled = value; }
    }

    public static class Projection {
        private String type;
        private Params4 params;
        private boolean enabled;

        // Getters and Setters
        public String getType() { return type; }
        public void setType(String value) { this.type = value; }

        public Params4 getParams() { return params; }
        public void setParams(Params4 value) { this.params = value; }

        public boolean getEnabled() { return enabled; }
        public void setEnabled(boolean value) { this.enabled = value; }
    }

    public static class Tool {
        private boolean enabled;

        // Getter and Setter
        public boolean getEnabled() { return enabled; }
        public void setEnabled(boolean value) { this.enabled = value; }
    }

    public static class Params {
        private long r;
        private long dx;
        private long dy;
        private long k;

        // Getters and Setters
        public long getR() { return r; }
        public void setR(long value) { this.r = value; }

        public long getDx() { return dx; }
        public void setDx(long value) { this.dx = value; }

        public long getDy() { return dy; }
        public void setDy(long value) { this.dy = value; }

        public long getK() { return k; }
        public void setK(long value) { this.k = value; }
    }

    public static class Params2 {
        private long a1;
        private long a2;
        private long y0;
        private long x0;
        private long a0;

        // Getters and Setters
        public long getA1() { return a1; }
        public void setA1(long value) { this.a1 = value; }

        public long getA2() { return a2; }
        public void setA2(long value) { this.a2 = value; }

        public long getY0() { return y0; }
        public void setY0(long value) { this.y0 = value; }

        public long getX0() { return x0; }
        public void setX0(long value) { this.x0 = value; }

        public long getA0() { return a0; }
        public void setA0(long value) { this.a0 = value; }
    }

    public static class Params3 {
        private long r;
        private long dx;
        private long dy;
        private long y0;
        private long x0;
        private long k;

        // Getters and Setters
        public long getR() { return r; }
        public void setR(long value) { this.r = value; }

        public long getDx() { return dx; }
        public void setDx(long value) { this.dx = value; }

        public long getDy() { return dy; }
        public void setDy(long value) { this.dy = value; }

        public long getY0() { return y0; }
        public void setY0(long value) { this.y0 = value; }

        public long getX0() { return x0; }
        public void setX0(long value) { this.x0 = value; }

        public long getK() { return k; }
        public void setK(long value) { this.k = value; }
    }

    public static class Params4 {
        @JsonProperty("cen_meridian")
        private long cenMeridian;
        @JsonProperty("ave_lat")
        private long aveLat;
        @JsonProperty("north_default")
        private long northDefault;
        @JsonProperty("if_north")
        private long ifNorth;
        private long scale;
        @JsonProperty("if_east")
        private long ifEast;
        @JsonProperty("ed_height")
        private long edHeight;
        @JsonProperty("east_default")
        private long eastDefault;

        // Getters and Setters
        public long getCenMeridian() { return cenMeridian; }
        public void setCenMeridian(long value) { this.cenMeridian = value; }

        public long getAveLat() { return aveLat; }
        public void setAveLat(long value) { this.aveLat = value; }

        public long getNorthDefault() { return northDefault; }
        public void setNorthDefault(long value) { this.northDefault = value; }

        public long getIfNorth() { return ifNorth; }
        public void setIfNorth(long value) { this.ifNorth = value; }

        public long getScale() { return scale; }
        public void setScale(long value) { this.scale = value; }

        public long getIfEast() { return ifEast; }
        public void setIfEast(long value) { this.ifEast = value; }

        public long getEdHeight() { return edHeight; }
        public void setEdHeight(long value) { this.edHeight = value; }

        public long getEastDefault() { return eastDefault; }
        public void setEastDefault(long value) { this.eastDefault = value; }
    }
} 