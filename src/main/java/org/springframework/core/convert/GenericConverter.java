package org.springframework.core.convert;

import java.util.Objects;
import java.util.Set;

public interface GenericConverter {
    Set<ConvertiblePair> getConvertibleTypes();

    Object convert(Object source, Class sourceType, Class targetType);

    public static final class ConvertiblePair {
        private final Class<?> sourceType;

        private final Class<?> targtType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targtType = targetType;
        }

        public Class<?> getSourceType() {
            return sourceType;
        }

        public Class<?> getTargtType() {
            return targtType;
        }

        @Override
        public boolean equals(Object o) {
           if (this == o)
               return true;
           if (o == null || o.getClass() != ConvertiblePair.class)
               return false;
           ConvertiblePair convertiblePair = (ConvertiblePair) o;
           return this.sourceType.equals(convertiblePair.sourceType) && this.targtType.equals(convertiblePair.targtType);
        }

        @Override
        public int hashCode() {
            return this.sourceType.hashCode() * 31 + this.targtType.hashCode();
        }
    }
}
