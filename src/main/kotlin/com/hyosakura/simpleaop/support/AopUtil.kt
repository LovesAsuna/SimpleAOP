package com.hyosakura.simpleaop.support


import com.hyosakura.simpleaop.LiteIocProxy
import com.hyosakura.simpleaop.TargetClassAware
import com.hyosakura.simpleaop.util.ClassUtil

object AopUtil {

    fun getTargetClass(candidate: Any): Class<*> {
        var result: Class<*>? = null
        if (candidate is TargetClassAware) {
            result = candidate.getTargetClass()
        }
        if (result == null) {
            result =
                if (isByteBuddyProxy(candidate)) candidate.javaClass.superclass else candidate.javaClass
        }
        return result!!
    }

    fun isByteBuddyProxy(`object`: Any?): Boolean {
        return `object` is LiteIocProxy &&
                `object`.javaClass.name.contains(ClassUtil.BYTEBUDDY_CLASS_SEPARATOR)
    }

}