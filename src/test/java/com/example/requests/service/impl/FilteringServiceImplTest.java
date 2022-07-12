package com.example.requests.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FilteringServiceImplTest {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final int numOfThreads = 10;
    private final CountDownLatch latch = new CountDownLatch(numOfThreads);

    private final int amount = 2;
    private final int minutes = 1;

    private RequestFilteringServiceImpl service = new RequestFilteringServiceImpl(amount, minutes);

    @Test
    void allowRequestTest() throws InterruptedException {
        String ipAddress1 = "127.0.0.1";
        String ipAddress2 = "198.54.3.2";
        List<String> ipAddresses = Arrays.asList(ipAddress1, ipAddress2, ipAddress1, ipAddress1, ipAddress2,
                ipAddress1, ipAddress2, ipAddress1, ipAddress1, ipAddress2);

        List<Future<?>> futures = new ArrayList<>();
        List<Boolean> accessListIp1 = new ArrayList<>();
        List<Boolean> accessListIp2 = new ArrayList<>();

        //when
        for (int i = 0; i < numOfThreads; i++) {
            int index = i;
            futures.add(executorService.submit(
                    () -> {
                        latch.countDown();
                        if (ipAddresses.get(index).equals(ipAddress1)) {
                            accessListIp1.add(service.allowRequest(ipAddresses.get(index)));
                        } else {
                            accessListIp2.add(service.allowRequest(ipAddresses.get(index)));
                        }
                    }
            ));

        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        //then
        if (executorService.isTerminated()) {
            Assertions.assertEquals(6, accessListIp1.size());
            Assertions.assertEquals(2, accessListIp1.stream().filter(a -> a).count());
            Assertions.assertEquals(4, accessListIp1.stream().filter(a -> !a).count());
            Assertions.assertTrue(accessListIp1.get(0));
            Assertions.assertTrue(accessListIp1.get(1));
            Assertions.assertFalse(accessListIp1.get(2));

            Assertions.assertEquals(4, accessListIp2.size());
            Assertions.assertEquals(2, accessListIp2.stream().filter(a -> a).count());
            Assertions.assertEquals(2, accessListIp2.stream().filter(a -> !a).count());
            Assertions.assertTrue(accessListIp2.get(0));
            Assertions.assertTrue(accessListIp2.get(1));
            Assertions.assertFalse(accessListIp2.get(2));
        } else {
            executorService.shutdownNow();
            Assertions.fail("ExecutorService was not terminated");
        }
    }
}
