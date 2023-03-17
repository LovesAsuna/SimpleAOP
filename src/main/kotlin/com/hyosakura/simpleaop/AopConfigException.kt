package com.hyosakura.simpleaop

/**
 * @author LovesAsuna
 **/
class AopConfigException : RuntimeException {

    constructor(msg: String) : super(msg)

    constructor(msg: String, cause: Throwable) : super(msg, cause)

}