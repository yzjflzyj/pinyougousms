package com.itheima.conf;

import com.itheima.listener.SmsListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消费者配置信息
 * @author Steven
 * @description com.itheima.conf
 * @Configuration 相当于spring配置文件中的<beans></beans>
 */
@Configuration
public class ConsumerConfiguration {

    @Autowired
    private SmsListener smsListener;

    /**
     * 配置消费者
     * @Bean 相当于spring配置文件中的<bean>
     */
    @Bean
    public DefaultMQPushConsumer getPushConsumer(){
        DefaultMQPushConsumer consumer = null;
        try {
            //创建消费者
            consumer = new DefaultMQPushConsumer("sms-consumer-group");
            //设置NameServer地址
            consumer.setNamesrvAddr("127.0.0.1:9876");
            //订阅消息
            consumer.subscribe("topic-sms","*");
            //设置消费者的消费接收模式为广播，默认为集群模式
            //consumer.setMessageModel(MessageModel.BROADCASTING);
            //设置监听器
            consumer.setMessageListener(smsListener);
            //启动消费者
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return consumer;
    }
}
