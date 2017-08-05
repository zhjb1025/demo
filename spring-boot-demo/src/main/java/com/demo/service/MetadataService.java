package com.demo.service;

import com.demo.mapper.MetaDataMapper;
import com.demo.mapper.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetadataService {
    @Autowired
    private MetaDataMapper metaDataMapper;


    public List<Metadata> queryMetaDataList(String  metaGroup){
        return metaDataMapper.queryMetaDataList(metaGroup);
    }

    public Metadata queryMetaData(String  metaGroup){
        List<Metadata> list = metaDataMapper.queryMetaDataList(metaGroup);
        if (list!=null){
            return list.get(0);
        }
        return null;
    }

    public Metadata queryMetaData(String  metaGroup,  String metaCode ){
        return metaDataMapper.queryMetaData(metaGroup,metaCode);
    }

}
