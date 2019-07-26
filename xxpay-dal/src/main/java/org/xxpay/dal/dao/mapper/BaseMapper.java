package org.xxpay.dal.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper<TModel, TExample> {
    int countByExample(TExample example);

    int deleteByExample(TExample example);

    int deleteByPrimaryKey(String id);

    int insert(TModel record);

    int insertSelective(TModel record);

    List<TModel> selectByExample(TExample example);

    TModel selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TModel record, @Param("example") TExample example);

    int updateByExample(@Param("record") TModel record, @Param("example") TExample example);

    int updateByPrimaryKeySelective(TModel record);

    int updateByPrimaryKey(TModel record);
}
