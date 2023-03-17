package com.hyosakura.simpleaop.util

import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

object ReflectionUtil {

    @Throws(NoSuchMethodException::class)
    fun <T> accessibleConstructor(clazz: Class<T>, vararg parameterTypes: Class<*>?): Constructor<T> {
        val ctor = clazz.getDeclaredConstructor(*parameterTypes)
        makeAccessible(ctor)
        return ctor
    }

    fun makeAccessible(ctor: Constructor<*>) {
        if ((!Modifier.isPublic(ctor.modifiers) || !Modifier.isPublic(ctor.declaringClass.modifiers)) && !ctor.isAccessible) {
            ctor.isAccessible = true
        }
    }

}