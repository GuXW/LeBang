package com.ruoyi.project.utils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JsonUtils {

    /**
     * 实体对象转换成Json字符串
     * @param t 实体对象T
     * @return T
     */
    public  static <T> String objectToJson(T t){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);//设置可用单引号
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//设置字段可以不用双引号包括
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));//设置时间格式
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Json字符串转换成实体对象
     * @param json
     * @param clazz 实体对象所属类Class
     * @return
     */
    public  static <T> T jsonToObject(String json, Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);//设置可用单引号
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//设置字段可以不用双引号包括
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));//设置时间格式
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);//设置实体无属性和json串属性对应时不会出错
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Json字符串转换成List
     * @param json
     * @param clazz 实体对象所属类Class
     * @return
     */
    public  static <T> T jsonToList(String json, Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);//设置可用单引号
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//设置字段可以不用双引号包括
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            return mapper.readValue(json, javaType);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Json字符串转换成List
     * @param json
     * @param typeReference TypeReference<T>
     * <p>
     *  <pre>new TypeReference<List<User>>() {}</pre>
     * </p>
     * @return
     */
    public static <T> T jsonToList(String json, TypeReference<T> typeReference){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);//设置可用单引号
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//设置字段可以不用双引号包括
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return mapper.readValue(json, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  美化输出
     * @param t
     * @return
     */
    public  static <T> String console(T t){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);//设置可用单引号
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//设置字段可以不用双引号包括
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

}

