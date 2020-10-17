package pushpull;

import org.zeromq.*;
import org.zeromq.ZMQ.*;

import java.util.Random;

public class Worker2 {
    public static void main(String[] args) {
        ZContext context = new ZContext();

        Socket workerPull = context.createSocket(SocketType.PULL);
        Socket workerPush = context.createSocket(SocketType.PUSH);

        // INIT connections
        workerPull.connect("tcp://localhost:5560");
        System.out.println("Connected to dispatcher");

        workerPush.connect("tcp://localhost:5561");
        System.out.println("Connected to resolver");


        while (!Thread.currentThread().isInterrupted()) { // keeps going unless canceled

            byte[] request = workerPull.recv(0); //  Wait for next request from dispatcher
            String string = new String(request);

            System.out.println("Worker 2 working on job: ["+ string +"].");

            // simulate doing work
            try {
                Random rand = new Random();
                Thread.sleep(((rand.nextInt(3) + 1) * 500));
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
}
