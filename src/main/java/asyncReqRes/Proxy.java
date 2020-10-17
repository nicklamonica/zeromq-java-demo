package asyncReqRes;

import org.zeromq.*;
import org.zeromq.ZMQ.*;

public class Proxy implements Runnable {

    public int numWorkers;
    public String workerConn;
    public String reqConn;

    public Proxy(int numWorkers, String requesterConnection, String workerConnection) {
        this.numWorkers = numWorkers;
        this.reqConn = requesterConnection;
        this.workerConn = workerConnection;
    }

    public void run()
    {
        try {
            ZContext context = new ZContext();

            // socket to get requests from
            Socket requesterConn = context.createSocket(SocketType.ROUTER);
            requesterConn.bind(this.reqConn);

            //  socket to connect to workers
            Socket workerConn = context.createSocket(SocketType.DEALER);
            workerConn.bind(this.workerConn);

            // create a pool of workers that will actually do the work
            for (int engine = 1; engine <= this.numWorkers; engine++) {
                new Thread(new Worker(context, engine, this.workerConn)).start();
            }
            //  Connect the requester to the workers using proxy
            ZMQ.proxy(requesterConn, workerConn, null);
            context.destroy();
        } catch (ZMQException e) {
            System.out.println(e.getMessage());
        }
    }
}
