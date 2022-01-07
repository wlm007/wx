package com.wlm.wxdemo.controller;

import com.wlm.wxdemo.service.WxServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接受微信公众号的请求处理接口
 * @author wuliming
 * @date 2021/12/15 14:23
 */
@Api(tags = "微信公众号请求处理")
@RestController
@RequestMapping("/wx")
public class WxInController {

    private WxServiceImpl wxService;

    @Autowired
    public void setWxService(WxServiceImpl wxService) {
        this.wxService = wxService;
    }

    @GetMapping("/weixin")
    public String inWeiXin(String signature,String nonce,String timestamp,String echostr){
        System.out.println(signature+"--"+nonce+"--"+timestamp+"--"+echostr);
        return echostr;
    }

    @PostMapping("/weixin")
    public void msgWeiXin(HttpServletRequest request, HttpServletResponse response) {
        wxService.handleWxMsg(request, response);
    }
}
