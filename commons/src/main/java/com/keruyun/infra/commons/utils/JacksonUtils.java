package com.keruyun.infra.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public final class JacksonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper = generateMapper(JsonInclude.Include.ALWAYS);
        //  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 通过Inclusion创建ObjectMapper对象
     * <p>
     * {@link Inclusion Inclusion 对象枚举}
     * <ul>
     * <li>{@link Inclusion Inclusion.ALWAYS 全部列入}</li>
     * <li>{@link Inclusion Inclusion.NON_DEFAULT 字段和对象默认值相同的时候不会列入}</li>
     * <li>{@link Inclusion Inclusion.NON_EMPTY 字段为NULL或者""的时候不会列入}</li>
     * <li>{@link Inclusion Inclusion.NON_NULL 字段为NULL时候不会列入}</li>
     * </ul>
     *
     * @param inclusion 传入一个枚举值, 设置输出属性
     * @return 返回ObjectMapper对象
     */
    private static ObjectMapper generateMapper(JsonInclude.Include inclusion) {

        ObjectMapper customMapper = new ObjectMapper();

        // 设置输出时包含属性的风格
        customMapper.setSerializationInclusion(inclusion);

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 禁止使用int代表Enum的order()來反序列化Enum,非常危險
        customMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

        // 所有日期格式都统一为以下样式
//        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return customMapper;
    }

    public static <T> Optional<T> jsonToBean(String jsonString, Class<T> tClass) throws IOException {
        return Optional.of(mapper.readValue(jsonString, tClass));
    }


    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return clazz.equals(String.class) ? (T) json : mapper.readValue(json, clazz);
        } catch (IOException e) {
            // logger.error(e.getMessage(), e);
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(String jsonString, Class<T> tClass) throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, tClass);
        return (List<T>) mapper.readValue(jsonString, javaType);
    }

    public static <T> Optional<T> jsonToBean(String jsonString, JavaType javaType) {
        return Optional.of(mapper.convertValue(jsonString, javaType));

    }

    public static Optional<String> beanToJson(Object object) throws JsonProcessingException {
        return Optional.of(mapper.writeValueAsString(object));
    }


    public static <T> String toJson(T src) {
        try {
            if (src == null) {
                return null;
            }
            return src instanceof String ? (String) src : mapper.writeValueAsString(src);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    public static <T> T toObjectByReference(String jsonStr, TypeReference<T> typeReference)
            throws Exception {
        return mapper.readValue(jsonStr, typeReference);
    }

    public static <K, V> JavaType buildMapJavaType(Class<K> kClass, Class<V> vClass) {
        return mapper.getTypeFactory().constructParametricType(Map.class, kClass, kClass);
    }


}
