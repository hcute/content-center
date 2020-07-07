package ribbonconfiguration;

import com.netflix.loadbalancer.IRule;
import link.hooray.alibaba.contentcenter.configuration.NacosFinalRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName Ribbon
 * @Description: TODO
 * @Author hooray
 * @Date 2020/7/7
 * @Version V1.0
 **/
@Configuration
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule(){
        return new NacosFinalRule();
    }
}
