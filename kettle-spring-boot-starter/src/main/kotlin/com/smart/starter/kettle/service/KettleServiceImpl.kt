package com.smart.starter.kettle.service

import com.smart.kettle.core.KettleActuator
import com.smart.starter.kettle.log.KettleLogController
import com.smart.starter.kettle.pool.KettleRepositoryProvider
import org.pentaho.di.core.exception.KettleException
import org.pentaho.di.core.logging.LogLevel
import org.pentaho.di.job.Job
import org.pentaho.di.repository.kdr.KettleDatabaseRepository
import org.pentaho.di.trans.Trans
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.function.Consumer
import kotlin.jvm.Throws


/**
 * kettle service 实现类
 * @author ShiZhongMing
 * 2020/8/24 10:15
 * @since 1.0
 */
@Service
class KettleServiceImpl(private val kettleRepositoryProvider: KettleRepositoryProvider, private val kettleLogController: KettleLogController) : KettleService {


    /**
     * 执行资源库转换
     * @param transName 转换名
     * @param directoryName 转换所在目录
     * @param params 参数
     * @param variableMap 变量
     * @param parameterMap  命名参数
     * @throws KettleException Exception
     */
    @Throws(KettleException :: class)
    override fun executeDbTransfer(transName: String, directoryName: String?, params: Array<String>, variableMap: Map<String, String>, parameterMap: Map<String, String>, logLevel: LogLevel): Trans {
        return this.executeDbTransfer(transName, directoryName, params, variableMap, parameterMap, logLevel, null)
    }

    override fun executeDbTransfer(transName: String, directoryName: String?, params: Array<String>, variableMap: Map<String, String>, parameterMap: Map<String, String>, logLevel: LogLevel, beforeHandler: Consumer<Trans>?): Trans {
        val repository = this.getRepository()
        // 获取元数据
        val transMeta = KettleActuator.getDbTransMeta(repository, transName, directoryName)
        // 初始化日志
        this.kettleLogController.initTransLog(transMeta)
        val trans = KettleActuator.executeTransfer(transMeta, params, variableMap, parameterMap, logLevel, beforeHandler)
        kettleRepositoryProvider.returnRepository(repository)
        return trans
    }

    /**
     * 执行资源库JOB
     * @param name job名
     * @param directoryName job所在目录
     * @param params 参数
     * @param parameterMap 命名参数
     * @throws KettleException Exception
     */
    override fun executeDbJob(name: String, directoryName: String?, params: Map<String, String>, parameterMap: Map<String, String>, logLevel: LogLevel ): Job {
        return this.executeDbJob(name, directoryName, params, parameterMap, logLevel, null)
    }

    override fun executeDbJob(name: String, directoryName: String?, params: Map<String, String>, parameterMap: Map<String, String>, logLevel: LogLevel, beforeHandler: Consumer<Job>?): Job {
        val repository = this.getRepository()
        // 获取元数据
        val jobMeta = KettleActuator.getDbJobMate(repository, name, directoryName)
        // 初始化日志
        this.kettleLogController.initJobLog(jobMeta)

        val job = KettleActuator.executeJob(repository, jobMeta, params, parameterMap, logLevel, beforeHandler)
        kettleRepositoryProvider.returnRepository(repository)
        return job
    }

    override fun executeFileTransfer(ktrPath: String, params: Array<String>, variableMap: Map<String, String>, parameter: Map<String, String>, logLevel: LogLevel, beforeHandler: Consumer<Trans>?): Trans {
        // 获取元数据
        val transMeta = KettleActuator.getTransMeta(ktrPath)
        // 初始化日志
        this.kettleLogController.initTransLog(transMeta)
        return KettleActuator.executeTransfer(transMeta, params, variableMap, parameter, logLevel, beforeHandler)
    }

    /**
     * 执行文件转换
     */
    override fun executeFileTransfer(ktrPath: String, params: Array<String>, variableMap: Map<String, String>, parameter: Map<String, String>, logLevel: LogLevel): Trans {
        return this.executeFileTransfer(ktrPath, params, variableMap, parameter, logLevel, null)
    }

    /**
     * 执行转换
     * 文件位于classpath
     * @param ktrPath 转换路径
     * @throws KettleException 转换异常
     */
    override fun executeClasspathFileTransfer(ktrPath: String, params: Array<String>, variableMap: Map<String, String>, parameter: Map<String, String>, logLevel: LogLevel): Trans {
        return this.executeClasspathFileTransfer(ktrPath, params, variableMap, parameter, logLevel, null)
    }

    override fun executeClasspathFileTransfer(ktrPath: String, params: Array<String>, variableMap: Map<String, String>, parameter: Map<String, String>, logLevel: LogLevel, beforeHandler: Consumer<Trans>?): Trans {
        val inputStream = this.javaClass.classLoader.getResourceAsStream(ktrPath)
        Assert.notNull(inputStream, "execute trans fail,can not find trans file")
        val transMeta = KettleActuator.getTransMeta(inputStream!!)
        // 初始化日志
        this.kettleLogController.initTransLog(transMeta)
        return KettleActuator.executeTransfer(transMeta, params, variableMap, parameter, logLevel, beforeHandler)
    }

    override fun executeFileJob(path: String, params: Map<String, String>, parameterMap: Map<String, String>, logLevel: LogLevel, beforeHandler: Consumer<Job>?): Job {
        val jobMeta = KettleActuator.getJobMate(path)
        return KettleActuator.executeJob(null, jobMeta, params, parameterMap, logLevel, beforeHandler)
    }

    override fun executeFileJob(path: String, params: Map<String, String>, parameterMap: Map<String, String>, logLevel: LogLevel): Job {
        return this.executeFileJob(path, params, parameterMap, logLevel, null)
    }

    override fun executeClasspathJob(path: String, params: Map<String, String>, parameterMap: Map<String, String>, logLevel: LogLevel, beforeHandler: Consumer<Job>?): Job {
        val inputStream = this.javaClass.classLoader.getResourceAsStream(path)
        Assert.notNull(inputStream, "execute trans fail,can not find job file")
        val jobMeta = KettleActuator.getJobMate(inputStream!!)
        return KettleActuator.executeJob(null, jobMeta, params, parameterMap, logLevel, beforeHandler)
    }

    override fun executeClasspathJob(path: String, params: Map<String, String>, parameterMap: Map<String, String>, logLevel: LogLevel): Job {
        return this.executeClasspathJob(path, params, parameterMap, logLevel, null)
    }

    /**
     * 获取资源库信息
     */
    private fun getRepository(): KettleDatabaseRepository {
        val repository = kettleRepositoryProvider.getRepository()
        Assert.notNull(repository, "获取资源库信息失败，无法执行kettle")
        return repository!!
    }

}
