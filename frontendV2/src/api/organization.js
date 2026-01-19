/**
 * 机构管理 API
 * 功能：机构信息的增删改查接口
 * 遵循原则：KISS - 简洁实现，接口定义清晰
 */
import request from '@/utils/request'

/**
 * 获取机构列表（分页）
 * @param {Object} params - 查询参数
 * @param {number} params.currentPage - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.name - 机构名称（可选，用于搜索）
 * @returns {Promise} 机构列表数据
 */
export function getOrgList(params) {
  return request({
    url: '/organization/list',
    method: 'get',
    params
  })
}

/**
 * 获取机构详情
 * @param {number|string} id - 机构ID
 * @returns {Promise} 机构详细信息
 */
export function getOrgInfo(id) {
  return request({
    url: `/organization/info/${id}`,
    method: 'get'
  })
}

/**
 * 新增机构
 * @param {Object} data - 机构信息
 * @param {string} data.organizationName - 机构名称（必填）
 * @param {string} data.organizationCode - 机构代码
 * @param {string} data.administrativeName - 行政区划名称
 * @param {string} data.organizationAbbr - 机构简称
 * @param {string} data.legalRepresentative - 法人代表
 * @param {string} data.agencySpecifications - 机构规格
 * @param {string} data.subordinateRelations - 隶属关系
 * @param {string} data.institutionalType - 机构类型
 * @param {string} data.mainFunction - 主要职能
 * @param {string} data.approveContent - 主要审批内容
 * @param {string} data.website - 网站
 * @param {string} data.email - 邮箱
 * @param {string} data.address - 地址
 * @param {string} data.postalCode - 邮政编码
 * @param {string} data.officeTelephone - 办公室电话
 * @param {string} data.fax - 传真
 * @param {string} data.staffSize - 编制人数
 * @param {string} data.whetherReform - 是否施行水务改革
 * @returns {Promise} 新增结果
 */
export function saveOrg(data) {
  return request({
    url: '/organization/save',
    method: 'post',
    data
  })
}

/**
 * 更新机构信息
 * @param {Object} data - 机构信息（包含id）
 * @returns {Promise} 更新结果
 */
export function updateOrg(data) {
  return request({
    url: '/organization/update',
    method: 'post',
    data
  })
}

/**
 * 删除机构
 * @param {number|string} id - 机构ID
 * @returns {Promise} 删除结果
 */
export function deleteOrg(id) {
  return request({
    url: `/organization/delete/${id}`,
    method: 'post'
  })
}
