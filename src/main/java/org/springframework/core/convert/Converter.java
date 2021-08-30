package org.springframework.core.convert;

public interface Converter <S,T>{
    T convert(S source);
}
