package pushpull;

import org.zeromq.ZMQ.*;
import org.zeromq.*;

public class Resolver implements Runnable {
    Socket socket;
    ZContext context;

    public Resolver(String portNum) {
        this.context = new ZContext();
        this.socket = context.createSocket(SocketType.PULL);
        this.socket.bind("tcp://localhost:5561"); // TODO replace with port num

        System.out.println("Connected to socket to receive data");

        //  clean up;
//        socket.close();
//        context.close();
    }

    public void run() {
        //  Wait for next completed job
        byte[] request = socket.recv(0);
        String string = new String(request);

        System.out.println("Received Completed Job: ["+string+"].");
    }

}
