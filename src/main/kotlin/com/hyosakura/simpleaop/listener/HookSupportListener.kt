package com.hyosakura.simpleaop.listener

import com.hyosakura.simpleaop.HookSupport
/**
 * @author LovesAsuna
 **/
interface HookSupportListener {

    fun activated(hook: HookSupport)

    fun adviceChanged(hook: HookSupport)

}
