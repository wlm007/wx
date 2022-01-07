package com.wlm.wxdemo.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.wlm.wxdemo.model.wx.ImageMessage;
import com.wlm.wxdemo.model.wx.TextMessage;
import jdk.internal.util.xml.impl.ReaderUTF8;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信消息接受和发送转换工具
 * @author wuliming
 * @date 2022/1/5 17:07
 */
public class MessageUtils {

    public static final String DEFAULT_ENCODE = "UTF-8";

    /**
     * 自定义xStream 使其支持<![CDATA[text]]>
     */
    public static XStream xStream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                boolean cData = true;
                @Override
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                    // 业务处理 针对 CreateTime 标签不添加 CDATA
                    String createTime = "CreateTime";
                    cData = !createTime.equals(name);
                }
                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cData) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * 解析微信通过post发送过劳的消息
     * @param request request
     * @return 解析后的map
     */
    public static Map<String, String> parseXml(HttpServletRequest request) {

        Map<String, String> map = null;
        try {
            InputStream io = request.getInputStream();
            SAXBuilder builder = new SAXBuilder();
            Reader reader = new ReaderUTF8(io);
            Document doc = builder.build(reader);

            // 获取根元素
            Element root = doc.getRootElement();
            // 获取根元素一级子元素
            List<Element> children = root.getChildren();
            map = new HashMap<>(children.size());

            for (Element child : children) {
                map.put(child.getName(), child.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void receiveResponse(HttpServletResponse response, String msg) {
        try {
            OutputStream os = response.getOutputStream();
            byte[] data = msg.getBytes(StandardCharsets.UTF_8);
            os.write(data);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String XML = "xml";

    /**
     * 文本消息 -> xml
     * @param textMessage 文本消息
     * @return 文本消息转成的xml
     */
    public static String textMessageToXml(TextMessage textMessage) {
        xStream.alias(XML, textMessage.getClass());
        return xStream.toXML(textMessage);
    }

    /**
     * 图片消息 -> xml
     * @param imageMessage 图片消息
     * @return 图片转成的xml
     */
    public static String imageMessageToXml(ImageMessage imageMessage) {
        xStream.alias(XML, imageMessage.getClass());
        return xStream.toXML(imageMessage);
    }
}
