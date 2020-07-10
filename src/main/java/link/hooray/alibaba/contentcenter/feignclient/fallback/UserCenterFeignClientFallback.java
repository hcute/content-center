package link.hooray.alibaba.contentcenter.feignclient.fallback;

import link.hooray.alibaba.contentcenter.domain.dto.user.UserDTO;
import link.hooray.alibaba.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserCenterFeignClientFallback
 * @Description: fallback 处理Sentinel feign限流或降级异常
 * @Author hooray
 * @Date 2020/7/9
 * @Version V1.0
 **/
@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {
    @Override
    public UserDTO findById(Integer id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setWxNickname("一个默认的用户");
        return userDTO;
    }
}
