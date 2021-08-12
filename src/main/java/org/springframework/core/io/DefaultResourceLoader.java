package org.springframework.core.io;

import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader{
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    @Override
    public Resource getResource(String location) {
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return  new ClassPathResource(location);

        } else {
            try {
                //当做url
                return new UrlResource(new URL(location));
            } catch (MalformedURLException ex) {
                return new FileSystemResource(location);
            }
        }
    }
}
