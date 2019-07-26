package org.xxpay.agent.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.common.util.MySeq;
import org.xxpay.dal.dao.mapper.FileMapper;
import org.xxpay.dal.dao.model.File;
import org.xxpay.dal.dao.model.FileExample;

import java.util.List;

@Component
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    public int add(File info) {
        info.setId(MySeq.getFile());
        return fileMapper.insertSelective(info);
    }

    public int update(File info) {
        return fileMapper.updateByPrimaryKeySelective(info);
    }

    public File select(String id) {
        return fileMapper.selectByPrimaryKey(id);
    }

    public List<File> getList(int offset, int limit, File info) {
        FileExample example = new FileExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        FileExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return fileMapper.selectByExample(example);
    }


    public Integer count(File info) {
        FileExample example = new FileExample();
        FileExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, info);
        return fileMapper.countByExample(example);
    }

    void setCriteria(FileExample.Criteria criteria, File info) {
        if(info != null) {
            if(StringUtils.isNotBlank(info.getId())) criteria.andIdEqualTo(info.getId());
        }
    }
}
