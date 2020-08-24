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
import kotlin.jvm.Throws


/**
 * kettle service 实现
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
        val repository = this.getRepository()
        // 获取元数据
        val transMeta = KettleActuator.getDbTransMeta(repository, transName, directoryName)
        // 初始化日志
        this.kettleLogController.initTransLog(transMeta)
        val trans = KettleActuator.executeTransfer(transMeta, params, variableMap, parameterMap, logLevel)
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
        val repository = this.getRepository()
        // 获取元数据
        val jobMeta = KettleActuator.getDbJobMate(repository, name, directoryName)
        // 初始化日志
        this.kettleLogController.initJobLog(jobMeta)

        val job = KettleActuator.executeJob(repository, jobMeta, params, parameterMap, logLevel)
        kettleRepositoryProvider.returnRepository(repository)
        return job
    }

    /**
     * 执行文件转换
     */
    override fun executeFileTransfer(ktrPath: String, params: Array<String>, variableMap: Map<String, String>, parameter: Map<String, String>, logLevel: LogLevel): Trans {
        // 获取元数据
        val transMeta = KettleActuator.getTransMeta(ktrPath)
        // 初始化日志
        this.kettleLogController.initTransLog(transMeta)
        return KettleActuator.executeTransfer(transMeta, params, variableMap, parameter, logLevel)
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