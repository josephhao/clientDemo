package dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import service.PicSender;

@Service(loadbalance = "roundrobin")
public class PicSenderImpl implements PicSender {
    @Override
    public String doSendPics(String msg) {
        return "dubbo server2 return : " + msg;
    }
}
