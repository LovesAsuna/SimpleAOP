package com.hyosakura.simpleaop

/**
 * @author LovesAsuna
 **/
interface TargetClassAware {

    fun getTargetClass(): Class<*>?

}