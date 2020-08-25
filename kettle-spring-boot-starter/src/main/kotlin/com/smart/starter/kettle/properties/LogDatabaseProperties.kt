package com.smart.starter.kettle.properties

import com.smart.kettle.core.properties.DatabaseMetaProperties
import org.springframework.beans.factory.InitializingBean
import org.springframework.util.Assert


/**
 * 日志数据库配置信息
 * @author ShiZhongMing
 * 2020/8/24 10:32
 * @since 1.0
 */
class LogDatabaseProperties(var enable: Boolean = true) : DatabaseMetaProperties() {

    companion object {
        const val DEFAULT_DB_NAME = "LOG_DB"
    }

    /**
     * 转换日志表名称
     */
    var transLogTableName: String? = null

    /**
     * 步骤日志表名称
     */
    var stepLogTableName: String? = null

    /**
     * metrics 日志表名称
     */
    var metricsLogTableName: String? = null

    /**
     * performance日志表名称
     */
    var performanceLogTableName: String? = null

    /**
     * 通道日志表名称
     */
    var channelLogTableTable: String? = null

}