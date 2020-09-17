```yaml

smart:
  kettle:
#    数据库资源连接池配置
    repository:
#      数据库类型
      type: mysql
#      连接方式
      access: jdbc
#      连接名称（必须）
      name: null
      host: localhost
#      数据库名称
      db: null
      port: 3306
#      数据库账号
      dbUser: root
#      数据库密码
      dbPassword: null
#      资源库账号
      resUser: admin
#      资源库密码
      resPassword: admin
#    全局日志配置（可以通过KettleLogController单独启用关闭日志）
    logProperties:
#      是否启用日志功能
      enable: true
#      转换日志表名称
      transLogTableName:
#     步骤日志表名称
      stepLogTableName:
#      metrics 日志表名称
      metricsLogTableName:
#      performance日志表名称
      performanceLogTableName:
#      通道日志表名称
      channelLogTableName:
#      job日志
      jobLogTableName:
#      job entry日志
      jobEntryLogTableName:

```
