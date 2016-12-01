/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqttsample;

/**
 *
 * @author Admin
 */
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

public class MQTTSample{
  public static void main(String[] args) {
    String topic        = "hello";
    String content      = "hi there";
    int qos             = 0;
    String broker       = "tcp://m20.cloudmqtt.com:17023";

    //MQTT client id to use for the device. "" will generate a client id automatically
    String clientId     = "ClientId";
    MemoryPersistence persistence = new MemoryPersistence();
    for(;;){
    try{
      /*  MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
               // MqttConnectOptions connOpts = new MqttConnectOptions();
               // connOpts.setCleanSession(true);
                 MqttConnectOptions connOpts = new MqttConnectOptions();
                    connOpts.setCleanSession(true);
                    connOpts.setUserName("testing");
                    connOpts.setPassword(new char[]{'p', 'a', 's', 's', 'w','o', 'r', 'd'});
                    // sampleClient.connect(connOpts);
                    //MqttMessage message = new MqttMessage(content.getBytes());
                   // message.setQos(qos); 
                   // System.out.println("Publish message: " + message);
                   // sampleClient.subscribe(topic, qos);
                   // sampleClient.disconnect();
                System.out.println("Connecting to broker: "+broker);
                sampleClient.connect(connOpts);
                System.out.println("Connected");
                System.out.println("Publishing message: "+content);
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos);
                sampleClient.publish(topic, message);
                System.out.println("Message published");
                sampleClient.disconnect();
                System.out.println("Disconnected");
                System.exit(0);*/
      MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
       MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      connOpts.setUserName("testing");
      connOpts.setPassword(new char[]{'p', 'a', 's', 's', 'w','o', 'r', 'd'});
      mqttClient.connect(connOpts);

      mqttClient.setCallback(new MqttCallback(){
        @Override
        public void messageArrived(String topic, MqttMessage msg)
                  throws Exception {
                      System.out.println("Recived:" + topic);
                      System.out.println("Recived:" + new String(msg.getPayload()));
                      MqttMessage message = new MqttMessage(content.getBytes());
      message.setQos(qos); 
      System.out.println("Publish message: " + message);
      mqttClient.subscribe(topic, qos);
      mqttClient.publish(topic, message);
      mqttClient.disconnect();
                }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
                    System.out.println("Delivary complete");
                }

        @Override
        public void connectionLost(Throwable arg0) {
                    // TODO Auto-generated method stub
                }
        
      });
      
      /*MqttMessage message = new MqttMessage(content.getBytes());
      message.setQos(qos); 
      System.out.println("Publish message: " + message);
      mqttClient.subscribe(topic, qos);
      mqttClient.publish(topic, message);
      mqttClient.disconnect();
    */
      //System.exit(0);
    }catch(MqttException me) {
      System.out.println("reason "+me.getReasonCode());
      System.out.println("msg "+me.getMessage());
      System.out.println("loc "+me.getLocalizedMessage());
      System.out.println("cause "+me.getCause());
      System.out.println("excep "+me);
     // me.printStackTrace();
    }
    }
  }
}
