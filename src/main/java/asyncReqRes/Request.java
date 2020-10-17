package asyncReqRes;

import org.zeromq.*;
import org.zeromq.ZMQ.*;

import java.util.Random;

public class Request {

    public String connection;
    private static Random rand = new Random(System.nanoTime());
    int jobToSend;
    String response;

    public Request(String connection, int job){

        this.connection = connection;
        this.jobToSend = job;
    }

    // figure out how to batch requests together
    private void batch(String anotherRequest) {}

    public String makeRequest()
    {
        try  {
            ZContext context = new ZContext();
            Socket requester = context.createSocket(SocketType.REQ);

            // this requests need an identity for some reason???
            String identity = String.format(
                    "%04X-%04X", rand.nextInt(), rand.nextInt()
            );
            requester.setIdentity(identity.getBytes(ZMQ.CHARSET));
            requester.connect(this.connection);

            ZPoller poller = new ZPoller(context);
            poller.register(requester, Poller.POLLIN);

            // send request
            requester.send(String.format("job #%d", jobToSend), 0);

            // poll
            poller.poll(1000);
            if (poller.isReadable(requester)) { // read socket if messages waiting
                // get message and print
                ZMsg msg = ZMsg.recvMsg(requester);
                msg.getLast().print(identity);
                this.response = msg.popString();
                msg.destroy();
            }
        } catch (ZMQException e ) {
            System.out.println(e.getMessage());
            this.response = "";
        }
        return this.response;
    }
}
