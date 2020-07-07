package link.hooray.alibaba.contentcenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName NacosWeightRibbonBalancer
 * @Description: ribbon 实现nacos 权重的负载均衡策略
 * @Author hooray
 * @Date 2020/7/7
 * @Version V1.0
 **/
@Slf4j
public class NacosWeightedRibbonBalancer extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        // 从配置文件加载负载均衡规则
    }

    @Override
    public Server choose(Object o) {
        try {
            BaseLoadBalancer loadBalancer =(BaseLoadBalancer) this.getLoadBalancer();
            String name = loadBalancer.getName();
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            Instance instance = namingService.selectOneHealthyInstance(name);
            log.info("选择的服务是: port = {} ,instance = {}" , instance.getPort(),instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            return null;
        }
    }
}
