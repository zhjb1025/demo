package com.demo.eoms.controller.msg;

import java.util.Date;

import com.demo.framework.dubbo.AccessLog;
import com.demo.framework.enums.TradeStatusEnum;
import com.demo.framework.util.CommUtil;

public class AccessLogPageQueryResult extends AccessLog {

    public String getTradeStatusLabel() {
        return TradeStatusEnum.getLabel(Integer.valueOf(this.getTradeStatus()));
    }
    public String getStartTimestampLabel() {
        Date date = new Date(this.getStartTimestamp());
        return CommUtil.dateFormat(date,"yyyy-MM-dd HH:mm:ss");
    }
}
