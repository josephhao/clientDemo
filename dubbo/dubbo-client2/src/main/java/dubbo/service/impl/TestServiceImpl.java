package dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import dubbo.service.TestService;
import org.springframework.stereotype.Service;
import service.PicSender;

@Service
public class TestServiceImpl implements TestService {
    @Reference
    PicSender picSender;

    @Override
    public String test(String msg) {
        return picSender.doSendPics(msg);
    }
}
