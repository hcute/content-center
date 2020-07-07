package link.hooray.alibaba.contentcenter.controller.share;

import link.hooray.alibaba.contentcenter.domain.dto.share.ShareDTO;
import link.hooray.alibaba.contentcenter.service.share.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ShareController
 * @Description: TODO
 * @Author hooray
 * @Date 2020/7/7
 * @Version V1.0
 **/
@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;

    @GetMapping("/{id}")
    public ShareDTO findById(@PathVariable Integer id) {
        return shareService.findById(id);
    }

}
