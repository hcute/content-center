package link.hooray.alibaba.contentcenter.feignclient.fallbackfactory;

import feign.hystrix.FallbackFactory;
import link.hooray.alibaba.contentcenter.domain.dto.user.UserDTO;
import link.hooray.alibaba.contentcenter.feignclient.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserCenterFeignClientFallbackFactory
 * @Description: TODO
 * @Author hooray
 * @Date 2020/7/9
 * @Version V1.0
 **/
@Component
@Slf4j
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {
    @Override
    public UserCenterFeignClient create(Throwable throwable) {
        return new UserCenterFeignClient() {
            @Override
            public UserDTO findById(Integer id) {
                log.error("远程调用被限流或者降级了",throwable);
                UserDTO userDTO = new UserDTO();
                userDTO.setId(id);
                userDTO.setWxNickname("一个默认的用户");
                return userDTO;
            }
        };
    }
}
