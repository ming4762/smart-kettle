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
open class DatabaseMetaProperties {
    val type = "mysql"
    val access = "jdbc"
    var name: String? = null
    val host = "localhost"
    val db: String? = null
    val port = "3306"
    val dbUser = "root"
    val dbPassword = ""

}