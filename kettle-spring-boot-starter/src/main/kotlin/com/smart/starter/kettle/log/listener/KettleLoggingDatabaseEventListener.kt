package com.smart.starter.kettle.log.listener

import org.pentaho.di.core.logging.KettleLoggingEvent
import org.pentaho.di.core.logging.KettleLoggingEventListener


/**
 * kettle 数据库事件监听器
 * @author ShiZhongMing
 * 2020/8/25 10:50
 * @since 1.0
 */
class KettleLoggingDatabaseEventListener : KettleLoggingEventListener {
    override fun eventAdded(kettleLoggingEvent: KettleLoggingEvent) {
        TODO("Not yet implemented")
    }
}