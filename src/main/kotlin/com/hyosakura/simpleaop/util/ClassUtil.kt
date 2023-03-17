package com.hyosakura.simpleaop.util

object ClassUtil {

    private const val PACKAGE_SEPARATOR = '.'

    private const val NESTED_CLASS_SEPARATOR = '$'

    const val BYTEBUDDY_CLASS_SEPARATOR = "ByteBuddy"

    fun getDefaultClassLoader(): ClassLoader? {
        var cl: ClassLoader? = null
        try {
            cl = Thread.currentThread().contextClassLoader
        } catch (ex: Throwable) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtil::class.java.classLoader
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader()
                } catch (ex: Throwable) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl
    }

    fun getShortName(className: String): String {
        require(className.isNotEmpty()) { "Class name must not be empty" }
        val lastDotIndex: Int = className.lastIndexOf(PACKAGE_SEPARATOR)
        var nameEndIndex: Int = className.indexOf(BYTEBUDDY_CLASS_SEPARATOR)
        if (nameEndIndex == -1) {
            nameEndIndex = className.length
        }
        var shortName = className.substring(lastDotIndex + 1, nameEndIndex)
        shortName = shortName.replace(
            NESTED_CLASS_SEPARATOR, PACKAGE_SEPARATOR
        )
        return shortName
    }

    private fun isLoadable(clazz: Class<*>, classLoader: ClassLoader): Boolean {
        return try {
            clazz == classLoader.loadClass(clazz.name)
            // Else: different class with same name found
        } catch (ex: ClassNotFoundException) {
            // No corresponding class found at all
            false
        }
    }

    fun getAllInterfacesForClass(clazz: Class<*>, classLoader: ClassLoader?): kotlin.Array<Class<*>> {
        return getAllInterfacesForClassAsSet(
            clazz,
            classLoader
        ).toTypedArray()
    }

    private fun getAllInterfacesForClassAsSet(clazz: Class<*>, classLoader: ClassLoader?): Set<Class<*>> {
        if (clazz.isInterface && isVisible(clazz, classLoader)) {
            return setOf(clazz)
        }
        val interfaces: MutableSet<Class<*>> = LinkedHashSet()
        var current: Class<*>? = clazz
        while (current != null) {
            val ifcs = current.interfaces
            for (ifc in ifcs) {
                if (isVisible(ifc, classLoader)) {
                    interfaces.add(ifc)
                }
            }
            current = current.superclass
        }
        return interfaces
    }

    private fun isVisible(clazz: Class<*>, classLoader: ClassLoader?): Boolean {
        if (classLoader == null) {
            return true
        }
        try {
            if (clazz.classLoader === classLoader) {
                return true
            }
        } catch (ex: SecurityException) {
            // Fall through to loadable check below
        }

        // Visible if same Class can be loaded from given ClassLoader
        return isLoadable(clazz, classLoader)
    }

}