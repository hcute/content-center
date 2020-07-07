package link.hooray.alibaba.contentcenter;

import link.hooray.alibaba.contentcenter.dao.share.ShareMapper;
import link.hooray.alibaba.contentcenter.domain.entity.share.Share;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @ClassName TestController
 * @Description: 测试整合代码的正确性
 * @Author hooray
 * @Date 2020/7/7
 * @Version V1.0
 **/
@RestController
public class TestController {

    @Autowired
    private ShareMapper shareMapper;


    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/test")
    public List<Share> testInsert(){
        Share share = new Share();
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setTitle("XXX");
        share.setCover("XXX");
        share.setAuthor("hooray");
        share.setBuyCount(1);
        shareMapper.insertSelective(share);
        List<Share> shares = shareMapper.selectAll();
        return shares;
    }

    @GetMapping("/test2")
    public List<ServiceInstance> getInstances(){
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        return instances;
    }
}
