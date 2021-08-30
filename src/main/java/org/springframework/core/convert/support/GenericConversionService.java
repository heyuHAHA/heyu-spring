package org.springframework.core.convert.support;

import org.springframework.core.convert.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class GenericConversionService implements ConversionService, ConverterRegistry {

    private Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return getConverter(sourceType,targetType) != null;
    }

    @Override
    public <T> T convert(Object source, Class<T> targetClass) {
        GenericConverter converter = getConverter(source.getClass(),targetClass);
        return (T)converter.convert(source,source.getClass(),targetClass);

    }

    @Override
    public void addConverter(Converter<?, ?> converter) {
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converter);
        ConverterAdapter converterAdapter = new ConverterAdapter(typeInfo, converter);
        for (GenericConverter.ConvertiblePair convertiblePair : converterAdapter.getConvertibleTypes()) {
            converters.put(convertiblePair,converterAdapter);
        }


    }

    private GenericConverter.ConvertiblePair getRequiredTypeInfo(Object converter) {
        Type[] types = converter.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class sourceType = (Class) actualTypeArguments[0];
        Class targetType = (Class) actualTypeArguments[1];

        return new GenericConverter.ConvertiblePair(sourceType,targetType);

    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converterFactory);
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(typeInfo, converterFactory);
        for (GenericConverter.ConvertiblePair convertibleType : converterFactoryAdapter.getConvertibleTypes()) {
            converters.put(convertibleType, converterFactoryAdapter);
        }
    }

    @Override
    public void addConverter(GenericConverter converter) {
        for (GenericConverter.ConvertiblePair convertiblePair : converter.getConvertibleTypes()) {
            converters.put(convertiblePair,converter);
        }
    }

    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType);
        List<Class<?>> targetCandidates = getClassHierarchy(targetType);
        for (Class source : sourceCandidates) {
            for (Class target : targetCandidates) {
                GenericConverter.ConvertiblePair pair = new GenericConverter.ConvertiblePair(source,target);
                GenericConverter converter = converters.get(pair);
                if (converter != null)
                    return converter;
            }
        }
        return null;
    }

    private List<Class<?>> getClassHierarchy(Class<?> clazz) {
        List<Class<?>> result = new ArrayList<>();
        while (clazz != null) {
            result.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    private final class ConverterAdapter implements  GenericConverter {
        private final ConvertiblePair typeInfo;

        private final Converter<Object, Object> converter;

        public ConverterAdapter(ConvertiblePair typeInfo, Converter<?, ?> converter) {
            this.typeInfo = typeInfo;
            this.converter = (Converter<Object, Object>) converter;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converter.convert(source);
        }
    }


    private final class ConverterFactoryAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final ConverterFactory<Object, Object> converterFactory;

        public ConverterFactoryAdapter(ConvertiblePair typeInfo, ConverterFactory<?,?> converterFactory) {
            this.typeInfo = typeInfo;
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converterFactory.getConvert(targetType).convert(source);
        }
    }
}
