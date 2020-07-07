package link.hooray.alibaba.contentcenter.configuration;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

/**
 * @ClassName ExtendBalancer
 * @Description: TODO
 * @Author hooray
 * @Date 2020/7/7
 * @Version V1.0
 **/
public class ExtendBalancer extends Balancer {
    public static Instance getHostByRandomWeight2(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}
