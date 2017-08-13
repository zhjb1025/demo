package com.demo.controller.msg;

import com.demo.common.enums.TradeStatusEnum;
import com.demo.common.msg.BaseObject;
import com.demo.common.util.CommUtil;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

public class AccessLogPageQueryResult extends AccessLog {

    public String getTradeStatusLabel() {
        return TradeStatusEnum.getLabel(this.getTradeStatus());
    }
    public String getStartTimestampLabel() {
        Date date = new Date(this.getStartTimestamp());
        return CommUtil.dateFormat(date,"yyyy-MM-dd HH:mm:ss");
    }
}
