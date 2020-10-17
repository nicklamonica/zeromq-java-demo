package asyncReqRes;

public class ParSPICE {

    String requestConn;

    public ParSPICE createEngines(int numWorkers) {
        this.requestConn = "tcp://localhost:4071";
        Proxy proxy = new Proxy(numWorkers, "tcp://localhost:4070", "inproc://workers");
        new Thread(proxy).start();
        return this;
    }

    // TODO make this return a future of the data
    public String doSomething(int something) {
        String response = new Request("tcp://localhost:4070", something).makeRequest();
        return response;
    }
}
