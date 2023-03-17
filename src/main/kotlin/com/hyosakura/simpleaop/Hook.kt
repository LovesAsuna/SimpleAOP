package com.hyosakura.simpleaop

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
annotation class Hook(val value: Array<KClass<out HookHandler>> = [])
