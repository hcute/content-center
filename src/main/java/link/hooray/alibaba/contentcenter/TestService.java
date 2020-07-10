package link.hooray.alibaba.contentcenter;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName TestService
 * @Description: TODO
 * @Author hooray
 * @Date 2020/7/9
 * @Version V1.0
 **/
@Service
@Slf4j
public class TestService {

    @SentinelResource("common")
    public String common(){
        log.info("common....");
        return "common";
    }
}
