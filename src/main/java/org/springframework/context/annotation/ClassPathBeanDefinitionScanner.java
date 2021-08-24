package org.springframework.context.annotation;


import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Scope;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{
    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage :basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                String beanScope = resolveBeanScope(candidate);
                if (StrUtil.isNotEmpty(beanScope))
                    candidate.setScope(beanScope);
                //生成
                String beanName = deterMineBeanName(candidate);
                registry.registerBeanDefinition(beanName,candidate);
            }
        }
    }

    private String deterMineBeanName(BeanDefinition candidate) {
        Class clazz = candidate.getBeanClass();
        if (clazz == null)
            //记录日志
            System.out.println("Class not found in BeanDefinition");
        Component component = (Component) clazz.getAnnotation(Component.class);
        String beanName = clazz.getSimpleName();
        //System.out.println(beanName);
        if (component != null) {
            beanName =  StrUtil.isNotEmpty(component.value()) ? component.value() : beanName ;
        }
        return beanName;
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getBeanClass();
        Scope scope = (Scope) clazz.getAnnotation(Scope.class);
        if (scope != null) {
            return scope.value();
        }
        return StrUtil.EMPTY;

    }
}
