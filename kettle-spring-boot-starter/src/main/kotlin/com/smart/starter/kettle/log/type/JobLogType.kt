package com.smart.starter.kettle.log.type

import org.pentaho.di.core.logging.BaseLogTable
import org.pentaho.di.job.JobMeta
import org.pentaho.di.trans.TransMeta


/**
 * job 日志类型
 * @author ShiZhongMing
 * 2020/8/24 14:02
 * @since 1.0
 */
enum class JobLogType(val function: (JobMeta) -> Any, val setFunction: (JobMeta, BaseLogTable) -> Unit) : LogType {

    /**
     * job日志
     */
    JOB_LOG(
            { jobMate -> org.pentaho.di.core.logging.JobLogTable.getDefault(jobMate, jobMate) },
            { jobMate, logTable -> jobMate.jobLogTable = logTable as org.pentaho.di.core.logging.JobLogTable }
    ),

    /**
     * 作业项日志
     */
    JOB_ENTRY_LOG(
            { jobMate -> org.pentaho.di.core.logging.JobEntryLogTable.getDefault(jobMate, jobMate) },
            { jobMate, logTable -> jobMate.jobEntryLogTable = logTable as org.pentaho.di.core.logging.JobEntryLogTable }
    ),

    /**
     * job日志通道
     */
    JOB_CHANNEL_LOG(
            { jobMate -> org.pentaho.di.core.logging.ChannelLogTable.getDefault(jobMate, jobMate) },
            { jobMate, logTable -> jobMate.channelLogTable = logTable as org.pentaho.di.core.logging.ChannelLogTable }
    )
}