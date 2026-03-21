/**
 * V2 API 契约辅助方法
 * 功能：统一分页参数与动作型请求体结构
 */

export function normalizePageParams(params = {}) {
  if (!params || typeof params !== 'object') {
    return {}
  }

  const {
    currentPage,
    pageSize,
    current,
    page,
    size,
    ...rest
  } = params

  const normalized = { ...rest }
  const normalizedPage = page ?? current ?? currentPage
  const normalizedSize = size ?? pageSize

  if (normalizedPage !== undefined && normalizedPage !== null && normalizedPage !== '') {
    normalized.page = normalizedPage
  }

  if (normalizedSize !== undefined && normalizedSize !== null && normalizedSize !== '') {
    normalized.size = normalizedSize
  }

  return normalized
}

export function buildIdPayload(id, extra = {}) {
  return {
    ...extra,
    id
  }
}

export function buildIdsPayload(ids, extra = {}) {
  return {
    ...extra,
    ids
  }
}
