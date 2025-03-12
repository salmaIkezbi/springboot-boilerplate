package com.nimbleways.springboilerplate.testhelpers.utils;

import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Instance {
    public static final String SCOPE_NAME = "perCall";
    private static final ClearableSimpleThreadScope SCOPE = new ClearableSimpleThreadScope();
    private static final MutableMap<ImmutableSet<Class<?>>, AnnotationConfigApplicationContext> CONTEXT_MAP = new ConcurrentHashMap<>();

    public static <T> T create(Class<T> clazz) {
        AnnotationConfigApplicationContext ctx = CONTEXT_MAP.computeIfAbsent(Immutable.set.of(clazz), Instance::createContext);
        return getNewBean(ctx, clazz);
    }

    public static BeanBag create(Class<?> ...classes) {
        AnnotationConfigApplicationContext ctx = CONTEXT_MAP.computeIfAbsent(Immutable.set.of(classes), Instance::createContext);
        return getNewBeans(ctx, classes);
    }

    private static BeanBag getNewBeans(AnnotationConfigApplicationContext ctx, Class<?> ...classes) {
        ImmutableSet<Object> beans = Immutable.set.fromStream(Arrays.stream(classes).map(ctx::getBean));
        SCOPE.clear();
        return new BeanBag(beans);
    }

    private static <T> T getNewBean(AnnotationConfigApplicationContext ctx, Class<T> clazz) {
        T bean = ctx.getBean(clazz);
        SCOPE.clear();
        return bean;
    }

    private static AnnotationConfigApplicationContext createContext(final ImmutableSet<Class<?>> classes) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getBeanFactory().registerScope(SCOPE_NAME, SCOPE);
        for (Class<?> clazz : classes) {
            Import importAnnotation = clazz.getAnnotation(Import.class);
            if (importAnnotation == null) {
                throw new IllegalArgumentException("@Import annotation is missing on class: " + clazz.getName());
            }
            ctx.registerBean(clazz.getName(), clazz, bd -> bd.setScope(SCOPE_NAME));
            registerDependenciesTransitively(ctx, importAnnotation);
        }
        ctx.refresh();
        return ctx;
    }

    private static void registerDependenciesTransitively(AnnotationConfigApplicationContext ctx, Import importAnnotation) {
        Class<?>[] dependencyClasses = importAnnotation.value();
        for (Class<?> dependencyClass : dependencyClasses) {
            ctx.registerBean(dependencyClass.getName(), dependencyClass, bd -> bd.setScope(SCOPE_NAME));
            Import depImportAnnotation = dependencyClass.getAnnotation(Import.class);
            if (depImportAnnotation != null) {
                registerDependenciesTransitively(ctx, depImportAnnotation);
            }
        }
    }

    // fork of org.springframework.context.support.SimpleThreadScope
    private final static class ClearableSimpleThreadScope implements Scope {
        private final ThreadLocal<Map<String, Object>> threadScope = NamedThreadLocal.withInitial("ClearableSimpleThreadScope", HashMap::new);

        @Override
        public @NotNull Object get(@NotNull String name, @NotNull ObjectFactory<?> objectFactory) {
            Map<String, Object> scope = this.threadScope.get();
            Object scopedObject = scope.get(name);
            if (scopedObject == null) {
                scopedObject = objectFactory.getObject();
                scope.put(name, scopedObject);
            }
            return scopedObject;
        }

        @Override
        @Nullable
        public Object remove(@NotNull String name) {
            Map<String, Object> scope = this.threadScope.get();
            return scope.remove(name);
        }

        @Override
        public void registerDestructionCallback(@NotNull String name, @NotNull Runnable callback) {
        }

        @Override
        @Nullable
        public Object resolveContextualObject(@NotNull String key) {
            return null;
        }

        @Override
        public String getConversationId() {
            return Thread.currentThread().getName();
        }

        public void clear() {
            threadScope.get().clear();
        }
    }
}
