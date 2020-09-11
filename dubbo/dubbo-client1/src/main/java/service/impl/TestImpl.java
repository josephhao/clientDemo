package service.impl;

import org.apache.dubbo.config.annotation.Reference;

import org.springframework.stereotype.Service;
import service.PicSender;
import service.TestService;

@Service //spring
public class TestImpl implements TestService {
    /**
     * use dubbo Reference to invoke  dubbo server ;
     */
    @Reference
    PicSender picSender;
    @Override
    public String test(String msg) {
        return picSender.doSendPics(msg);
    }
}
