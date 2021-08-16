package org.springframework.context.support;

import org.springframework.beans.BeansException;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    private String[] configLocations;

    public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
        this(new String[]{configLocation});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
       this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return this.configLocations;
    }
}
