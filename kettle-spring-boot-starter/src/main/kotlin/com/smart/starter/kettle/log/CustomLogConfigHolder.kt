package com.smart.starter.kettle.log


/**
 * 自定义配置holder
 * @author ShiZhongMing
 * 2020/8/24 11:06
 * @since 1.0
 */
object CustomLogConfigHolder {

    private val holder: ThreadLocal<MutableMap<LogType, CustomLogConfig>> = ThreadLocal.withInitial {  mutableMapOf<LogType, CustomLogConfig>() }

    /**
     * 获取配置
     */
    @JvmStatic
    fun getConfig(logType: LogType): CustomLogConfig {
        val map = holder.get()
        if (!map.containsKey(logType)) {
            map[logType] = CustomLogConfig()
        }
        return map[logType]!!
    }

}