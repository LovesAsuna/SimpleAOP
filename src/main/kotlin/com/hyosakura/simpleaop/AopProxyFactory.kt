package com.hyosakura.simpleaop

/**
 * @author LovesAsuna
 **/
interface AopProxyFactory {

    fun createAopProxy(config: HookSupport): AopProxy

}