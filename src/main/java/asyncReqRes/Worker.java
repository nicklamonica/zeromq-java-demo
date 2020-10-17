package asyncReqRes;

import org.zeromq.*;
import org.zeromq.ZMQ.*;

import java.util.Random;

public class Worker implements Runnable {

    public final int workerNum;
    public ZContext context;
    public String connection;

    public Worker(ZContext context, int workerId, String workerConnection) {
        this.workerNum = workerId;
        this.context = context;
        this.connection = workerConnection;
    }

    public void run() {
        Socket socket = this.context.createSocket(SocketType.REP);
        socket.connect(this.connection);

        while (!Thread.currentThread().isInterrupted()) {

            byte[] msg = socket.recv();
            String job = new String(msg);

            System.out.println("Worker " + this.workerNum + " is working on job: ["+ job +"].");

            // simulate doing work
            this.doWork();
            //  send completed work to requester
            String response = "Job: " + job + " is complete!";
            socket.send(response.getBytes(), 0);
        }

        //  clean up
        socket.close();
        context.close();
    }

    public void doWork() {
        try {
            Random rand = new Random();
            Thread.sleep((long) ((rand.nextInt(3) + 0.5) * 500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
