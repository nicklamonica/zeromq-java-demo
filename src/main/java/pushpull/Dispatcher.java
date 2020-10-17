package pushpull;

import org.zeromq.*;
import org.zeromq.ZMQ.*;

public class Dispatcher {

    Socket dispatcher;

    public void createEngines(int numEngines) {
        ZContext context = new ZContext(1);
        this.dispatcher = context.createSocket(SocketType.PUSH);

        // create socket to connect to workers
        dispatcher.bind("tcp://localhost:5560");
        System.out.println("launch and connect client.");


        for (int jobNumber = 0; jobNumber < 100; jobNumber++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Sending Job: " + jobNumber);
            dispatcher.send("Job " + jobNumber, 0);
        }
        dispatcher.close();
        context.destroy();
    }
}
