package link.hooray.alibaba.contentcenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName NacosSameClusterRibbonConfiguration
 * @Description: nacos服务同集群优先调用
 * @Author hooray
 * @Date 2020/7/7
 * @Version V1.0
 **/
@Slf4j
public class NacosSameClusterRibbonConfiguration extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        // 基于配置文件的负载均衡策略
    }

    @Override
    public Server choose(Object o) {
        try {
            // 获取当前服务所在的集群
            String clusterName = nacosDiscoveryProperties.getClusterName();

            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            // 获取想要请求的微服务的名称
            String name = loadBalancer.getName();

            // 获取服务发现的相关信息
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            // 获取指定服务的所有实例 A
            List<Instance> instances = namingService.selectInstances(name, true);
            List<Instance> sameClusterInstances = instances.stream()
                    .filter(instance -> Objects.equals(instance.getClusterName(), clusterName))
                    .collect(Collectors.toList());
            List<Instance> instancesToBeChosen = new ArrayList<>();
            if (CollectionUtils.isEmpty(instancesToBeChosen)) {
                instancesToBeChosen = instances;
                log.info("发生跨集群调用, name = {} , clusterName = {} , instances = {} ",name,clusterName,instances);
            } else {
                instancesToBeChosen = sameClusterInstances;
            }
            // 基于权重的负载均衡算法，选择一个实例
            Instance instance = ExtendBalancer.getHostByRandomWeight2(instances);
            log.info("选择的实例是 port = {}, instance = {} ",instance.getPort(),instance);

            return new NacosServer(instance);

        } catch (NacosException e) {
            log.error("发生错误了：" + e);
            return null;
        }
    }
}

