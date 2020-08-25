package com.smart.starter.kettle.log.type


/**
 * job 日志类型
 * @author ShiZhongMing
 * 2020/8/24 14:02
 * @since 1.0
 */
enum class JobLogType : LogType {

    /**
     * job日志
     */
    JOB_LOG,

    /**
     * 作业项日志
     */
    JOB_ENTRY_LOG,

    /**
     * job日志通道
     */
    JOB_CHANNEL_LOG
}