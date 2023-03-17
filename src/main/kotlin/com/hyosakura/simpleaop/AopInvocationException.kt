package com.hyosakura.simpleaop

/**
 * @author LovesAsuna
 **/
class AopInvocationException : RuntimeException {

    constructor(msg: String) : super(msg)

    constructor(msg: String, cause: Throwable) : super(msg, cause)

}