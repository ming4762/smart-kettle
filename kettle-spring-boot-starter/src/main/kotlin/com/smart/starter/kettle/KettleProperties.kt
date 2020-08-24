package com.smart.starter.kettle

import com.smart.kettle.core.properties.DatabaseMetaProperties
import com.smart.starter.kettle.properties.KettleDatabaseRepositoryProperties
import com.smart.starter.kettle.properties.LogDatabaseProperties
import org.springframework.boot.context.properties.ConfigurationProperties


/**
 *
 * @author ShiZhongMing
 * 2020/8/24 9:35
 * @since 1.0
 */
@ConfigurationProperties("smart.kettle")
class KettleProperties {


    /**
     * 数据库资源库连接池
     */
    val repository: KettleDatabaseRepositoryProperties = KettleDatabaseRepositoryProperties()

    /**
     * 日志DB配置
     */
    val logProperties: LogDatabaseProperties = LogDatabaseProperties()

}