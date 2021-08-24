package org.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    //init and destroy method
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";
    public static final String COMPONENT_SCAN_ELEMENT = "context:component-scan";
    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, DefaultResourceLoader defaultResourceLoader) {
        super(registry, defaultResourceLoader);
    }

    @Override
    public void
    loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try (InputStream inputStream = resource.getInputStream()) {
            try {
                doLoadBeanDefinitions(inputStream);
            } finally {
                inputStream.close();
            }

        } catch (IOException ex) {
            throw new BeansException("IOException parsing XML document from " +resource, ex);
        }
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws BeansException {
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for ( int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i) instanceof Element) {
                if (COMPONENT_SCAN_ELEMENT.equals(childNodes.item(i).getNodeName())) {
                    Element context_component_scan = (Element) childNodes.item(i);
                    if (context_component_scan != null) {
                        String basePath = context_component_scan.getAttribute(BASE_PACKAGE_ATTRIBUTE);
                        if (StrUtil.isEmpty(basePath)) {
                            throw new BeansException("The value of base-package attribute can not be empty or null");
                        }
                        scanPackage(basePath);
                    }
                }
                if (BEAN_ELEMENT.equals(childNodes.item(i).getNodeName())) {
                    //解析bean标签
                    Element bean = (Element) childNodes.item(i);
                    String id = bean.getAttribute(ID_ATTRIBUTE);
                    String name = bean.getAttribute(NAME_ATTRIBUTE);
                    String className = bean.getAttribute(CLASS_ATTRIBUTE);
                    String initMethodName = bean.getAttribute(INIT_METHOD_ATTRIBUTE);
                    String destoryMethodName = bean.getAttribute(DESTROY_METHOD_ATTRIBUTE);
                    String beanScope = bean.getAttribute(SCOPE_ATTRIBUTE);

                    Class<?> clazz = null;
                    try {
                        System.out.println(className);
                        clazz = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new BeansException("Cannot find class [" + className +"]");

                    }
                    //id优于Name
                    String beanName = StrUtil.isNotEmpty(id) ? id :name;
                    if (StrUtil.isEmpty(beanName)) {
                        beanName = StrUtil.lowerFirst(clazz.getSimpleName());
                    }

                    BeanDefinition beanDefinition = new BeanDefinition(clazz);
                    beanDefinition.setDestroyMethodName(destoryMethodName);
                    beanDefinition.setInitMethodName(initMethodName);
                    if (StrUtil.isNotEmpty(beanScope))
                        beanDefinition.setScope(beanScope);

                    for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                        if (bean.getChildNodes().item(j) instanceof Element) {
                            if (PROPERTY_ELEMENT.equals(bean.getChildNodes().item(j).getNodeName())) {
                                //解析property标签
                                Element property = (Element) bean.getChildNodes().item(j);
                                String nameAttr = property.getAttribute(NAME_ATTRIBUTE);
                                String valueAttr = property.getAttribute(VALUE_ATTRIBUTE);
                                String refAttr = property.getAttribute(REF_ATTRIBUTE);
                                if (StrUtil.isEmpty(nameAttr)) {
                                    throw new BeansException("The name attribute cannot be null or empty for " + beanName);
                                }
                                Object value = valueAttr;
                                if (StrUtil.isNotEmpty(refAttr)) {
                                    value = new BeanReference(refAttr);
                                }
                                PropertyValue propertyValue = new PropertyValue(nameAttr,value);
                                beanDefinition.getPropertyValues().add(propertyValue);

                            }
                        }
                    }
                    if(getRegistry().containsBeanDefinition(beanName)) {
                        throw new BeansException("Duplicate beanName [" + beanName +"] is not allowed");
                    }
                    //注册
                    getRegistry().registerBeanDefinition(beanName,beanDefinition);

                }
            }
        }
    }

    private void scanPackage(String basePath) {
//        System.out.println("basePath :" +basePath);
        String[] basePackages = StrUtil.splitToArray(basePath, ",");
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }


}
