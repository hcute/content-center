package link.hooray.alibaba.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

/**
 * @ClassName UserCenterRibbonConfiguration
 * @Description: java代码方式配置Ribbon
 * @Author hooray
 * @Date 2020/7/7
 * @Version V1.0
 **/
@Configuration
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {
    
}
