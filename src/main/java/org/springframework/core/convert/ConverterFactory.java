package org.springframework.core.convert;

public interface ConverterFactory<S,R> {
    <T extends R> Converter<S,T> getConvert(Class<T> targetClass);
}
