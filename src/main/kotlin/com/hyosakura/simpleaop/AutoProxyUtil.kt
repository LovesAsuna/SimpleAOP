package com.hyosakura.simpleaop

import java.lang.reflect.Proxy

object AutoProxyUtil {

    val PRESERVE_TARGET_CLASS_ATTRIBUTE: String = "${AutoProxyUtil::class.java.name}.preserveTargetClass"

    fun completeProxiedInterfaces(hook: HookSupport): Array<Class<*>> {
        var specifiedInterfaces = hook.getProxiedInterfaces()
        if (specifiedInterfaces.isEmpty()) {
            val targetClass = hook.getTargetClass()
            if (targetClass != null) {
                if (targetClass.isInterface) {
                    hook.setInterfaces(targetClass)
                } else if (Proxy.isProxyClass(targetClass)) {
                    hook.setInterfaces(*targetClass.interfaces)
                }
                specifiedInterfaces = hook.getProxiedInterfaces()
            }
        }
        val proxiedInterfaces = ArrayList<Class<*>>(specifiedInterfaces.size + 3)
        for (ifc in specifiedInterfaces) {
            if (!ifc.isSealed) {
                proxiedInterfaces.add(ifc)
            }
        }
        if (!hook.isInterfaceProxied(LiteIocProxy::class.java)) {
            proxiedInterfaces.add(LiteIocProxy::class.java)
        }
        if (!hook.isInterfaceProxied(Hooked::class.java)) {
            proxiedInterfaces.add(Hooked::class.java)
        }
        return proxiedInterfaces.toTypedArray()
    }

}