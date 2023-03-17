package com.hyosakura.simpleaop.autoproxy

import com.hyosakura.simpleaop.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.kotlinFunction

abstract class AutoProxyCreator : ProxyProcessorSupport() {

    companion object {

        var DO_NOT_PROXY: Array<Any>? = null

    }


    private val earlyProxyReferences: MutableMap<Any, Any> = ConcurrentHashMap(16)

    private val targetSourcedBeans = Collections.newSetFromMap(ConcurrentHashMap<String, Boolean>(16))

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val advisedBeans: MutableMap<Any, Boolean> = ConcurrentHashMap(256)

    fun applyAutoProxy(bean: Any, beanName: String): Any {
        val cacheKey = getCacheKey(bean.javaClass, beanName)
        if (earlyProxyReferences.remove(cacheKey) !== bean) {
            return wrapIfNecessary(bean, beanName, cacheKey)
        }
        return bean
    }

    private fun wrapIfNecessary(bean: Any, beanName: String, cacheKey: Any): Any {
        if (beanName.isNotEmpty() && this.targetSourcedBeans.contains(beanName)) {
            return bean
        }
        if (false == this.advisedBeans[cacheKey]) {
            return bean
        }
        if (isInfrastructureClass(bean.javaClass)) {
            this.advisedBeans[cacheKey] = false
            return bean
        }

        val specificMethods = getHookMethodsForBean(bean.javaClass)
        if (!specificMethods.contentEquals(DO_NOT_PROXY)) {
            this.advisedBeans[cacheKey] = true
            return createProxy(
                bean.javaClass, beanName, specificMethods!!, SingletonTargetSource(bean)
            )
        }

        this.advisedBeans[cacheKey] = false
        return bean
    }

    fun createProxy(
        beanClass: Class<*>, beanName: String?, specificMethods: Array<Method>, targetSource: TargetSource
    ): Any {
        val proxyFactory = ProxyFactory()
        proxyFactory.copyFrom(this)

        if (proxyFactory.isProxyTargetClass()) {
            if (Proxy.isProxyClass(beanClass)) {
                for (ifc in beanClass.interfaces) {
                    proxyFactory.addInterface(ifc)
                }
            }
        } else {
            if (shouldProxyTargetClass(beanClass, beanName)) {
                proxyFactory.setProxyTargetClass(true)
            } else {
                evaluateProxyInterfaces(beanClass, proxyFactory)
            }
        }

        val handlers = buildHandlers(specificMethods)
        proxyFactory.setTargetSource(targetSource)
        proxyFactory.setHookMethods(*specificMethods)
        proxyFactory.addHandlers(*handlers)

        val classLoader = this.proxyClassLoader
        return proxyFactory.getProxy(classLoader)
    }

    fun getCacheKey(beanClass: Class<*>, beanName: String?): Any {
        return if (!beanName.isNullOrEmpty()) {
            beanName
        } else {
            beanClass
        }
    }

    fun isInfrastructureClass(beanClass: Class<*>): Boolean {
        val retVal = HookHandler::class.java.isAssignableFrom(beanClass)
        if (retVal && logger.isTraceEnabled) {
            logger.trace("Did not attempt to auto-proxy infrastructure class [" + beanClass.name + "]")
        }
        return retVal
    }

    abstract fun shouldProxyTargetClass(beanClass: Class<*>, beanName: String?): Boolean

    fun getHookMethodsForBean(beanClass: Class<*>): Array<Method>? {
        val methods = LinkedList<Method>()
        for (method in beanClass.kotlin.declaredFunctions) {
            if (method.hasAnnotation<Hook>()) {
                method.javaMethod?.let { methods.add(it) }
            }
        }
        return if (methods.isEmpty()) {
            null
        } else {
            methods.toTypedArray()
        }
    }

    private fun buildHandlers(methods: Array<Method>): Array<HookHandler> {
        val handlersList = LinkedList<HookHandler>()
        for (method in methods) {
            val hook = method.kotlinFunction!!.findAnnotation<Hook>()!!
            val handlers = hook.value
            for (handlerClass in handlers) {
                // first try get handler in BeanFactory
                val handler = getHandlerBean(handlerClass.java)
                handlersList.add(handler)
            }
        }
        return handlersList.toTypedArray()
    }

    abstract fun getHandlerBean(clazz: Class<*>): HookHandler
}