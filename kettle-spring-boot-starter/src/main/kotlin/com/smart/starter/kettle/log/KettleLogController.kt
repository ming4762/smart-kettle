package com.smart.starter.kettle.log

import com.smart.starter.kettle.log.type.JobLogType
import com.smart.starter.kettle.log.type.LogType
import com.smart.starter.kettle.log.type.TransLogType
import com.smart.starter.kettle.properties.LogDatabaseProperties
import org.pentaho.di.core.database.DatabaseMeta
import org.pentaho.di.core.logging.BaseLogTable
import org.pentaho.di.job.JobMeta
import org.pentaho.di.trans.TransMeta
import org.springframework.util.Assert


/**
 * 日志控制器
 * @author ShiZhongMing
 * 2020/8/24 9:54
 * @since 1.0
 */
class KettleLogController(private val logDatabaseProperties: LogDatabaseProperties) {

    companion object {
        /**
         * 开启trans日志
         */
        @JvmStatic
        @JvmOverloads
        fun enableTransLog(enable: Boolean, tableName: String? = null) {
            this.enableLog(TransLogType.TRANS_LOG, enable, tableName)
        }

        @JvmStatic
        @JvmOverloads
        fun enableStepLog(enable: Boolean, tableName: String? = null) {
            this.enableLog(TransLogType.STEP_LOG, enable, tableName)
        }

        @JvmStatic
        @JvmOverloads
        fun enableMetricsLog(enable: Boolean, tableName: String? = null) {
            this.enableLog(TransLogType.METRICS_LOG, enable, tableName)
        }

        @JvmStatic
        @JvmOverloads
        fun enablePerformanceLog(enable: Boolean, tableName: String? = null) {
            this.enableLog(TransLogType.PERFORMANCE_LOG, enable, tableName)
        }

        @JvmStatic
        @JvmOverloads
        fun enableTransChannelLog(enable: Boolean, tableName: String? = null) {
            this.enableLog(TransLogType.TRANS_CHANNEL_LOG, enable, tableName)
        }

        /**
         * 启停 job日志
         */
        @JvmStatic
        @JvmOverloads
        fun enableJobLog(enable: Boolean, tableName: String? = null) {
            this.enableLog(JobLogType.JOB_LOG, enable, tableName)
        }

        @JvmStatic
        @JvmOverloads
        fun enableJobEntryLog(enable: Boolean, tableName: String? = null) {
            this.enableLog(JobLogType.JOB_ENTRY_LOG, enable, tableName)
        }

        @JvmStatic
        @JvmOverloads
        fun enableJobChannelLog(enable: Boolean, tableName: String? = null) {
            this.enableLog(JobLogType.JOB_CHANNEL_LOG, enable, tableName)
        }

        /**
         * 启用日志
         * @param logType 日志类型
         * @param tableName 日志表名称（可以指定日志表，也可以使用全局配置的日志表）
         */
        @JvmStatic
        private fun enableLog(logType: LogType, enable: Boolean, tableName: String? = null) {
            val config = CustomLogConfigHolder.getConfig(logType)
            config.enable = enable
            config.tableName = tableName
        }
    }

    /**
     * 是否启用日志
     */
    private fun isEnable(): Boolean {
        return this.logDatabaseProperties.enable
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
            if (this.isEnableTransLog(it)) {
                val customConfig = CustomLogConfigHolder.getConfig(it)
                val logTable = it.function.invoke(transMeta) as BaseLogTable
                logTable.connectionName = databaseMeta.name
                logTable.tableName = customConfig.tableName?:this.getGlobalTransLogTableName(it)
                it.setFunction.invoke(transMeta, logTable)
            }
        }
    }

    /**
     * 初始化job日志
     * @param jobMeta job元数据
     */
    fun initJobLog(jobMeta: JobMeta) {
        // 创建数据库元数据
        val databaseMeta = this.createLogDbMeta()
        jobMeta.addDatabase(databaseMeta)
        // 遍历设置日志信息
        JobLogType.values().forEach {
            if (this.isEnableJobLog(it)) {
                val customConfig = CustomLogConfigHolder.getConfig(it)
                val logTable = it.function.invoke(jobMeta) as BaseLogTable
                logTable.connectionName = databaseMeta.name
                logTable.tableName = customConfig.tableName?:this.getGlobalJobLogTableName(it)
                it.setFunction.invoke(jobMeta, logTable)
            }
        }
    }

    /**
     * 是否启用日志
     */
    private fun isEnableTransLog(logType: TransLogType): Boolean {
        // 获取配置信息
        val customConfig = CustomLogConfigHolder.getConfig(logType)
        if (customConfig.enable == true) {
            Assert.notNull(customConfig.tableName?:this.getGlobalTransLogTableName(logType), "必须指定日志表，日志表类型：${logType.name}")
            return true
        } else if (customConfig.enable == null && this.isEnable() && this.getGlobalTransLogTableName(logType) != null) {
            return true
        }
        return false
    }

    /**
     * 是否启用job日志
     */
    private fun isEnableJobLog(logType: JobLogType): Boolean {
        // 获取配置信息
        val customConfig = CustomLogConfigHolder.getConfig(logType)
        if (customConfig.enable == true) {
            Assert.notNull(customConfig.tableName?:this.getGlobalJobLogTableName(logType), "必须指定日志表，日志表类型：${logType.name}")
            return true
        } else if (customConfig.enable == null && this.isEnable() && this.getGlobalJobLogTableName(logType) != null) {
            return true
        }
        return false
    }

    /**
     * 获取全局的日志表名称
     */
    private fun getGlobalTransLogTableName(logType: TransLogType): String? {
        return when (logType) {
            TransLogType.TRANS_LOG -> this.logDatabaseProperties.transLogTableName
            TransLogType.STEP_LOG -> this.logDatabaseProperties.stepLogTableName
            TransLogType.METRICS_LOG -> this.logDatabaseProperties.metricsLogTableName
            TransLogType.PERFORMANCE_LOG -> this.logDatabaseProperties.performanceLogTableName
            TransLogType.TRANS_CHANNEL_LOG -> this.logDatabaseProperties.channelLogTableName
        }
    }

    /**
     * 获取全局的job日志表名称
     */
    private fun getGlobalJobLogTableName(logType: JobLogType): String? {
        return when (logType) {
            JobLogType.JOB_LOG -> this.logDatabaseProperties.jobLogTableName
            JobLogType.JOB_ENTRY_LOG -> this.logDatabaseProperties.jobEntryLogTableName
            JobLogType.JOB_CHANNEL_LOG -> this.logDatabaseProperties.channelLogTableName
        }
    }


    /**
     * 创建日志DatabaseMeta
     * @return DatabaseMeta
     */
    private fun createLogDbMeta(): DatabaseMeta {
        val databaseMeta = DatabaseMeta()
        databaseMeta.name = this.logDatabaseProperties.name?:LogDatabaseProperties.DEFAULT_DB_NAME
        databaseMeta.setDatabaseType(this.logDatabaseProperties.type)
        databaseMeta.hostname = this.logDatabaseProperties.host
        databaseMeta.setDBPort(this.logDatabaseProperties.port)
        databaseMeta.setDBName(this.logDatabaseProperties.db)
        databaseMeta.username = this.logDatabaseProperties.dbUser
        databaseMeta.password = this.logDatabaseProperties.dbPassword
        return databaseMeta
    }
}