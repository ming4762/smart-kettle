package com.smart.starter.kettle.pool

import com.smart.kettle.core.meta.CustomDatabaseMeta
import com.smart.kettle.core.properties.DatabaseMetaProperties
import com.smart.starter.kettle.properties.KettleDatabaseRepositoryProperties
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.PooledObjectFactory
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.pentaho.di.repository.kdr.KettleDatabaseRepository
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta
import java.util.*
import kotlin.jvm.Throws


/**
 *
 * @author ShiZhongMing
 * 2020/8/24 9:20
 * @since 1.0
 */
class KettlePooledObjectFactory(private val properties: KettleDatabaseRepositoryProperties) : PooledObjectFactory<KettleDatabaseRepository> {

    /**
     * 创建资源库对象
     */
    @Throws(Exception::class)
    override fun makeObject(): PooledObject<KettleDatabaseRepository> {
        val repository = KettleDatabaseRepository()
        // 创建连接信息
        val databaseMeta = CustomDatabaseMeta(properties.name, properties.type,
                properties.access, properties.host, properties.db,
                properties.port, properties.dbUser, properties.dbPassword)
        val kettleDatabaseRepositoryMeta = KettleDatabaseRepositoryMeta("kettle", "kettle", "Transformation description", databaseMeta)
        // 初始化资源库
        repository.init(kettleDatabaseRepositoryMeta)
        //连接资源库
        repository.connect(properties.resUser, properties.resPassword)
        return DefaultPooledObject(repository)
    }

    /**
     * 销毁对象
     */
    @Throws(Exception::class)
    override fun destroyObject(pooledObject: PooledObject<KettleDatabaseRepository?>) {
        Optional.ofNullable(pooledObject.getObject())
                .ifPresent { obj: KettleDatabaseRepository -> obj.disconnect() }
    }

    override fun validateObject(pooledObject: PooledObject<KettleDatabaseRepository>): Boolean {
        val repository = pooledObject.getObject()
        return Objects.nonNull(repository) && repository.isConnected
    }

    @Throws(Exception::class)
    override fun activateObject(pooledObject: PooledObject<KettleDatabaseRepository?>?) {
    }

    @Throws(Exception::class)
    override fun passivateObject(pooledObject: PooledObject<KettleDatabaseRepository?>?) {
    }
}