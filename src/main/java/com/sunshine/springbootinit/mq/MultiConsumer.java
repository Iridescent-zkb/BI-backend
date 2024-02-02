package com.sunshine.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class MultiConsumer {
  // 声明队列名称为"multi_queue"
  private static final String TASK_QUEUE_NAME = "multi_queue";

  public static void main(String[] argv) throws Exception {
    // 创建一个新的连接工厂
    ConnectionFactory factory = new ConnectionFactory();
    // 设置连接工厂的主机地址
    factory.setHost("localhost");
    // 从工厂获取一个新的连接
    final Connection connection = factory.newConnection();
      for (int i = 0; i < 2; i++) {
          final Channel channel = connection.createChannel();

          channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
          System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
          // 设置预取计数为 1，这样RabbitMQ就会在给消费者新消息之前等待先前的消息被确认
          channel.basicQos(1);

          // 定义了如何处理消息
          int finalI = i;
          DeliverCallback deliverCallback = (consumerTag, delivery) -> {
              String message = new String(delivery.getBody(), "UTF-8");

              try {
                  // 处理工作
                  System.out.println(" [x] Received '" + "编号:" + finalI + ":" + message + "'");
                  // 发送确认消息，确认消息已被处理
                  channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                  // 停 20 秒，模拟机器处理能力有限
                  Thread.sleep(20000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
                  // 发生异常后，拒绝确认消息，发送拒绝消息，并不重新投递该消息
                  channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
              } finally {
                  System.out.println(" [x] Done");
                  channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
              }
          };
          channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
      }
  }
}