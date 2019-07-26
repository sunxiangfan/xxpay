package org.xxpay.mgr.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.xxpay.common.model.SimpleResult;
import org.xxpay.dal.dao.model.File;
import org.xxpay.mgr.service.FileService;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/upload")
    @ResponseBody
    public String index(HttpServletRequest request, MultipartHttpServletRequest multiReq) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        JSONObject object = null;
        try {
            inputStream = multiReq.getFile("file").getInputStream();
            outputStream = new ByteArrayOutputStream();
            long count = IOUtils.copy(inputStream, outputStream);
            byte[] bytes = outputStream.toByteArray();
            File file = new File();
            file.setBytes(bytes);
            fileService.add(file);
            SimpleResult result = SimpleResult.buildSucRes("上传成功！");
            object = (JSONObject) JSON.toJSON(result);
            String fileName="/file/image/"+file.getId()+".jpg";
            object.put("fileName", fileName);
        } catch (IllegalArgumentException ex) {
            SimpleResult result = SimpleResult.buildFailRes(ex.getMessage());
            object = (JSONObject) JSON.toJSON(result);
        } catch (IOException ex) {
            SimpleResult result = SimpleResult.buildFailRes("上传失败！");
            object = (JSONObject) JSON.toJSON(result);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        return object.toString();
    }

    @RequestMapping(value = "/image/{id}.jpg", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImge(@PathVariable("id") String id) {
        Assert.isTrue(StringUtils.isNoneBlank(id),"id不能为空！");
        File file = fileService.select(id);
        Assert.notNull(file, "未找到该文件。");
        byte[] bytes = file.getBytes();
        return bytes;
    }

}
