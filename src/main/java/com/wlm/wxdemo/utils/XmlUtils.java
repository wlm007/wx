package com.wlm.wxdemo.utils;

import com.wlm.wxdemo.model.wx.WxMenu;
import com.wlm.wxdemo.model.wx.WxMenuButton;
import jdk.internal.util.xml.impl.ReaderUTF8;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;

/**
 * @author wuliming
 * @date 2021/12/16 15:07
 */
public class XmlUtils {

    public static WxMenu menuParse() {
        WxMenu menu = new WxMenu();
        SAXBuilder builder = new SAXBuilder();
        InputStream io = XmlUtils.class.getResourceAsStream("/xml/menu.xml");
        System.out.println("文件读取流:" + io);
        Document doc;
        try {
            Reader reader = new ReaderUTF8(io);
            doc = builder.build(reader);
            // 获取根元素
            Element root = doc.getRootElement();
            // 获取根元素一级子元素
            List<Element> children = root.getChildren();
            WxMenuButton[] buttons = new WxMenuButton[children.size()];
            int i = 0;
            for (Element item : children) {
                String submenu = item.getAttributeValue("submenu");
                int j = 0;
                if ("true".equals(submenu)) {
                    String name = item.getAttributeValue("name");
                    List<Element> list = item.getChild("sub_button").getChildren();
                    WxMenuButton[] subList = new WxMenuButton[list.size()];
                    for (Element e : list) {
                        setButton(subList, j, e);
                        j++;
                    }
                    WxMenuButton mainButton = new WxMenuButton();
                    mainButton.setName(name);
                    mainButton.setSub_button(subList);
                    buttons[i] = mainButton;
                } else {
                    setButton(buttons, i, item);
                }
                i++;
            }
            menu.setButton(buttons);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (io != null) {
                io.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menu;
    }

    private static void setButton(WxMenuButton[] buttons, int i, Element item) {
        String click = "click";
        String view = "view";
        String subName = item.getAttributeValue("name");
        String subType = item.getAttributeValue("type");
        WxMenuButton button = new WxMenuButton();
        button.setName(subName);
        button.setType(subType);
        if (click.equals(subType)) {
            String subKey = item.getAttributeValue("key");
            button.setKey(subKey);
        }
        if (view.equals(subType)) {
            String subUrl = item.getAttributeValue("url");
            button.setUrl(subUrl);
        }
        buttons[i] = button;
    }

    public static void main(String[] args) {
        menuParse();
    }
}
