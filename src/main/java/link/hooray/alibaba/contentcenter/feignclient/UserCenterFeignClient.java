package link.hooray.alibaba.contentcenter.feignclient;

import link.hooray.alibaba.contentcenter.configuration.UserCenterFeignConfiguration;
import link.hooray.alibaba.contentcenter.domain.dto.user.UserDTO;
import link.hooray.alibaba.contentcenter.feignclient.fallback.UserCenterFeignClientFallback;
import link.hooray.alibaba.contentcenter.feignclient.fallbackfactory.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "user-center",configuration = UserCenterFeignConfiguration.class)
@FeignClient(name = "user-center",
//        fallback = UserCenterFeignClientFallback.class,
        fallbackFactory = UserCenterFeignClientFallbackFactory.class
)
public interface UserCenterFeignClient {

    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable Integer id);
}
