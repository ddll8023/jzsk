# 部门管理模块实施计划

> 创建日期：2026-03-03

## 任务列表

### Task 1: 创建Dept实体类

**文件：** `backend/szy-new/src/main/java/com/szy/pojo/entity/Department.java`

**内容：**
- id: Long
- departmentName: String
- departmentResponsibility: String
- level: String
- company: String
- createTime: Date
- updateTime: Date

---

### Task 2: 创建DeptDTO和DeptQueryDTO

**文件：**
- `backend/szy-new/src/main/java/com/szy/pojo/dto/DeptDTO.java`
- `backend/szy-new/src/main/java/com/szy/pojo/dto/DeptQueryDTO.java`

**内容：**
- DeptDTO: departmentName, departmentResponsibility, level, company（含@Schema、@NotBlank校验）
- DeptQueryDTO: currentPage, pageSize, departmentName（含@Schema校验）

---

### Task 3: 创建DeptVO

**文件：** `backend/szy-new/src/main/java/com/szy/pojo/vo/DeptVO.java`

**内容：**
- id, departmentName, departmentResponsibility, level, company, createTime, updateTime

---

### Task 4: 创建DeptMapper

**文件：**
- `backend/szy-new/src/main/java/com/szy/mapper/DeptMapper.java`
- `backend/szy-new/src/main/resources/mapper/DeptMapper.xml`

**SQL方法：**
- selectById(Long id)
- selectPage(DeptQueryDTO query)
- insert(Department dept)
- update(Department dept)
- deleteById(Long id)

---

### Task 5: 创建DeptService

**文件：**
- `backend/szy-new/src/main/java/com/szy/service/DeptService.java`
- `backend/szy-new/src/main/java/com/szy/service/impl/DeptServiceImpl.java`

**方法：**
- getById(Long id): DeptVO
- getPage(DeptQueryDTO query): PageResultVO<DeptVO>
- save(DeptDTO dto): DeptVO
- update(DeptDTO dto): DeptVO
- deleteById(Long id): void

---

### Task 6: 创建DeptController

**文件：** `backend/szy-new/src/main/java/com/szy/controller/DeptController.java`

**API：**
- GET /department/list - 列表（分页+名称搜索）
- GET /department/{id} - 详情
- POST /department/save - 新增
- PUT /department/update - 更新
- DELETE /department/{id} - 删除

---

## 执行顺序

1. Task 1 → Task 2 → Task 3 → Task 4 → Task 5 → Task 6
2. 更新 `docs/开发状态.md`
3. Git提交
