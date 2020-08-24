package com.smart.starter.kettle.pool.impl

import com.smart.kettle.core.properties.DatabaseMetaProperties
import com.smart.starter.kettle.pool.KettlePooledObjectFactory
import com.smart.starter.kettle.pool.KettleRepositoryProvider
import com.smart.starter.kettle.properties.KettleDatabaseRepositoryProperties
import lombok.extern.slf4j.Slf4j
import org.apache.commons.pool2.impl.GenericObjectPool
import org.pentaho.di.repository.kdr.KettleDatabaseRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean


/**
 * 默认的kettle资源库提供者
 * @author ShiZhongMing
 * 2020/8/24 9:18
 * @since 1.0
 */
@Slf4j
class DefaultKettleRepositoryProviderImpl(private val databaseMetaProperties: KettleDatabaseRepositoryProperties): KettleRepositoryProvider, InitializingBean {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DefaultKettleRepositoryProviderImpl :: class.java)
    }

    /**
     * 资源库连接池
     */
    private lateinit var objectPool: GenericObjectPool<KettleDatabaseRepository>

    /**
     * 获取资源库连接
     */
    override fun getRepository(): KettleDatabaseRepository? {
        if (objectPool.numWaiters > 0) {
            LOGGER.warn("线程池暂无空闲资源库对象")
        }
        return try {
            objectPool.borrowObject()
        } catch (e: Exception) {
            LOGGER.warn("从连接池中获取资源库发生错误", e)
            null
        }
    }

    override fun returnRepository(repository: KettleDatabaseRepository) {
        objectPool.returnObject(repository)
    }

    override fun afterPropertiesSet() {
        // 创建资源库连接池
        val factory = KettlePooledObjectFactory(databaseMetaProperties)
        objectPool = GenericObjectPool(factory)
    }
}