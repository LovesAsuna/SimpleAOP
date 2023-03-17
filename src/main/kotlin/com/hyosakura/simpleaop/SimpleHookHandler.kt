package com.hyosakura.simpleaop

/**
 * @author LovesAsuna
 **/
open class SimpleHookHandler : HookHandler {

    override fun before() {}

    override fun afterReturning(obj: Any?): Any? {
        return obj
    }

    override fun afterThrowing(throwable: Throwable) {
        throw throwable
    }

    override fun after() {}

}