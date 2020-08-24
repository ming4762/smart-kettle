package com.smart.starter.kettle

import org.springframework.boot.context.properties.EnableConfigurationProperties
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
@EnableConfigurationProperties(KettleProperties :: class)
class SmartKettleAutoConfiguration {
}