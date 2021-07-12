package com.pt.pmq.protocol;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.Data;

import java.io.Serializable;

/**
 * @author nate-pt
 * @date 2021/7/12 10:13
 * @Since 1.8
 * @Description pmq协议数据包对象
 */
@Data
public class PmqPacket implements Serializable {
    private int magicNum;

}
