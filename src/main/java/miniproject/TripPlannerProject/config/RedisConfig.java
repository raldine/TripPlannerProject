package miniproject.TripPlannerProject.config;

import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {
    private final Logger logger = Logger.getLogger(RedisConfig.class.getName());

    // get all redis config into the class
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.database}")
    private int redisDatabase;

    @Value("${spring.data.redis.username}")
    private String redisUsername;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    // the following means if someone calls for redis-0 --> it will create this
    @Bean("redis-0")
    public RedisTemplate<String, String> createRedisTemplate() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        // Sets the database - select 0
        config.setDatabase(redisDatabase);

        // set Username and Password if they are set
        if (!redisUsername.trim().equals("")) {
            logger.info("Setting Redis username and password");
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
        }

        // create a connection to database
        JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

        // create a factory to connect to Redis
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        // Create the RedisTempalte
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFac);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // data (i.e. objects) entered in Redis database
                                                                     // will become a neutral format so that if
                                                                     // python/other language apps read database --> can
                                                                     // be read/writtern
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());// same as above but for keys of maps
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    // if you want to insert java object to database
    @Bean("redis-0-object")
    public RedisTemplate<String, Object> createRedisTemplateObject() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        // Sets the database - select 0
        config.setDatabase(redisDatabase);

        // set Username and Password if they are set
        if (!redisUsername.trim().equals("")) {
            logger.info("Setting Redis username and password");
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
        }

        // create a connection to database
        JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

        // create a factory to connect to Redis
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        // Create the RedisTempalte
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFac);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // data (i.e. objects) entered in Redis database
                                                                     // will become a neutral format so that if
                                                                     // python/other language apps read database --> can
                                                                     // be read/writtern
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());// same as above but for keys of maps
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
    
}
