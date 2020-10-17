import org.junit.*;

import asyncReqRes.ParSPICE;


public class AsyncTest {

    @Test
    public void asynctest() {
        ParSPICE parSpice = new ParSPICE().createEngines(4);

        for (int i = 0; i < 10; i++) {
            parSpice.doSomething(i);
        }
    }
}
