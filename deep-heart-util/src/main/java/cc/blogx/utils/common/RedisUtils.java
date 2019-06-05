package cc.blogx.utils.common;

import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisUtils {

    private final static long expired = 12 * 60 * 60;
    private static StringRedisTemplate redisTemplate;

    private static StringRedisTemplate getRedisTemplate() {
        if (null == redisTemplate) {
            redisTemplate = AppCtxUtils.getBean(StringRedisTemplate.class);
        }
        return redisTemplate;
    }

    public static void set(String key, String value) {
        RedisUtils.set(key, value, expired);
    }

    public static void set(String key, String value, long time) {
        RedisUtils.getRedisTemplate().opsForValue().set(key, value, time);
    }

    public static String get(String key) {
        return RedisUtils.getRedisTemplate().opsForValue().get(key);
    }
}
