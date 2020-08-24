package com.smart.starter.kettle.log

import com.smart.starter.kettle.properties.LogDatabaseProperties
import org.pentaho.di.core.database.DatabaseMeta
import org.pentaho.di.core.logging.BaseLogTable
import org.pentaho.di.job.JobMeta
import org.pentaho.di.trans.TransMeta
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Component
import org.springframework.util.Assert


/**
 * 日志控制器
 * @author ShiZhongMing
 * 2020/8/24 9:54
 * @since 1.0
 */
@Component
class KettleLogController(private val logDatabaseProperties: LogDatabaseProperties) {



    /**
     * 是否启用日志
     */
    private fun isEnable(): Boolean {
        return this.logDatabaseProperties.enable
    }

    /**
     * 开启trans日志
     */
    fun enableTransLog(tableName: String?) {
        this.enableLog(TransLogType.TRANS_LOG, tableName)
    }

    fun enableStepLog(tableName: String?) {
        this.enableLog(TransLogType.STEP_LOG, tableName)
    }

    fun enableMetricsLog(tableName: String?) {
        this.enableLog(TransLogType.METRICS_LOG, tableName)
    }

    fun enablePerformanceLog(tableName: String?) {
        this.enableLog(TransLogType.PERFORMANCE_LOG, tableName)
    }

    fun enableChannelLog(tableName: String?) {
        this.enableLog(TransLogType.TRANS_CHANNEL_LOG, tableName)
    }

    /**
     * 启用日志
     * @param logType 日志类型
     * @param tableName 日志表名称（可以指定日志表，也可以使用全局配置的日志表）
     */
    private fun enableLog(logType: TransLogType, tableName: String?) {
        val config = CustomLogConfigHolder.getConfig(logType)
        config.enable = true
        config.tableName = tableName
    }


    /**
     * 初始化转换日志
     * @param transMeta 转换元数据
     */
    fun initTransLog(transMeta: TransMeta) {
        // 创建数据库元数据
        val databaseMeta = this.createLogDbMeta()
        transMeta.addDatabase(databaseMeta)
        // 遍历设置日志信息
        TransLogType.values().forEach {
            if (this.isEnableLog(it)) {
                val customConfig = CustomLogConfigHolder.getConfig(it)
                val logTable = it.function.invoke(transMeta) as BaseLogTable
                logTable.connectionName = databaseMeta.name
                logTable.tableName = customConfig.tableName?:this.logDatabaseProperties.transLogTableName
                it.setFunction.invoke(transMeta, logTable)
            }
        }
    }

    /**
     * 初始化job日志
     * @param jobMeta job元数据
     */
    fun initJobLog(jobMeta: JobMeta) {

    }

    /**
     * 是否启用日志
     */
    private fun isEnableLog(logType: TransLogType): Boolean {
        // 获取配置信息
        val customConfig = CustomLogConfigHolder.getConfig(logType)
        if (customConfig.enable == true) {
            Assert.notNull(customConfig.tableName?:this.logDatabaseProperties.transLogTableName, "必须指定日志表，日志表类型：${logType.name}")
            return true
        } else if (customConfig.enable == null && this.isEnable() && this.logDatabaseProperties.transLogTableName != null) {
            return true
        }
        return false
    }


    /**
     * 创建日志DatabaseMeta
     * @return DatabaseMeta
     */
    private fun createLogDbMeta(): DatabaseMeta {
        val databaseMeta = DatabaseMeta()
        BeanUtils.copyProperties(this.logDatabaseProperties, databaseMeta)
        return databaseMeta
    }
}