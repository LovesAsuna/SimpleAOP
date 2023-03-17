package com.hyosakura.simpleaop

/**
 * @author LovesAsuna
 **/
interface TargetSource {

    fun getTargetClass(): Class<*>?

    fun isStatic(): Boolean

    @Throws(Exception::class)
    fun getTarget(): Any?

    @Throws(Exception::class)
    fun releaseTarget(target: Any)

}