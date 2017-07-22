package com.demo.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class Config {
  
  @Autowired
  private Environment env;
  
  private static Logger log = LoggerFactory.getLogger(Config.class);  
  
  private Long lastModify_=0l;
  
  private Properties props_ = null;
  
  public  String getConfigByString(String keyName){
      Properties props = null;
      boolean isNeedLoadCfg = false;
      String filePath=env.getProperty("demo.cfg.file","");
      File cfgFile = new File(filePath);
      if ( cfgFile.exists() == false ){
          log.error("demo.cfg.file="+filePath);
          return "";
      }
      if ( props_ == null ){
          isNeedLoadCfg = true ;
      }else{
          Long lastModify = lastModify_;
          if ( lastModify.longValue() != cfgFile.lastModified() )
              isNeedLoadCfg = true;
      }


      if ( isNeedLoadCfg == true ){
          FileInputStream fis = null;
          try {
              fis = new FileInputStream(cfgFile);
              props = new Properties();
              props.load(fis);
              lastModify_=cfgFile.lastModified();
              props_=props;
          }catch (Exception e) {
              log.error("", e);
              return "";
          }finally{
              try{
                  if ( fis != null )
                      fis.close();
              }catch(Exception e){
                log.error("", e);
              }
          }
      }

      return props_.getProperty(keyName, "");
  }
  
  public  int getConfigByInt(String keyName){
    return Integer.parseInt(getConfigByString(keyName));
  }
  
  public  int getConfigByInt(String keyName,int defaultValue){
    return Integer.parseInt(getConfigByString(keyName));
  }
  
  public  int getConfigByInt(String keyName,String defaultValue){
    String temp=getConfigByString(keyName);
    if(temp==null ||temp.trim().length()==0){
      temp=defaultValue;
    }
    return Integer.parseInt(temp);
  }
}
