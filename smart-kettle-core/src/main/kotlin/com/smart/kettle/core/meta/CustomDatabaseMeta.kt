package com.smart.kettle.core.meta

import com.smart.kettle.core.constants.DatabaseTypeEnum
import org.apache.commons.lang3.StringUtils
import org.pentaho.di.core.database.DatabaseMeta
import org.pentaho.di.core.exception.KettleDatabaseException
import kotlin.jvm.Throws

/**
 * @author shizhongming
 * 2020/7/16 8:24 下午
 */
class CustomDatabaseMeta(name: String?, type: String?, access: String?, host: String?, db: String?, port: String?, user: String?, pass: String?) : DatabaseMeta(name, type, access, host, db, port, user, pass) {

    companion object {
        /**
         * 旧的mysql驱动
         */
        private const val OLD_MYSQL_DRIVER = "org.gjt.mm.mysql.Driver"

        /**
         * 新的mysql驱动
         */
        private const val NEW_MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver"

        /**
         * mysql servertimezone
         */
        private const val MYSQL_TIME_ZONE = "&serverTimeZone=GMT%2B8"
    }

    /**
     * 重写获取数据库驱动
     * 解决mysql驱动包版本冲突
     */
    override fun getDriverClass(): String {
        var driverClass = super.getDriverClass()
        if (OLD_MYSQL_DRIVER == driverClass) {
            driverClass = NEW_MYSQL_DRIVER
        }
        return driverClass
    }

    /**
     * 重写getURL函数，解决mysql serverTimeZone问题
     * @return url
     * @throws KettleDatabaseException KettleDatabaseException
     */
    @Throws(KettleDatabaseException::class)
    override fun getURL(): String {
        var url = super.getURL()
        if (StringUtils.equals(databaseInterface.pluginName, DatabaseTypeEnum.MySql.name)) {
            url += MYSQL_TIME_ZONE
        }
        return url
    }
}