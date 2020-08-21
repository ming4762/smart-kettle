package com.smart.kettle.core.properties

import lombok.Getter
import lombok.Setter

/**
 * 资源库配置
 * @author shizhongming
 * 2020/7/16 8:33 下午
 */
@Getter
@Setter
class DatabaseMetaProperties {
    private val type = "mysql"
    private val access = "jdbc"
    private val name = ""
    private val host = "localhost"
    private val db = ""
    private val port = "3306"
    private val dbUser = "root"
    private val dbPassword = ""
    private val resUser = "admin"
    private val resPassword = "admin"
}