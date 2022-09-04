package com.chen.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author CHEN
 */
@Component
@Slf4j
public class RedisTemplate {
    @Autowired
    private JedisPool jedisPool;
    @Resource
    private CustomObjectMapper customObjectMapper;

    /**
     * 存放字符串类型数据
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String set(String key,String value,Long expire){
        Jedis resource = jedisPool.getResource();
        String result=null;
        try {
            result= resource.setex(key, expire, value);
        }catch (JedisException e){
            log.error("redis exception error",e);
            jedisPool.returnBrokenResource(resource);
        }finally {
            jedisPool.returnResource(resource);
        }
        return  result;
    }

    /**
     * 获取字符串数据
     * @param key
     * @return
     */
    public String get(String key){
        Jedis resource = jedisPool.getResource();
        String result=null;
        try {
            result= resource.get(key);
        }catch (JedisException e){
            log.error("redis exception error",e);
            jedisPool.returnBrokenResource(resource);
        }finally {
            jedisPool.returnResource(resource);
        }
        return  result;
    }

    /**
     * 设置json格式字符串
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String setObj(String key,Object value,Long expire){
        Jedis resource = jedisPool.getResource();
        String result=null;
        try {
            String jsonValue = customObjectMapper.writeValueAsString(value);
            resource.setex(key,expire,jsonValue);
        }catch (JedisException |JsonProcessingException e){
            log.error("redis exception error",e);
            jedisPool.returnBrokenResource(resource);
        } finally {
            jedisPool.returnResource(resource);
        }
        return  result;
    }
    public <T> T getObj(String key, TypeReference<T> typeReference){
        Jedis resource = jedisPool.getResource();
        T object=null;
        try {
            String result = resource.get(key);
            object = customObjectMapper.readValue(result,typeReference);
        }catch (JedisException |JsonProcessingException e){
            log.error("redis exception error",e);
            jedisPool.returnBrokenResource(resource);
        } finally {
            jedisPool.returnResource(resource);
        }
        return  object;
    }

    /**
     * 删除keys
     * @param key key
     * @return
     */
    public Long remove(String ... key){
        Jedis resource = jedisPool.getResource();
        String result=null;
        long res=0L;
        try {
            res = resource.del(key);
        }catch (JedisException e){
            log.error("redis exception error",e);
            jedisPool.returnBrokenResource(resource);
        } finally {
            jedisPool.returnResource(resource);
        }
        return res;
    }

    /**
     * 续命
     */
    public Long expire(String key,Long expire){
        Jedis resource = jedisPool.getResource();
        long res=-1L;
        try {
            res = resource.expire(key, expire);
        }catch (JedisException e){
            log.error("redis exception error",e);
            jedisPool.returnBrokenResource(resource);
        } finally {
            jedisPool.returnResource(resource);
        }
        return res;
    }
    /**
     * 获取键
     */
    public Set<String> keys(String pattern){
        Jedis resource = jedisPool.getResource();
        Set<String> keys=null;
        try {
            keys = resource.keys(pattern);
        }catch (JedisException e){
            log.error("redis exception error",e);
            jedisPool.returnBrokenResource(resource);
        } finally {
            jedisPool.returnResource(resource);
        }
        return keys;
    }
}
