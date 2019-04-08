package com.cw.client.tcp.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


/**
 * @author cw
 */
public class JacksonUtils {

     public  static String toJson(Object obj){
        ObjectMapper mapper = new ObjectMapper();
         String str = null;
         try {
             str = mapper.writeValueAsString(obj);
         } catch (JsonProcessingException e) {
             e.printStackTrace();
         }
         return str;
    }

   public static <T> T fromJson (String str , TypeReference<T> typeReference){
        ObjectMapper mapper = new ObjectMapper();
        T t = null;

        try {
            t = mapper.readValue(str, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

}
