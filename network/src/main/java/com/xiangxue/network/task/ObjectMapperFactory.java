/*
package com.http.com.xiangxue.network.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by tangyx
 * Date 2017/9/29
 * email tangyx@live.com
 *//*


public class ObjectMapperFactory {
    private static ObjectMapperFactory INSTANCE;
    private ObjectMapper mMapper;

    private ObjectMapperFactory() {
        mMapper = new ObjectMapper();
        //解析的时候，如果没有对应的字段，直接略过。
        mMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        mMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        //换转的成json的时候如果值是null直接忽略
        mMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    public static ObjectMapperFactory getObjectMapper(){
        return INSTANCE==null?INSTANCE=new ObjectMapperFactory():INSTANCE;
    }
    */
/**
     * json转对象
     *
     * @param value
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     *//*

    public <T> T onJsonToModel(String value, Class obj) throws IOException {
        return (T) mMapper.readValue(value, obj);
    }
    */
/**
     * 对象转json
     *//*

    public String onModelToJson(Object model) {
        try {
            return mMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    */
/**
     * 数据转list集合
     * @param value
     * @param obj
     * @return
     *//*

    public <T> T onJsonToList(String value, Class obj) {
        List list = null;
        JavaType javaType = mMapper.getTypeFactory().constructParametricType(ArrayList.class, obj);
        try {
            list = mMapper.readValue(value, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (T) list;
    }

    */
/**
     * 把json解析成list，如果list内部的元素存在jsonString，继续解析
     *
     * @param json
     * @return
     * @throws Exception
     *//*

    public List<Object> json2ListRecursion(String json) throws Exception {
        if (json == null) {
            return null;
        }

        List<Object> list = mMapper.readValue(json, List.class);

        for (Object obj : list) {
            if (obj != null && obj instanceof String) {
                String str = (String) obj;
                if (str.startsWith("[")) {
                    obj = json2ListRecursion(str);
                } else if (obj.toString().startsWith("{")) {
                    obj = json2MapRecursion(str);
                }
            }
        }

        return list;
    }

    */
/**
     * 把json解析成map，如果map内部的value存在jsonString，继续解析
     *
     * @param json
     * @return
     * @throws Exception
     *//*

    public Map<String, Object> json2MapRecursion(String json) throws Exception {
        if (json == null) {
            return null;
        }

        Map<String, Object> map = mMapper.readValue(json, Map.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj != null && obj instanceof String) {
                String str = ((String) obj);

                if (str.startsWith("[")) {
                    List<?> list = json2ListRecursion(str);
                    map.put(entry.getKey(), list);
                } else if (str.startsWith("{")) {
                    Map<String, Object> mapRecursion = json2MapRecursion(str);
                    map.put(entry.getKey(), mapRecursion);
                }
            }
        }

        return map;
    }

    */
/**
     * map  转JavaBean
     *//*

    public <T> T obj2pojo(Object obj, Class<T> clazz) {
        return mMapper.convertValue(obj, clazz);
    }

}
*/
