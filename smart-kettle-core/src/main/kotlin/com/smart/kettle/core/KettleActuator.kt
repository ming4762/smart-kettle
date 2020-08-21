package com.smart.kettle.core

import org.apache.commons.lang3.StringUtils
import org.pentaho.di.core.KettleEnvironment
import org.pentaho.di.core.exception.KettleException
import org.pentaho.di.core.logging.LogLevel
import org.pentaho.di.core.util.EnvUtil
import org.pentaho.di.repository.RepositoryDirectoryInterface
import org.pentaho.di.repository.kdr.KettleDatabaseRepository
import org.pentaho.di.trans.Trans
import org.pentaho.di.trans.TransMeta
import org.springframework.lang.NonNull
import kotlin.jvm.Throws


/**
 *
 * @author ShiZhongMing
 * 2020/8/21 16:07
 * @since 1.0
 */
object KettleActuator {

    /**
     * 不显示控制台日志
     */
    private const val KETTLE_DISABLE_CONSOLE_LOGGING = "KETTLE_DISABLE_CONSOLE_LOGGING"


    /**
     * 关闭控制台日志
     */
    fun closeConsoleLogging() {
        System.setProperty(KETTLE_DISABLE_CONSOLE_LOGGING, "Y")
    }

    /**
     * 打开控制台日志
     */
    fun openConsoleLogging() {
        System.clearProperty(KETTLE_DISABLE_CONSOLE_LOGGING)
    }

    /**
     * 获取TransMeta
     * @param ktrPath 转换路径
     */
    fun getTransMeta(ktrPath: String): TransMeta {
        // 初始化kettle环境
        KettleEnvironment.init()
        EnvUtil.environmentInit()
        return TransMeta(ktrPath)
    }

    /**
     * 执行数据库trans
     * @param repository 资源库
     * @param transName 转换名
     * @param directoryName 目录名
     * @param params 参数
     * @param variableMap 变量
     * @param parameterMap 命名参数
     * @throws KettleException KettleException
     */
    fun getDbTransMeta(repository: KettleDatabaseRepository, transName: String, directoryName: String?): TransMeta {
        val directoryInterface: RepositoryDirectoryInterface = getDirectoryInterface(repository, directoryName)
        // 获取转换
        return repository.loadTransformation(transName, directoryInterface, null, true, null)
    }

    /**
     * 执行转换
     * @param transMeta 转换参数
     * @param params 参数
     * @param variableMap 变量
     * @param parameter 命名参数
     * @param logLevel 日志级别
     * @throws KettleException 转换异常
     */
    @Throws(KettleException :: class)
    fun executeTransfer(
            transMeta: TransMeta,
            params: Array<String> = arrayOf(),
            variableMap: Map<String, String> = mapOf(),
            parameter: Map<String, String> = mapOf(),
            logLevel: LogLevel = LogLevel.BASIC
    ): Trans {
        val trans = Trans(transMeta)
        // 设置变量
        variableMap.forEach { (variableName: String?, variableValue: String?) -> trans.setVariable(variableName, variableValue) }
        // 设置命名参数
        for ((key, value) in parameter) {
            trans.setParameterValue(key, value)
        }
        trans.logLevel = logLevel
        // 执行转换
        trans.execute(params)
        // 等待转换完成
        trans.waitUntilFinished()
        if (trans.errors > 0) {
            throw KettleException("There are errors during transformation exception!(传输过程中发生异常)")
        }
        return trans;
    }


    /**
     * 获取资源路径
     * @param repository 资源库
     * @param directoryName 目录名
     * @return 目录路径
     * @throws KettleException KettleException
     */
    @Throws(KettleException::class)
    private fun getDirectoryInterface(repository: KettleDatabaseRepository, directoryName: String?): RepositoryDirectoryInterface {
        var directoryInterface = repository.loadRepositoryDirectoryTree()
        if (StringUtils.isNotBlank(directoryName)) {
            directoryInterface = directoryInterface.findDirectory(directoryName)
        }
        return directoryInterface
    }
}




