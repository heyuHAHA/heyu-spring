package org.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.*;

import java.lang.reflect.Method;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        //生命周期第三阶段：InstantiationAwareBeanPostProcessor，可生成代理对象
        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if (bean != null)
            return bean;
        return doCreateBean(beanName,beanDefinition);
    }

    private Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Object bean = applyBeanPostProcessorsBeforeInitialization(beanDefinition.getBeanClass(),beanName);
        return bean;
    }

    protected Object applyBeanPostProcessorsBeforeInitialization(Class beanClass, String beanName) throws BeansException {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessorList()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor)beanPostProcessor).postProcessBeforeInstantiation(beanClass,beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Object bean = null;
        try {
            //生命周期第四步:bean实例化
            bean = createBeanInstance(beanDefinition);
            //生命周期第五步:InstantiationAwareBeanPostProcessor，修改BeanDefinition的PropertyValue
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName,bean,beanDefinition);
            //为Bean填充属性
            applyPropertyValues(beanName,bean,beanDefinition);
            //执行Bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            initializeBean(beanName,bean,beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        //注册有销毁方法的bean
        registerDisposableBeanIfNecessary(beanName,bean,beanDefinition);

        //addSingleton(beanName,bean);
        //增加了prototype 和 single类型的Bean，如果是single就放到缓存
        if (beanDefinition.isSingleton()) {
            addSingleton(beanName,bean);
        }
        return bean;
    }

    private void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessorList()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor)beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(),bean,beanName);
                if (pvs != null) {
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().add(propertyValue);
                    }
                }
            }
        }
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
//        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
//            registerDisposableBean(beanName,new DisposableBeanAdapter(bean,beanName,beanDefinition));
//        }
        //只有singleton类型Bean会执行销毁方法
        if (beanDefinition.isSingleton()) {
            if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
                registerDisposableBean(beanName,new DisposableBeanAdapter(bean,beanName,beanDefinition));
            }
        }
    }

    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {
        //生命周期第六步: BeanFactoryAware#setBeanFactory
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        //生命周期第七步:执行BeanPostProcessor的前置处理, ApplicationContextAware也是这一步

        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean,beanName);
        try {
            //生命周期第八步: 执行Bean的初始化方法
            invokeInitMethods(beanName,wrappedBean,beanDefinition);

        } catch (Exception e) {
            throw new BeansException("An error occur in initalizeBean");
        }
        //生命周期第九步:执行BeanPostProcessor的后置处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean,beanName);
        return wrappedBean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) throws BeansException {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }

    /**
     * 为Bean填充属性
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        PropertyValue pv = null;
        try {
            PropertyValue[] propertyValues = beanDefinition.getPropertyValues().getPropertyValues();
            for (PropertyValue propertyValue : propertyValues) {
                pv = propertyValue;
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                if (value instanceof BeanReference) {
                    BeanReference beanReference  = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                BeanUtil.setFieldValue(bean,name,value);
            }
        } catch (Exception e) {

            //throw new BeansApplyPropertiesExceptions("apply " + pv.getName() + " : " + pv.getValue()  + " to " + beanName +" occurs a error" );
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessorList()) {
            Object wrapper = beanPostProcessor.postProcessorBeforeInitialization(result,beanName);
            if (wrapper == null) {
                return result;
            }
        }
        return result;

    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessorList()) {
            Object wrapper = processor.postProcessorAfterInitialization(result,beanName);
            if (wrapper == null) {
                return null;
            }
        }
        return null;
    }

    /**
     * 执行bean的初始化方法
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        if (bean instanceof InitializingBean) {
            ((InitializingBean)bean).afterPropertiesSet();
        }
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName)) {
            Method initMethod = ClassUtil.getPublicMethod(beanDefinition.getBeanClass(),initMethodName);
            if (initMethod != null)
                initMethod.invoke(bean);
        }

    }
}
