package com.smzdm.commons.rpc.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public final class ExtensionLoader {
    private static final Map<Class<?>, Object> extensionMap = new ConcurrentHashMap<Class<?>, Object>();
    private static final Map<Class<?>, List<?>> extensionListMap = new ConcurrentHashMap<Class<?>, List<?>>();

    public ExtensionLoader() {
    }

    public static <T> T getExtension(Class<T> clazz) {
        Object extension = extensionMap.get(clazz);
        if (extension == null) {
            extension = newExtension(clazz);
            if (extension != null) {
                extensionMap.put(clazz, extension);
            }
        }

        return (T) extension;
    }

    public static <T> List<T> getExtensionList(Class<T> clazz) {
        List<T> extensions = (List<T>) extensionListMap.get(clazz);
        if (extensions == null) {
            extensions = newExtensionList(clazz);
            if (!extensions.isEmpty()) {
                extensionListMap.put(clazz, extensions);
            }
        }

        return extensions;
    }

    public static <T> T newExtension(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        if (serviceLoader.iterator().hasNext()) {
            return serviceLoader.iterator().next();
        }
        return null;
    }

    public static <T> List<T> newExtensionList(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        List<T> extensions = new ArrayList<T>();

        for (T service : serviceLoader) {
            extensions.add(service);
        }

        return extensions;
    }
}
