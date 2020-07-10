package link.hooray.alibaba.contentcenter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import link.hooray.alibaba.contentcenter.dao.share.ShareMapper;
import link.hooray.alibaba.contentcenter.domain.dto.user.UserDTO;
import link.hooray.alibaba.contentcenter.domain.entity.share.Share;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
@Slf4j
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

    @Autowired
    private TestService testService;
    @GetMapping("test-a")
    public String testA() {
        testService.common();
        return "test-a";
    }

    @GetMapping("test-b")
    public String testB() {
        testService.common();
        return "test-b";
    }

    @GetMapping("test-hot")
    @SentinelResource("hot")
    public String testHot(
            @RequestParam(required = false) String a,
            @RequestParam(required = false) String b
    ){
        return a + " " + b;
    }
    @GetMapping("add-flow-rule")
    public void addFlowRule(){
        initFlowQpsRule();
    }

    /**
     *
     * 作者：大目
     * 链接：https://www.imooc.com/article/289345
     * 来源：慕课网
     * 本文原创发布于慕课网 ，转载请注明出处，谢谢合作
     */

    private void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("/share/1");
        // set limit qps to 20
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @GetMapping("/test-sentinel-api")
    public String testSentinelAPI(@RequestParam(required = false) String a){
        String resourceName = "test-sentinel-api";
        // 指定资源的限流规则只针对test-title-service;
        ContextUtil.enter(resourceName,"test-little-service");
        // 定义一个sentinel保护的资源，名称是test-sentinel-api
        Entry entry = null;
        try {

            entry = SphU.entry(resourceName);
            // 被保护的业务逻辑
            // 默认的降级规则不会统计这个异常，只会统计BlockException和他的子类，如果需要统计自定义异常进行降级，则需要在catch下面添加该异常
            if (StringUtils.isBlank(a)) {
                throw new IllegalArgumentException("a不能为空");
            }
            return a;
        }
        // 如果被保护的资源被限流或者被降级了就会抛出BlockException
        catch (BlockException e) {
            log.error("限流或者被降级" + e);
            return "限流或者被降级";
        } catch (IllegalArgumentException e2) {
            // 统计IllegalArgumentException 发生的次数或者占比
            Tracer.trace(e2);
            return "参数非法";
        }

        finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }


    /**
     * @MethodName: testSentinelResource
     * @Description: 重构Sentinel资源配置
     * @Param: [a]
     * @Return: java.lang.String
     * @Author: hooray
     * @Date: 2020/7/9
     */
    @GetMapping("/test-sentinel-resource")
    @SentinelResource(value = "test-little-resource",blockHandler = "block",fallback = "fallback")
    public String testSentinelResource(@RequestParam(required = false) String a){

        // 被保护的业务逻辑
        // 默认的降级规则不会统计这个异常，只会统计BlockException和他的子类，如果需要统计自定义异常进行降级，则需要在catch下面添加该异常
        if (StringUtils.isBlank(a)) {
            throw new IllegalArgumentException("a cannot null");
        }
        return a;
    }
    /**
     * @MethodName: testSentinelAPI
     * @Description: 处理限流或者降级
     * @Param: [a, e]
     * @Return: java.lang.String
     * @Author: hooray
     * @Date: 2020/7/9
     */
    public String testSentinelAPI(String a,BlockException e) {
        log.error("限流或者被降级 block");
        return "限流或者被降级 block";
    }
    /**
     * @MethodName: fallback
     * @Description: 处理降级，1.6 之后还可以处理Throwable
     * @Param: [a]
     * @Return: java.lang.String
     * @Author: hooray
     * @Date: 2020/7/9
     */
    public String fallback(String a){
        log.error("限流或者被降级 fallback");
        return "限流或者被降级 fallback";
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("test-rest-sentinel/{userId}")
    public UserDTO testRestTemplateSentinel(@PathVariable Integer userId){
        return restTemplate.getForObject("http://user-center/users/{id}",UserDTO.class,userId);
    }
}
