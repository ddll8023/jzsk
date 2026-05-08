# sqlV2 精简数据库脚本

## 说明

本目录基于 `backendV2/数据库表使用清单.md` 生成，只保留 `backendV2` 当前真实引用的本地 MySQL 数据库表。

旧版整库导出包含大量未被当前后端直接访问的冗余表，已从 `sql/` 根目录移除。后续本地 MySQL 数据库初始化以本目录脚本为准。

## 文件范围

| 文件 | 数据库 | 保留表 |
| --- | --- | --- |
| `gcdd.sql` | `gcdd` | `dict`、`dict_detail`、`duty_log`、`duty_schedule`、`inspection_records`、`maintence_record`、`measuring_item`、`measuring_station`、`seepage_data`、`warning_facilities` |
| `jcxx.sql` | `jcxx` | `authority`、`department`、`organization`、`person`、`role`、`role_authority`、`user`、`user_role` |
| `yjxx.sql` | `yjxx` | `warning_indicator_setting`、`warning_information` |

## 未包含内容

- `eb_database.sql` 当前未被 `backendV2` 引用，已移除。
- 非本地数据源脚本不纳入 `sqlV2`，包括 PostgreSQL、SQL Server 以及其他外部库。
