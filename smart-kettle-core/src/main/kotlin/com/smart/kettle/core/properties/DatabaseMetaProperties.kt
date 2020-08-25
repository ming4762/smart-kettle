package com.smart.kettle.core.properties

import lombok.Getter
import lombok.Setter

/**
 * 资源库配置
 * @author shizhongming
 * 2020/7/16 8:33 下午
 */
open class DatabaseMetaProperties {
    var type = "mysql"
    var access = "jdbc"
    var name: String? = null
    var host = "localhost"
    var db: String? = null
    var port = "3306"
    var dbUser = "root"
    var dbPassword = ""

}