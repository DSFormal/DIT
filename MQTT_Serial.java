/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialconnection;

/**
 *
 * @author BlessyPearline
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import static serialtest.TestClass.input;
//import static serialtest.TestClass.output;

public class SerialConnection implements SerialPortEventListener {

    public SerialPort serialPort;
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {
    "/dev/tty.usbserial-A9007UX1", // Mac OS X
    "/dev/ttyUSB0", // Linux
    "COM5", // Windows
    };

    public static BufferedReader input;
    public static OutputStream output;
    /** Milliseconds to block while waiting for port open */
    public static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    public static final int DATA_RATE = 9600;

    public void initialize() {
    CommPortIdentifier portId = null;
    Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

    //First, Find an instance of serial port as set in PORT_NAMES.
    while (portEnum.hasMoreElements()) {
        CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
        for (String portName : PORT_NAMES) {
            if (currPortId.getName().equals(portName)) {
                portId = currPortId;
                break;
            }
        }
    }
    if (portId == null) {
        System.out.println("Could not find COM port.");
        return;
    }

    try {
        // open serial port, and use class name for the appName.
        serialPort = (SerialPort) portId.open(this.getClass().getName(),
        TIME_OUT);

        // set port parameters
        serialPort.setSerialPortParams(DATA_RATE,
        SerialPort.DATABITS_8,
        SerialPort.STOPBITS_1,
        SerialPort.PARITY_NONE);

        // open the streams
        input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        output = serialPort.getOutputStream();
        char ch = 1;
        output.write(ch);

        // add event listeners
        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
    } 
    catch (Exception e) {
        System.err.println(e.toString());
    }
    }

    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    public synchronized void serialEvent(SerialPortEvent oEvent) {
    if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
        try {
            String inputLine=input.readLine();
            System.out.println(inputLine);
        } 
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    }
    public static synchronized void writeData(String data) {
        System.out.println("Sent: " + data);
        try {
            output.write(data.getBytes());
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }

    public static void main(String[] ag)
    {
        SerialConnection main = new SerialConnection();
        main.initialize();
    try
    {
    SerialConnection obj = new SerialConnection();
    int c=0;
    obj.initialize();
    InputStreamReader Ir = new InputStreamReader(System.in);
    BufferedReader Br = new BufferedReader(Ir);
    
    String topic        = "test";
    String content      = "!! Alarm On !!";
    int qos             = 0;
    String broker       = "tcp://m21.cloudmqtt.com:19856";

    //MQTT client id to use for the device. "" will generate a client id automatically
    String clientId     = "ClientId";
    MemoryPersistence persistence = new MemoryPersistence();
   
      MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      connOpts.setUserName("joy");
      connOpts.setPassword(new char[]{'p', '@', 's', 's', 'w','o', 'r', 'd'});
      mqttClient.connect(connOpts);
   
    while (c!=3)
    {
         //AddClient Input Here;
        mqttClient.setCallback(new MqttCallback(){
        @Override
        public void messageArrived(String topic, MqttMessage msg) //This method is called when a message arrives from the server.
                  throws Exception {
                      System.out.println("Recived:" + topic);
                      System.out.println("Recived:" + new String(msg.getPayload()));
                }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {//Called when delivery for a message has been completed, and all acknowledgments have been received.
                    System.out.println("Delivary complete");
                }
        
        @Override
        public void connectionLost(Throwable arg0) {
                    // TODO Auto-generated method stub
                }
        
      });
    
      MqttMessage message = new MqttMessage(content.getBytes());
      message.setQos(qos); 
      System.out.println("Publish message: " + message);
      mqttClient.subscribe(topic, qos);
      mqttClient.publish(topic, message);
      mqttClient.disconnect();
    }
        
    System.exit(0);
    }
    catch(MqttException me) {
      System.out.println("reason "+me.getReasonCode());
      System.out.println("msg "+me.getMessage());
      System.out.println("loc "+me.getLocalizedMessage());
      System.out.println("cause "+me.getCause());
      System.out.println("excep "+me);
      }
    
    catch(Exception e){
    System.out.println("Started");
    }
    
    String inputLine=input.readLine();  //read sag from Arduino
    System.out.println(inputLine);
    
    }
    }
    
