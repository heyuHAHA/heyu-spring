package org.springframework.core.convert;

public interface ConverterRegistry {
    void addConverter(Converter<?,?> converter);

    void addConverterFactory(ConverterFactory<?,?> converterFactory);

    void addConverter(GenericConverter converter);
}
