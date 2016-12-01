/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialtest;
//public class SerialTest implements SerialPortEventListener {
//}
/**
*
* @author Atulmaharaj
*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;
//import static serialtest.TestClass.input;
//import static serialtest.TestClass.output;
public class SerialTest implements SerialPortEventListener {

    public SerialPort serialPort;
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {
    "/dev/tty.usbserial-A9007UX1", // Mac OS X
    "/dev/ttyUSB0", // Linux
    "COM3", // Windows
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
        SerialTest main = new SerialTest();
        main.initialize();
    try
    {
    SerialTest obj = new SerialTest();
    int c=0;
    obj.initialize();
    InputStreamReader Ir = new InputStreamReader(System.in);
    BufferedReader Br = new BufferedReader(Ir);
    while (c!=3)
    {
        System.out.println("LED ON / OFF MENU.");       //debugging
        System.out.println("1.LED ON");                 //debugging
        System.out.println("2.LED OFF");                //debugging
        //AddClient Input Here;
        System.out.print("Enter your choice :");       //debugging
        c = Integer.parseInt(Br.readLine());   //change to pass in client input
        switch(c)
        {
        case 1:
        writeData("1");
        //AddClient Output here
        break;

        case 2:
        writeData("0");
        //AddClient Output here
        break;

        case 3:
            //AddClient Output here, No more connection
            System.exit(0);
        }
    }

    String inputLine=input.readLine();  //read sag from Arduino
    System.out.println(inputLine);
    //System.out.println("Started");
    }
    catch(Exception e){}

    }

}