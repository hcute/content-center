package link.hooray.alibaba.contentcenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import link.hooray.alibaba.contentcenter.configuration.GlobalFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("link.hooray.alibaba")
@SpringBootApplication
@EnableFeignClients // (defaultConfiguration = GlobalFeignConfiguration.class)
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }


    // @Bean 在spring 容器中创建对象类型为 RestTemplate id / 名称为restTemplate 的实例

    // restTemplate 整合Ribbon
    @Bean
    @LoadBalanced
    // restTemplate 整合 Sentinel
    @SentinelRestTemplate
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
