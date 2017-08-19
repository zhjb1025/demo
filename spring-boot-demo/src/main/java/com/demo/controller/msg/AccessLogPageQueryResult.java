package com.demo.controller.msg;

import java.util.Date;

import com.demo.common.enums.TradeStatusEnum;
import com.demo.common.util.CommUtil;

public class AccessLogPageQueryResult extends AccessLog {

    public String getTradeStatusLabel() {
        return TradeStatusEnum.getLabel(this.getTradeStatus());
    }
    public String getStartTimestampLabel() {
        Date date = new Date(this.getStartTimestamp());
        return CommUtil.dateFormat(date,"yyyy-MM-dd HH:mm:ss");
    }
}
