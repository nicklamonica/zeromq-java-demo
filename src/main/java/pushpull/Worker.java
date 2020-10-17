package pushpull;

import org.zeromq.*;
import org.zeromq.ZMQ.*;

import java.util.Random;

public class Worker implements Runnable{

    Socket workerPull;
    Socket workerPush;
    ZContext context;

    public Worker(String requestorUrl, String resolverUrl) {
        this.context = new ZContext();

        this.workerPull = context.createSocket(SocketType.PULL);
        this.workerPush = context.createSocket(SocketType.PUSH);

        // INIT connections
        workerPull.connect("tcp://localhost:5560");
        System.out.println("Connected to dispatcher");

        workerPush.connect("tcp://localhost:5561");
        System.out.println("Connect to resolver");


        while (!Thread.currentThread().isInterrupted()) { // keeps going unless canceled

            byte[] request = workerPull.recv(0); //  Wait for next request from dispatcher
            String string = new String(request);

            System.out.println("Worker 1 working on job: ["+ string +"].");

            // simulate doing work
            try {
                Random rand = new Random();
                Thread.sleep((long) ((rand.nextInt(3) + 0.5) * 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //  send complete work to resolver
            workerPush.send(string.getBytes(), 0);
        }

        //  clean up
        workerPull.close();
        workerPush.close();
        context.close();
    }

    public void run() {

    }
}
