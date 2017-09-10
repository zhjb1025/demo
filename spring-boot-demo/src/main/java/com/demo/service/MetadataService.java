package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.demo.mapper.MetaDataMapper;
import com.demo.mapper.Metadata;

@Service
public class MetadataService {
    @Autowired
    private MetaDataMapper metaDataMapper;

    @Cacheable(value = "metadata", key = "'metadata_'+#metaGroup")
    public List<Metadata> queryMetaDataList(String  metaGroup){
        return metaDataMapper.queryMetaDataList(metaGroup);
    }
    
    @Cacheable(value = "metadata", key = "'metadata_'+#metaGroup") 
    public Metadata queryMetaData(String  metaGroup){
        List<Metadata> list = metaDataMapper.queryMetaDataList(metaGroup);
        if (list!=null){
            return list.get(0);
        }
        return null;
    }
    
    @Cacheable(value = "metadata", key = "'metadata_'+#metaGroup+'_'+#metaCode")  
    public Metadata queryMetaData(String  metaGroup,  String metaCode ){
        return metaDataMapper.queryMetaData(metaGroup,metaCode);
    }

}
