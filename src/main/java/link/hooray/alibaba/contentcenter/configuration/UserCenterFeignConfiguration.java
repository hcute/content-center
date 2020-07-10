package link.hooray.alibaba.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName UserCenterFeignConfiguration
 * @Description: feign 客户端调用的日志配置，该类不应该加@Configuration注解，
 *              如果加了需要移动到@ComponentScan扫码的包以外，以免造成父子上下文重叠问题
 * @Author hooray
 * @Date 2020/7/8
 * @Version V1.0
 **/
public class UserCenterFeignConfiguration {

    @Bean
    public Logger.Level loggerLevel(){
        return Logger.Level.FULL;
    }
}
