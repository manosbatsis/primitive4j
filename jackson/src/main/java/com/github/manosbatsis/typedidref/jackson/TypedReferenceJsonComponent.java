package com.github.manosbatsis.typedidref.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.github.manosbatsis.typedidref.core.TypedProperty;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class TypedReferenceJsonComponent {
  public static class Serializer extends JsonSerializer<TypedProperty> {
    public void serialize(
        TypedProperty obj, JsonGenerator jsonGenerator, SerializerProvider provider)
        throws IOException {
      if (obj == null) {
        jsonGenerator.writeNull();
      } else {
        provider.defaultSerializeValue(obj.getValue(), jsonGenerator);
      }
    }
  }

  @AllArgsConstructor // used by createContextual
  @NoArgsConstructor // used when you don't know the type, e.g. in simple module creation or in
  // `@JsonDeserialize`
  static class Deserializer<T extends TypedProperty<?, ?>> extends JsonDeserializer<T>
      implements ContextualDeserializer {

    private Class<T> targetType;
    private Class<?> innerValueType;

    @Override
    public JsonDeserializer<?> createContextual(
        DeserializationContext deserializationContext, BeanProperty beanProperty) {
      Class<T> typedReferenceSubClass =
          findContectuialTargetClass(deserializationContext, beanProperty);

      Class<?> innervalueClass =
          ((ParameterizedType) typedReferenceSubClass.getGenericSuperclass())
              .getActualTypeArguments()[0].getClass();
      return new Deserializer<>(typedReferenceSubClass, innervalueClass);
    }

    private Class<T> findContectuialTargetClass(
        DeserializationContext deserializationContext, BeanProperty beanProperty) {
      // BeanProperty is null when the type to deserialize is the top-level type or a generic type,
      // not a type of a bean property
      Class<?> type =
          Objects.nonNull(deserializationContext.getContextualType())
              ? deserializationContext.getContextualType().getRawClass()
              : beanProperty.getMember().getType().getRawClass();
      return (Class<T>) type;
    }

    public T deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
      try {
        var codec = jsonParser.getCodec();
        return targetType
            .getConstructor(innerValueType)
            .newInstance(codec.readValue(jsonParser, innerValueType));
      } catch (NoSuchMethodException
          | InstantiationException
          | IllegalAccessException
          | InvocationTargetException e) {
        throw new JsonMappingException(jsonParser, "Failed deserializing value", e);
      }
    }
  }
}
