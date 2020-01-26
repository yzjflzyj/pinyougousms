package com.itheima.listener;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.itheima.utils.SmsUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 消息监听器
 * @description com.itheima.demo.listener
 */
@Component
public class SmsListener implements MessageListenerConcurrently {

    @Autowired
    private SmsUtil smsUtil;

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            //1.循环读取消息-msgs.for
            for (MessageExt msg : msgs) {
                //1.1输出消息-主题、标签、消息key、内容(body)
                String topic = msg.getTopic();  //主题
                String tags = msg.getTags();  //标签
                String keys = msg.getKeys();  //消息key
                //2.获取body内容并转换为map
                String body = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                Map<String,String> messageMap= JSON.parseObject(body,Map.class);
                System.out.println("topic:" + topic + ",tag:" + tags + ",key:" + keys + ",body:" + body);
                //3、调用工具发送消息
                SendSmsResponse response = smsUtil.sendSms(
                        messageMap.get("mobile"),
                        messageMap.get("template_code"),
                        messageMap.get("sign_name"),
                        messageMap.get("param")
                );
                //4、输出短信发送结果
                System.out.println("Code=" + response.getCode());
                System.out.println("Message=" + response.getMessage());
                System.out.println("RequestId=" + response.getRequestId());
                System.out.println("BizId=" + response.getBizId());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //4.2.返回消息读取状态-CONSUME_SUCCESS
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
