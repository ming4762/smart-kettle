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
class LogDatabaseProperties(var enable: Boolean = true) : DatabaseMetaProperties(), InitializingBean {

    companion object {
        private const val DEFAULT_DB_NAME = "LOG_DB"
    }

    /**
     * 转换日志表名称
     */
    val transLogTableName: String? = null

    /**
     * 步骤日志表名称
     */
    val stepLogTableName: String? = null

    /**
     * metrics 日志表名称
     */
    val metricsLogTableName: String? = null

    /**
     * performance日志表名称
     */
    val performanceLogTableName: String? = null

    /**
     * 通道日志表名称
     */
    val channelLogTableTable: String? = null

    /**
     * 验证参数是否正确
     */
    override fun afterPropertiesSet() {
        if (this.enable) {
            this.name = this.name?: DEFAULT_DB_NAME
            Assert.notNull(this.db, "必须指定日志数据库")
        }
    }
}