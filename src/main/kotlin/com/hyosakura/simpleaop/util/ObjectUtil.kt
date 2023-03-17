package com.hyosakura.simpleaop.util

object ObjectUtil {

    fun nullSafeEquals(o1: Any?, o2: Any?): Boolean {
        if (o1 === o2) {
            return true
        }
        if (o1 == null || o2 == null) {
            return false
        }
        if (o1 == o2) {
            return true
        }
        return if (o1.javaClass.isArray && o2.javaClass.isArray) {
            arrayEquals(o1, o2)
        } else false
    }

    private fun arrayEquals(o1: Any, o2: Any): Boolean {
        if (o1 is Array<*> && o2 is Array<*>) {
            return o1.contentEquals(o2)
        }
        if (o1 is BooleanArray && o2 is BooleanArray) {
            return o1.contentEquals(o2)
        }
        if (o1 is ByteArray && o2 is ByteArray) {
            return o1.contentEquals(o2)
        }
        if (o1 is CharArray && o2 is CharArray) {
            return o1.contentEquals(o2)
        }
        if (o1 is DoubleArray && o2 is DoubleArray) {
            return o1.contentEquals(o2)
        }
        if (o1 is FloatArray && o2 is FloatArray) {
            return o1.contentEquals(o2)
        }
        if (o1 is IntArray && o2 is IntArray) {
            return o1.contentEquals(o2)
        }
        if (o1 is LongArray && o2 is LongArray) {
            return o1.contentEquals(o2)
        }
        return if (o1 is ShortArray && o2 is ShortArray) {
            o1.contentEquals(o2)
        } else false
    }

    fun nullSafeHashCode(obj: Any?): Int {
        if (obj == null) {
            return 0
        }
        if (obj.javaClass.isArray) {
            if (obj is Array<*>) {
                return nullSafeHashCode(obj)
            }
            if (obj is BooleanArray) {
                return nullSafeHashCode(obj)
            }
            if (obj is ByteArray) {
                return nullSafeHashCode(obj)
            }
            if (obj is CharArray) {
                return nullSafeHashCode(obj)
            }
            if (obj is DoubleArray) {
                return nullSafeHashCode(obj)
            }
            if (obj is FloatArray) {
                return nullSafeHashCode(obj)
            }
            if (obj is IntArray) {
                return nullSafeHashCode(obj)
            }
            if (obj is LongArray) {
                return nullSafeHashCode(obj)
            }
            if (obj is ShortArray) {
                return nullSafeHashCode(obj)
            }
        }
        return obj.hashCode()
    }

    fun containsElement(array: Array<Any?>?, element: Any?): Boolean {
        if (array == null) {
            return false
        }
        for (arrayEle in array) {
            if (nullSafeEquals(arrayEle, element)) {
                return true
            }
        }
        return false
    }

}