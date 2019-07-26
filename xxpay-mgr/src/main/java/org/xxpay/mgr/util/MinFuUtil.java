package org.xxpay.mgr.util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: xxpay-master
 * @Package: org.xxpay.service.channel.minfu
 * @ClassName: MinFuUtil
 * @Description:
 * @CreateDate: 2019/6/20 10:37
 * @UpdateUser: 更新者
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MinFuUtil {

    private final static DocumentBuilderFactory DOCUMENT_BUILDER = DocumentBuilderFactory.newInstance();

    public static Map<String,Object> parseXml(String nodeName, String xml) {
        try {
            final String header = "<?xml version=\"1.0\"";
            if (xml.length() <= header.length() || !xml.substring(0, 19).equals(header)) {
                return null;
            }
            DocumentBuilder documentBuilder = DOCUMENT_BUILDER.newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            NodeList nodeList = document.getElementsByTagName(nodeName);
            return parseXml(nodeList.item(0).getChildNodes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String,Object> parseXml(NodeList nodeList) {
        final Map result = new HashMap();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == 1) {
                final Map<String, Object> args = parseXml(nodeList.item(i).getChildNodes());
                if (args.size() == 0) {
                    String value = "";
                    if (nodeList.item(i).getChildNodes().getLength() > 0) {
                        value = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                    }
                    result.put(nodeList.item(i).getNodeName(), value);
                } else {
                    result.put(nodeList.item(i).getNodeName(), args);
                }
            }
        }
        return result;
    }

}
