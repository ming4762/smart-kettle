package com.smart.starter.kettle

import com.smart.starter.kettle.log.KettleLogController
import com.smart.starter.kettle.pool.KettleRepositoryProvider
import com.smart.starter.kettle.pool.impl.DefaultKettleRepositoryProviderImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


/**
 * kettle自动配置类
 * @author ming
 * 2020/8/24 9:13
 * @since 1.0
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(KettleProperties::class)
class SmartKettleAutoConfiguration {

    /**
     * 创建资源库提供器
     * @param kettleProperties 配置信息
     * @return 资源库提供器
     */
    @Bean
    @ConditionalOnMissingBean(KettleRepositoryProvider::class)
    fun kettleRepositoryProvider(kettleProperties: KettleProperties): KettleRepositoryProvider? {
        return DefaultKettleRepositoryProviderImpl(kettleProperties.repository)
    }

    /**
     * 创建日志控制器
     */
    @Bean
    fun kettleLogController(kettleProperties: KettleProperties): KettleLogController {
        return KettleLogController(kettleProperties.logProperties)
    }
}