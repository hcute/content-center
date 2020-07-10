package link.hooray.alibaba.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName BaiduFeignClient
 * @Description: Feign 脱离 Ribbon使用
 * @Author hooray
 * @Date 2020/7/8
 * @Version V1.0
 **/
@FeignClient(name = "baidu",url = "http://www.baidu.com")
public interface BaiduFeignClient {

    @GetMapping("")
     String index();
}
