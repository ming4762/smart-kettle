package com.smart.starter.kettle.service

import org.pentaho.di.core.exception.KettleException
import org.pentaho.di.core.logging.LogLevel
import org.pentaho.di.job.Job
import org.pentaho.di.trans.Trans
import org.springframework.lang.NonNull
import org.springframework.util.Assert
import java.io.File
import java.io.IOException
import kotlin.jvm.Throws

/**
 * kettle 服务
 * @author shizhongming
 * 2020/7/16 8:54 下午
 */
interface KettleService {
    /**
     * 执行资源库转换
     * @param transName 转换名
     * @param directoryName 转换所在目录
     * @param params 参数
     * @param variableMap 变量
     * @param parameterMap  命名参数
     * @throws KettleException Exception
     */
    @Throws(KettleException::class)
    fun executeDbTransfer(transName: String, directoryName: String?, params: Array<String> = arrayOf(), variableMap: Map<String, String> = mapOf(), parameterMap: Map<String, String> = mapOf(), logLevel: LogLevel = LogLevel.BASIC): Trans


    /**
     * 执行资源库JOB
     * @param name job名
     * @throws KettleException Exception
     */
    @Throws(KettleException::class)
    fun executeDbJob(name: String, directoryName: String? = null, params: Map<String, String> = mapOf(), @NonNull parameterMap: Map<String, String> = mapOf(), logLevel: LogLevel = LogLevel.BASIC): Job

    /**
     * 执行转换
     * @param ktrPath 转换路径
     * @throws KettleException 转换异常
     */
    @Throws(KettleException::class)
    fun executeFileTransfer(@NonNull ktrPath: String, params: Array<String> = arrayOf(), variableMap: Map<String, String> = mapOf(), parameter: Map<String, String> = mapOf(), logLevel: LogLevel = LogLevel.BASIC): Trans

    /**
     * 执行转换
     * 文件位于classpath
     * @param ktrPath 转换路径
     * @throws KettleException 转换异常
     */
    @Throws(IOException::class, KettleException::class)
    fun executeClasspathFileTransfer(ktrPath: String, params: Array<String> = arrayOf(), variableMap: Map<String, String> = mapOf(), parameter: Map<String, String> = mapOf(), logLevel: LogLevel = LogLevel.BASIC): Trans

}
