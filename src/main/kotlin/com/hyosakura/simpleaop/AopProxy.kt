package com.hyosakura.simpleaop

/**
 * @author LovesAsuna
 **/
interface AopProxy {

    fun getProxy(): Any

    fun getProxy(classLoader: ClassLoader?): Any

}