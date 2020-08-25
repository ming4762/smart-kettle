package com.smart.starter.kettle.log.type

import org.pentaho.di.core.logging.BaseLogTable
import org.pentaho.di.core.logging.ChannelLogTable
import org.pentaho.di.trans.TransMeta


/**
 * 日志类型
 * @author ShiZhongMing
 * 2020/8/24 13:35
 * @since 1.0
 */
enum class TransLogType(val function: (TransMeta) -> Any, val setFunction: (TransMeta, BaseLogTable) -> Unit): LogType {

    /**
     * 转换日志
     */
    TRANS_LOG(
            {transMeta -> org.pentaho.di.core.logging.TransLogTable.getDefault(transMeta, transMeta, transMeta.steps) },
            { transMeta, logTable -> transMeta.transLogTable = logTable as org.pentaho.di.core.logging.TransLogTable }
    ),

    /**
     * 步骤日志
     */
    STEP_LOG(
            {transMeta -> org.pentaho.di.core.logging.StepLogTable.getDefault(transMeta, transMeta) },
            { transMeta, logTable -> transMeta.stepLogTable = logTable as org.pentaho.di.core.logging.StepLogTable }
    ),

    /**
     * METRICS_LOG
     */
    METRICS_LOG({transMeta -> org.pentaho.di.core.logging.MetricsLogTable.getDefault(transMeta, transMeta) },
            { transMeta, logTable -> transMeta.metricsLogTable = logTable as org.pentaho.di.core.logging.MetricsLogTable }),

    /**
     * 性能日志
     */
    PERFORMANCE_LOG({transMeta -> org.pentaho.di.core.logging.PerformanceLogTable.getDefault(transMeta, transMeta) },
            { transMeta, logTable -> transMeta.performanceLogTable = logTable as org.pentaho.di.core.logging.PerformanceLogTable }),

    /**
     * 转换日志通道
     */
    TRANS_CHANNEL_LOG({transMeta -> ChannelLogTable.getDefault(transMeta, transMeta) },
            { transMeta, logTable -> transMeta.channelLogTable = logTable as org.pentaho.di.core.logging.ChannelLogTable });


}