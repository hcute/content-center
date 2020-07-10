package link.hooray.alibaba.contentcenter;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName Sentinel
 * @Description: TODO
 * @Author hooray
 * @Date 2020/7/9
 * @Version V1.0
 **/
public class SentinelTest {

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10000 ; i++) {
            String forObject = restTemplate.getForObject("http://localhost:8010/actuator/sentinel", String.class);
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }
}
