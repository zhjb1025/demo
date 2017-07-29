package com.demo.mapper;

import com.demo.mapper.BranchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BranchInfoMapper {

    List<BranchInfo> getAll();

    int insert(BranchInfo record);

    BranchInfo selectByID(Integer id);

    int updateByID(BranchInfo record);

    @Select( "select max(branch_code) from branch_info where  parent_id=#{parentId,jdbcType=INTEGER}")
    String getMaxBranchCode(@Param("parentId") Integer parentId);

}