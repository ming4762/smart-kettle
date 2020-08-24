package com.smart.starter.kettle.pool

import org.pentaho.di.repository.kdr.KettleDatabaseRepository


/**
 * kettle资源库提供器
 * @author ShiZhongMing
 * 2020/8/24 9:17
 * @since 1.0
 */
interface KettleRepositoryProvider {

    /**
     * 获取资源库
     * @return kettle资源库
     */
    fun getRepository(): KettleDatabaseRepository?

    /**
     * 归还资源库连接池
     * @param repository kettle资源库
     */
    fun returnRepository(repository: KettleDatabaseRepository)
}