package com.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MetaDataMapper {

    @Select("select id ,meta_group as metaGroup,meta_code as metaCode,remark from metadata where meta_group=#{metaGroup,jdbcType=VARCHAR}")
    List<Metadata> queryMetaDataList(@Param("metaGroup") String  metaGroup);

    @Select("select id ,meta_group as metaGroup,meta_code as metaCode,remark from metadata " +
            "where meta_group=#{metaGroup,jdbcType=VARCHAR} and meta_code=#{metaCode,jdbcType=VARCHAR}")
    Metadata queryMetaData(@Param("metaGroup") String  metaGroup, @Param("metaCode") String metaCode );

}