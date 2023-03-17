package com.hyosakura.simpleaop

import com.hyosakura.simpleaop.listener.HookSupportListener


/**
 * @author LovesAsuna
 **/
open class ProxyCreatorSupport : HookSupport() {

    private val aopProxyFactory: AopProxyFactory

    private var active = false

    private val listeners: MutableList<HookSupportListener> = ArrayList()

    init {
        aopProxyFactory = DefaultAopProxyFactory()
    }

    open fun getAopProxyFactory(): AopProxyFactory {
        return this.aopProxyFactory
    }

    @Synchronized
    fun createAopProxy(): AopProxy {
        if (!active) {
            activate()
        }
        return getAopProxyFactory().createAopProxy(this)
    }

    private fun activate() {
        this.active = true
        for (listener in this.listeners) {
            listener.activated(this)
        }
    }

    override fun hookChanged() {
        super.hookChanged()
        synchronized(this) {
            if (this.active) {
                for (listener in this.listeners) {
                    listener.adviceChanged(this)
                }
            }
        }
    }

}