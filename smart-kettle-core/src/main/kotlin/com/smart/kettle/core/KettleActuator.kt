package com.smart.kettle.core

import org.apache.commons.lang3.StringUtils
import org.pentaho.di.core.KettleEnvironment
import org.pentaho.di.core.exception.KettleException
import org.pentaho.di.core.logging.LogLevel
import org.pentaho.di.core.util.EnvUtil
import org.pentaho.di.job.Job
import org.pentaho.di.job.JobMeta
import org.pentaho.di.repository.RepositoryDirectoryInterface
import org.pentaho.di.repository.kdr.KettleDatabaseRepository
import org.pentaho.di.trans.Trans
import org.pentaho.di.trans.TransMeta
import java.io.FileInputStream
import java.io.InputStream
import java.util.function.Consumer
import kotlin.jvm.Throws


/**
 * kettle执行器
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
    @JvmStatic
    fun closeConsoleLogging() {
        System.setProperty(KETTLE_DISABLE_CONSOLE_LOGGING, "Y")
    }

    /**
     * 打开控制台日志
     */
    @JvmStatic
    fun openConsoleLogging() {
        System.clearProperty(KETTLE_DISABLE_CONSOLE_LOGGING)
    }

    /**
     * 获取TransMeta
     * @param ktrPath 转换路径
     */
    @JvmStatic
    fun getTransMeta(ktrPath: String): TransMeta {
        this.initKettle()
        return TransMeta(ktrPath)
    }

    /**
     * 获取TransMeta
     * @param inputStream 转换流
     */
    fun getTransMeta(inputStream: InputStream): TransMeta {
        this.initKettle()
        return TransMeta(inputStream, null, true, null, null)
    }

    /**
     * 执行数据库trans
     * @param repository 资源库
     * @param transName 转换名
     * @param directoryName 目录名
     */
    @JvmStatic
    fun getDbTransMeta(repository: KettleDatabaseRepository, transName: String, directoryName: String?): TransMeta {
        this.initKettle()
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
     * @param beforeHandler 执行前回调
     * @throws KettleException 转换异常
     */
    @JvmStatic
    @JvmOverloads
    @Throws(KettleException :: class)
    fun executeTransfer(
            transMeta: TransMeta,
            params: Array<String> = arrayOf(),
            variableMap: Map<String, String> = mapOf(),
            parameter: Map<String, String> = mapOf(),
            logLevel: LogLevel = LogLevel.BASIC,
            beforeHandler: Consumer<Trans>? = null
    ): Trans {
        val trans = Trans(transMeta)
        // 设置变量
        variableMap.forEach { (variableName: String?, variableValue: String?) -> trans.setVariable(variableName, variableValue) }
        // 设置命名参数
        for ((key, value) in parameter) {
            trans.setParameterValue(key, value)
        }
        trans.logLevel = logLevel
        // 执行前回调
        beforeHandler?.accept(trans)
        // 执行转换
        trans.execute(params)
        // 等待转换完成
        trans.waitUntilFinished()

        return trans
    }

    /**
     * 获取资源库JOB MATE
     * @param repository kettle资源库
     * @param jobName job名称
     * @param directoryName 目录
     */
    @JvmStatic
    fun getDbJobMate(repository: KettleDatabaseRepository, jobName: String, directoryName: String?): JobMeta {
        this.initKettle()
        val directoryInterface = getDirectoryInterface(repository, directoryName)
        return repository.loadJob(jobName, directoryInterface, null, null)
    }

    /**
     * 执行job
     * @param repository 资源库
     * @param jobMeta job元数据
     * @param params job参数
     * @param parameterMap 命名参数
     * @param beforeHandler 执行前回调
     * @throws KettleException Exception
     */
    @JvmStatic
    @JvmOverloads
    @Throws(KettleException :: class)
    fun executeJob(
            repository: KettleDatabaseRepository,
            jobMeta: JobMeta,
            params: Map<String, String> = mapOf(),
            parameterMap: Map<String, String> = mapOf(),
            logLevel: LogLevel = LogLevel.BASIC,
            beforeHandler: Consumer<Job>? = null
    ): Job {
        val job = Job(repository, jobMeta)
        params.forEach { (variableName: String?, variableValue: String?) -> job.setVariable(variableName, variableValue) }
        for ((key, value) in parameterMap) {
            job.setParameterValue(key, value)
        }
        // 执行前回调
        beforeHandler?.accept(job)
        job.start()
        job.waitUntilFinished()

        return job
    }


    /**
     * 获取资源路径
     * @param repository 资源库
     * @param directoryName 目录名
     * @return 目录路径
     * @throws KettleException KettleException
     */
    @JvmStatic
    @Throws(KettleException::class)
    private fun getDirectoryInterface(repository: KettleDatabaseRepository, directoryName: String?): RepositoryDirectoryInterface {
        var directoryInterface = repository.loadRepositoryDirectoryTree()
        if (StringUtils.isNotBlank(directoryName)) {
            directoryInterface = directoryInterface.findDirectory(directoryName)
        }
        return directoryInterface
    }

    /**
     * 初始化kettle环境
     */
    private fun initKettle() {
        if (!KettleEnvironment.isInitialized()) {
            // 初始化kettle环境
            KettleEnvironment.init()
            EnvUtil.environmentInit()
        }
    }
}




