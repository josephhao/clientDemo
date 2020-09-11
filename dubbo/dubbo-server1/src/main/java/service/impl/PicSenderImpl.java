package service.impl;

import org.apache.dubbo.config.annotation.Service;

import service.PicSender;

/**
 * //dubbo Service
 */
@Service
public class PicSenderImpl implements PicSender {
    @Override
    public String doSendPics(String msg) {
        return "server1 return ";
    }
}
