package com.wlm.wxdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.wlm.wxdemo.model.wx.WxMenu;
import com.wlm.wxdemo.model.wx.WxProperties;
import com.wlm.wxdemo.service.WxServiceImpl;
import com.wlm.wxdemo.utils.XmlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author wuliming
 * @date 2021/12/15 14:23
 */
@Api(tags = "微信公众号接口测试")
@RestController
@RequestMapping("/wx")
public class WxController {

    private WxProperties wxProperties;

    @Autowired
    public void setWxProperties(WxProperties wxProperties) {
        this.wxProperties = wxProperties;
    }

    private WxServiceImpl wxService;

    @Autowired
    public void setWxService(WxServiceImpl wxService) {
        this.wxService = wxService;
    }

    @ApiOperation(value = "获取微信 access_token")
    @ApiOperationSupport(author = "wlm", order = 1)
    @GetMapping("/get_token")
    public String getToken() {
        return wxService.getAccessToken(wxProperties.getAppId(), wxProperties.getAppSecret());
    }

    @ApiOperation(value = "创建自定义菜单")
    @ApiOperationSupport(author = "wlm", order = 2)
    @GetMapping("/create_menu")
    public JSONObject createMenu() {
        WxMenu menu = XmlUtils.menuParse();
        return wxService.createMenu(menu);
    }

    @ApiOperation(value = "文件转换测试")
    @ApiOperationSupport(author = "wlm", order = 3)
    @PostMapping("/upload_img")
    public String uploadImg(@RequestParam("file") MultipartFile file) {
        if (file != null) {
            try {
                // MultipartFile -> File
                String fileNewName = String.valueOf(System.currentTimeMillis());
                String filePath  = "D:\\uploadFile\\" ;
                File mm = new File(filePath + File.pathSeparator + fileNewName + ".jpg");
                FileUtils.copyInputStreamToFile(file.getInputStream(), mm);

                // File -> MultipartFile
                File newFile = new File("src/main/resources/input.txt");
                FileInputStream input = new FileInputStream(newFile);
                MultipartFile multipartFile =new MockMultipartFile("file", newFile.getName(), "text/plain", IOUtils.toByteArray(input));
                System.out.println(multipartFile);

                return mm.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("文件是空的");
        }

        return null;
    }

    @ApiOperation(value = "微信永久素材上传")
    @ApiOperationSupport(author = "wlm", order = 4)
    @PostMapping("/upload")
    public JSONObject upload(@RequestParam("file") MultipartFile file) {
        return wxService.commUpload(file, String.valueOf(System.currentTimeMillis()), "demo");
    }

    @ApiOperation(value = "微信素材列表获取")
    @ApiOperationSupport(author = "wlm", order = 5)
    @GetMapping("/get_material_list/{type}")
    public JSONObject getMaterialList(@PathVariable("type") String type) {
        return wxService.getMaterialList(type);
    }

    @ApiOperation(value = "发送模板消息")
    @ApiOperationSupport(author = "wlm", order = 6)
    @GetMapping("/send_template")
    public JSONObject sendTemplate() {
        return wxService.sendTemplate();
    }

    @ApiOperation(value = "获取模板列表")
    @ApiOperationSupport(author = "wlm", order = 7)
    @GetMapping("/get_template_list")
    public JSONObject getTemplateList() {
        return wxService.getTemplateList();
    }

}
