package link.hooray.alibaba.contentcenter.service.share;

import link.hooray.alibaba.contentcenter.dao.share.ShareMapper;
import link.hooray.alibaba.contentcenter.domain.dto.share.ShareDTO;
import link.hooray.alibaba.contentcenter.domain.dto.user.UserDTO;
import link.hooray.alibaba.contentcenter.domain.entity.share.Share;
import link.hooray.alibaba.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @ClassName ShareService
 * @Description: TODO
 * @Author hooray
 * @Date 2020/7/7
 * @Version V1.0
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ShareService {

    private final ShareMapper shareMapper;

//    private final RestTemplate restTemplate;

    private final DiscoveryClient discoveryClient;

    private final UserCenterFeignClient userCenterFeignClient;

    public ShareDTO findById(Integer id){

        Share share = shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();

        // 使用原生的服务发现调用微服务
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        String targetURL = instances.stream()
//                .map(instance -> instance.getUri().toString() + "/users/{id}")
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("当前没有可用度饿实例"));

        // 手动实现负载均衡
//        List<String> targetURLS = instances.stream()
//                .map(instance -> instance.getUri().toString() + "/users/{id}")
//                .collect(Collectors.toList());
//
//        int i = ThreadLocalRandom.current().nextInt(targetURLS.size());
//
//        String targetURL = targetURLS.get(i);

        // 使用Ribbon整合restTemplate 实现负载均衡


        // 这里存在问题，代码不可读，复杂url好烦，引出了feign
        /*String targetURL = "http://user-center/users/{id}";
        log.info("请求的目标地址：{}" ,targetURL);
        UserDTO userDTO = this.restTemplate.getForObject(targetURL, UserDTO.class, userId);*/

        //使用feignClient 实现
        UserDTO userDTO = userCenterFeignClient.findById(id);
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share,shareDTO);
        shareDTO.setWxNickName(userDTO.getWxNickname());


        return shareDTO;
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject("http://localhost:8080/users/{id}", String.class,1);
        System.out.println(forObject);
    }
}
