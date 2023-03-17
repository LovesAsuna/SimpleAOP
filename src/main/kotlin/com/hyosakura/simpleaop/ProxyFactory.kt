package com.hyosakura.simpleaop

/**
 * @author LovesAsuna
 **/
class ProxyFactory : ProxyCreatorSupport() {

    fun getProxy(): Any {
        return createAopProxy().getProxy()
    }

    fun getProxy(classLoader: ClassLoader?): Any {
        return createAopProxy().getProxy(classLoader)
    }

}