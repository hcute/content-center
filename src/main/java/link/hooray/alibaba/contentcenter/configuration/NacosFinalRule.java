package link.hooray.alibaba.contentcenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName NacosFinalRule
 * @Description: 优先选择同集群下，符合metadata的实例，如果同集群下没有符合metadata的实例，选择所有集群下，符合metadata的实例
 * @Author hooray
 * @Date 2020/7/7
 * @Version V1.0
 **/
@Slf4j
public class NacosFinalRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        try {
            String clusterName = nacosDiscoveryProperties.getClusterName();
            String targetVersion = nacosDiscoveryProperties.getMetadata().get("target-version");
            DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) getLoadBalancer();
            // 想要调用的服务的名称
            String name = loadBalancer.getName();
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            // 所有实例
            List<Instance> instances = namingService.selectInstances(name, true);

            List<Instance> metadataMatchInstances = instances;
            // 如果元数据要求匹配版本，就调用匹配的版本
            if (StringUtils.isNotBlank(targetVersion)) {
                metadataMatchInstances = instances.stream()
                        .filter(instance -> Objects.equals(targetVersion, instance.getMetadata().get("version")))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(metadataMatchInstances)) {
                    log.warn("未找到元数据匹配的目标实例，请检查配置。targetVersion ={} , instance = {}" ,targetVersion,instances);
                    return null;
                }
            }
            List<Instance> clusterMetadataMatchInstances = metadataMatchInstances;
            if (StringUtils.isNotBlank(clusterName)) {
                clusterMetadataMatchInstances = metadataMatchInstances.stream()
                        .filter(instance -> Objects.equals(clusterName,instance.getClusterName()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(clusterMetadataMatchInstances)) {
                    clusterMetadataMatchInstances = metadataMatchInstances;
                    log.warn("发生了跨集群调用，clusterName = {} , targetVersion = {} , clusterMetadataInstances = {}",
                            clusterName,
                            targetVersion,
                            clusterMetadataMatchInstances);
                }
            }

            Instance instance = ExtendBalancer.getHostByRandomWeight2(clusterMetadataMatchInstances);
            log.info("选择的实例是 port = {}, instance = {} ",instance.getPort(),instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.info("出错了:" + e);
            return null;
        }
    }
}
