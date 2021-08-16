package org.springframework.beans.factory.support;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{
    private String[] configLocations;

    public ClassPathXmlApplicationContext(String configLocation) {
        this(new String[]{configLocation});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) {
       this.configLocations = configLocations;
       try {
           refresh();
       } catch (Exception e) {
           System.out.println(e.getMessage());
       }
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[0];
    }
}
