package com.wlm.wxdemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlm.wxdemo.dao.WxTokenDao;
import com.wlm.wxdemo.enums.FileEnum;
import com.wlm.wxdemo.enums.WxEventEnums;
import com.wlm.wxdemo.enums.WxMsgTypeEnums;
import com.wlm.wxdemo.model.wx.*;
import com.wlm.wxdemo.params.wx.WxMaterialListParams;
import com.wlm.wxdemo.utils.MessageUtils;
import com.wlm.wxdemo.utils.WxDicts;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.UUID;

/**
 * @author wuliming
 * @date 2021/12/16 9:52
 */
@Service
@Data
public class WxServiceImpl {

    /**
     * 获取token 200次/天
     */
    private String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=appId&secret=appSecret";

    /**
     * 菜单创建  100 次/天
     */
    private String menuCreateUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 获取用户列表
     */
    private String getUserListUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";

    /**
     * 获取用户基本信息  500000 次/天
     */
    private String accessUserUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";

    /**
     * 发送模板消息
     */
    private String messageSendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 获取微信服务器ip地址
     */
    private String getWxApiListUrl = "https://api.weixin.qq.com/cgi-bin/get_api_domain_ip?access_token=ACCESS_TOKEN";

    /**
     * 上传图片
     */
    private String uploadImgUrl = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";

    /**
     * 上传临时素材
     */
    private String uploadMediaUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    /**
     * 上传永久素材
     */
    private String uploadMaterialUrl = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";

    /**
     * 获取永久素材列表
     */
    private String getMaterialListUrl = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";

    /**
     * 获取模板列表
     */
    private String getTemplateListUrl = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";

    /**
     * 发送模板接口 post
     */
    private String sendTemplateUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 创建临时/永久二维码ticket
     */
    private String createTicketUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";

    /**
     * 获取微信服务器ip地址
     */
    private String getWeiXinUrlList = "https://api.weixin.qq.com/cgi-bin/get_api_domain_ip?access_token=ACCESS_TOKEN";

    /**
     * 通过ticket获取/展示 二维码
     */
    private String showTicketUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";

    /**
     * token  2小时有效期
     */
    private String token;

    /**
     * token过期时间 7200000ms - 7200s - 120m - 2h
     */
    private Integer expiresIn = 7200000;

    private final String GET = "GET";

    private final String POST = "POST";

    private final String ACCESS_TOKEN = "ACCESS_TOKEN";

    private WxTokenDao wxTokenDao;

    @Autowired
    public void setWxTokenDao(WxTokenDao wxTokenDao) {
        this.wxTokenDao = wxTokenDao;
    }

    public String getAccessToken(String appId, String appSecret) {
        Long now = System.currentTimeMillis();
        // 微信token失效时间为7200秒 在此时间内再次请求获取token时直接返回上一次保存的token
        WxToken wxToken = wxTokenDao.selectByPrimaryKey(1);
        if (wxToken == null) {
            String url = accessTokenUrl.replace("appId", appId).replace("appSecret", appSecret);
            JSONObject result = httpsRequest(url, GET, null);
            // 将token保存并记录当前时间
            token = result.getString("access_token");
            wxToken = new WxToken();
            wxToken.setAccessToken(token);
            wxToken.setUpdateTime(now);
            wxTokenDao.insert(wxToken);
        } else {
            // 如果token存在则判断是否2小时过期
            Long updateTime = wxToken.getUpdateTime();
            if (now - updateTime > expiresIn) {
                String url = accessTokenUrl.replace("appId", appId).replace("appSecret", appSecret);
                JSONObject result = httpsRequest(url, GET, null);
                token = result.getString("access_token");
                wxToken.setAccessToken(token);
                wxToken.setUpdateTime(now);
                wxTokenDao.updateByPrimaryKey(wxToken);
            } else {
                token = wxToken.getAccessToken();
            }
        }
        return token;
    }

    public JSONObject createMenu(WxMenu menu) {
        String url = menuCreateUrl.replace(ACCESS_TOKEN, token);
        String menuJson = JSONObject.toJSON(menu).toString();
        System.out.println(menuJson);
        return httpsRequest(url,POST, menuJson);
    }

    private String imageMediaId = "0EXTCJ7mnwSk3SHfVjyH7Ch_lbqAxrlfNHIIshs1d1Jts_uCg0Sab0L2HsNnohmV";

    public JSONObject uploadImg(File file) {
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("文件不存在");
        }
        String url = uploadMaterialUrl.replace(ACCESS_TOKEN, token).replace("TYPE", "image");

        StringBuilder sb = new StringBuilder();
        StringBuilder resultJson = new StringBuilder();
        try {
            HttpsURLConnection conn = getHttpsConn(url);
            if (null == conn) {
                throw new RuntimeException("建立网络连接失败");
            }
            // 设置请求方式
            conn.setRequestMethod("POST");
            // 输入输出开启
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 设置请求头信息
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");

            // 设置边界
            String boundary = "----------" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // 请求正文信息
            // 第一部分：
            // 必须多两道线
            sb.append("--");
            sb.append(boundary);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"").append(file.length())
                    .append("\";filename=\"").append(file.getName()).append("\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");

            byte[] head = sb.toString().getBytes(StandardCharsets.UTF_8);
            // 获得输出流
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 输出表头
            out.write(head);

            // 文件正文部分
            // 把文件已流文件的方式 推入到url中
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();

            // 结尾部分
            // 定义最后数据分隔线
            byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);
            out.write(foot);
            out.flush();
            out.close();


            readResultInput(resultJson, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject result = JSONObject.parseObject(resultJson.toString());
        System.out.println(result);
        imageMediaId = result.getString("media_id");
        return result;

    }

    private void readResultInput(StringBuilder sb, HttpsURLConnection conn) throws IOException {
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bis = new BufferedReader(isr);
        String str;
        while ((str = bis.readLine()) != null) {
            sb.append(str);
        }
        bis.close();
        isr.close();
        is.close();
        conn.disconnect();
    }

    private JSONObject httpsRequest(String url, String questType, String outputStr) {
        StringBuilder sb = new StringBuilder();
        JSONObject result = null;
        try {
            System.out.println("-------------开始请求微信公众号后台-----------------------");
            HttpsURLConnection conn = getHttpsConn(url);
            if (null == conn) {
                throw new RuntimeException("建立网络连接失败");
            }
            // 设置请求方式
            conn.setRequestMethod(questType);
            if (GET.equalsIgnoreCase(questType)) {
                conn.getContent();
            }
            if (null != outputStr) {
                // 输入输出开启
                conn.setDoOutput(true);
                conn.setDoInput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(outputStr.getBytes(StandardCharsets.UTF_8));
                out.flush();
                out.close();
            }
            readResultInput(sb, conn);
            System.out.println("-------------结束请求微信公众号后台-----------------------");
            result = JSON.parseObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private HttpsURLConnection getHttpsConn(String url) {
        HttpsURLConnection conn = null;
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {}
                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {}
                @Override
                public X509Certificate[] getAcceptedIssuers() {return null;}
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = ctx.getSocketFactory();
            URL httpUrl = new URL(url);
            //建立连接
            conn = (HttpsURLConnection) httpUrl.openConnection();
            conn.setSSLSocketFactory(ssf);
            //设置不要缓存
            conn.setUseCaches(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public JSONObject commUpload(MultipartFile file, String uploadDateStr, String system) {
        String basePath = "/root/temp/";
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new RuntimeException("文件不存在");
        }
        // 获取文件扩展名
        String fileExt = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        // 定义文件存放目录
        String dirPath = system + "/" + uploadDateStr + "/" + FileEnum.getFilePath(fileExt) +"/";
        // 获取文件具体路径
        String newFilePath = dirPath + UUID.randomUUID() + "." + fileExt;
        // 文件存放目录是否存在,不存在新增
        File uploadFilePath = new File(basePath + dirPath);
        if (!uploadFilePath.exists()) {
            boolean isMkdir = uploadFilePath.mkdirs();
            if (!isMkdir) {
                throw new RuntimeException("文件创建异常!文件路径为:" + newFilePath);
            }
        }
        // 新建文件
        File newFile = new File(basePath + newFilePath);
        try {
            FileCopyUtils.copy(file.getBytes(), newFile);
            System.out.println(newFile.getPath());
            JSONObject result = uploadImg(newFile);
            // 素材上传到微信后删除本地文件
            boolean isDelete = newFile.delete();
            if (!isDelete) {
                throw new RuntimeException("文件删除失败!文件路径为:" + newFilePath);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("文件复制异常!文件路径为:" + newFilePath);
        }
    }

    public void handleWxMsg(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> receiverMsg = MessageUtils.parseXml(request);
        System.out.println("微信发送消息内容: " + receiverMsg);
        String msgType = receiverMsg.get(WxDicts.WX_MSG_TYPE);
        String resStr = "";

        // 处理点击事件消息
        if (WxMsgTypeEnums.WX_MSG_TYPE_EVENT.getMsgType().equals(msgType)) {
            String eventType = receiverMsg.get(WxDicts.WX_EVENT);
            if (WxEventEnums.WX_EVENT_TYPE_CLICK.getEventType().equals(eventType)) {
                String eventKey = receiverMsg.get(WxDicts.WX_EVENT_KEY);
                String m00003 = "M0003";
                String m000201 = "M0002_01";
                if (m00003.equals(eventKey)) {
                    ImageMessage imageMessage = new ImageMessage();
                    this.setMsgData(receiverMsg, imageMessage, WxMsgTypeEnums.WX_MSG_TYPE_IMAGE.getMsgType());
                    imageMessage.setImage(new Image(imageMediaId));
                    resStr = MessageUtils.imageMessageToXml(imageMessage);
                } else if (m000201.equals(eventKey)) {
                    ArticlesMessage articlesMessage = new ArticlesMessage();
                    articlesMessage.setArticleCount(1);
                    this.setMsgData(receiverMsg, articlesMessage, WxMsgTypeEnums.WX_MSG_TYPE_NEWS.getMsgType());
                    Articles articles = new Articles();
                    ArticlesItem item = new ArticlesItem();
                    item.setTitle("test");
                    item.setDescription("test");
                    item.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/ibTxdMwOicibduncxldKmefBCGA10RcJ6y7TicEIiaiaRW9RrjblJc10W7oqluEOcZPFOwCG8mg1WRlpljBMR6dulD8g/0?wx_fmt=jpeg");
                    item.setUrl("https://www.baidu.com");
                    articles.setItem(item);
                    articlesMessage.setArticles(articles);
                    resStr = MessageUtils.articlesMessageToXml(articlesMessage);
                } else {
                    TextMessage textMessage = new TextMessage();
                    textMessage.setContent("你好");
                    this.setMsgData(receiverMsg, textMessage, WxMsgTypeEnums.WX_MSG_TYPE_TEXT.getMsgType());
                    resStr = MessageUtils.textMessageToXml(textMessage);
                }
            } else if (WxEventEnums.WX_EVENT_TYPE_SUBSCRIBE.getEventType().equals(eventType)) {
                // 关注事件
                TextMessage textMessage = new TextMessage();
                textMessage.setContent("感谢您的订阅");
                this.setMsgData(receiverMsg, textMessage, WxMsgTypeEnums.WX_MSG_TYPE_TEXT.getMsgType());
                resStr = MessageUtils.textMessageToXml(textMessage);
            } else if (WxEventEnums.WX_EVENT_TYPE_UNSUBSCRIBE.getEventType().equals(eventType)) {
                // 取消关注
                TextMessage textMessage = new TextMessage();
                textMessage.setContent("再见，祝您生活愉快");
                this.setMsgData(receiverMsg, textMessage, WxMsgTypeEnums.WX_MSG_TYPE_TEXT.getMsgType());
                resStr = MessageUtils.textMessageToXml(textMessage);
            }
        } else {
            // 处理其他消息
            if (WxMsgTypeEnums.WX_MSG_TYPE_TEXT.getMsgType().equals(msgType)) {
                TextMessage textMessage = new TextMessage();
                textMessage.setContent("你好");
                this.setMsgData(receiverMsg, textMessage, WxMsgTypeEnums.WX_MSG_TYPE_TEXT.getMsgType());
                resStr = MessageUtils.textMessageToXml(textMessage);
            }
        }
        System.out.println("被动回复消息内容：\n" + resStr);
        // 设置返回字符集为utf-8
        response.setCharacterEncoding(MessageUtils.DEFAULT_ENCODE);
        try {
            response.getWriter().write(resStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMsgData(Map<String, String> receiverMsg, BaseMessage message, String msgType) {
        message.setToUserName(receiverMsg.get(WxDicts.WX_FROM_USER_NAME));
        message.setFromUserName(receiverMsg.get(WxDicts.WX_TO_USER_NAME));
        message.setCreateTime(System.currentTimeMillis());
        message.setMsgType(msgType);
    }

    public JSONObject getMaterialList(String type) {
        String url = getMaterialListUrl.replace(ACCESS_TOKEN, token);
        WxMaterialListParams params = new WxMaterialListParams();
        params.setType(type);
        params.setCount(20);
        params.setOffset(0);
        return httpsRequest(url, POST, JSON.toJSONString(params));
    }

    public JSONObject getTemplateList() {
        String url = getTemplateListUrl.replace(ACCESS_TOKEN, token);
        return httpsRequest(url, GET, null);
    }

    public JSONObject sendTemplate() {
        String url = sendTemplateUrl.replace(ACCESS_TOKEN, token);
        String outStr =
           "{" +
                "\"touser\":\"oR4y25mJdVv2Ww4CuXaoSioD9VsM\"," +
                "\"template_id\":\"FV1U0nfXaTeJhLTsC2Fq7wiB7rZXIOjQQpcX43683_0\"," +
                "\"topcolor\":\"#FF0000\"," +
                "\"data\":{" +
                    "\"card\":{" +
                        "\"value\":\"0426\"," +
                        "\"color\":\"#173177\"" +
                    "}," +
                   "\"date\":{" +
                       "\"value\":\"01月07日 11时24分\"," +
                       "\"color\":\"#173177\"" +
                   "}," +
                   "\"type\":{" +
                       "\"value\":\"消费\"," +
                       "\"color\":\"#173177\"" +
                   "}," +
                   "\"money\":{" +
                       "\"value\":\"134元\"," +
                       "\"color\":\"#173177\"" +
                   "}," +
                   "\"deadTime\":{" +
                       "\"value\":\"01月07日 11时24分\"," +
                       "\"color\":\"#173177\"" +
                   "}," +
                   "\"left\":{" +
                       "\"value\":\"6542.6\"," +
                       "\"color\":\"#173177\"" +
                   "}" +
                "}" +
           "}";
        return httpsRequest(url, POST, outStr);
    }

    public JSONObject createTicket(boolean isTemporary, boolean isStr) {
        String url = createTicketUrl.replace(ACCESS_TOKEN, token);
        String actionName;
        if (isTemporary) {
            actionName = isStr ? "QR_STR_SCENE" : "QR_SCENE";
        } else {
            actionName = isStr ? "QR_LIMIT_STR_SCENE" : "QR_LIMIT_SCENE";
        }
        String temporaryParams =
                "{" +
                        "\"expire_seconds\":604800," +
                        "\"action_name\":\""+actionName+"\"," +
                        "\"action_info\":{" +
                            "\"scene\":{" +
                                (isStr ? "\"scene_str\":\"test\"" : "\"scene_id\":123") +
                            "}" +
                        "}" +
                "}";
        JSONObject result = httpsRequest(url, POST, temporaryParams);
        result.put("get_ticket", showTicketUrl.replace("TICKET", result.getString("ticket")));
        return result;
    }

    public JSONObject getWeiXinIpList() {
        String url = getWeiXinUrlList.replace(ACCESS_TOKEN, token);
        return httpsRequest(url, GET, null);
    }

    public JSONObject getUserList(String openId) {
        String url = getUserListUrl.replace(ACCESS_TOKEN, token).replace("NEXT_OPENID", openId == null ? "" : openId);
        return httpsRequest(url, GET, null);
    }
}
