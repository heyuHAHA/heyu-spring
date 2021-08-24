package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements  ConfigurableListableBeanFactory , BeanDefinitionRegistry{

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    @Override
     public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
      BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
      if (beanDefinition == null) {
          throw new BeansException("can not find BeanDefinition for beanName[" + beanName +"]");
      }
      return beanDefinition;
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        for (String s : beanDefinitionMap.keySet()) {
            getBean(s);
        }
    }

    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver :this.embeddedValueResolvers) {
            result = resolver.resolverStringValue(result);
        }
        return result;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
       return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();
        for(Map.Entry<String,BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class beanClass = entry.getValue().getBeanClass();
            String beanName = entry.getKey();
            if(type.isAssignableFrom(beanClass)) {
                T bean = (T)getBean(beanName);
                result.put(beanName,bean);
            }
        }
        return result;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

}
