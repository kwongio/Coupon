package com.example.api;

import com.example.api.repository.CouponRepository;
import com.example.api.serivce.ApplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApiApplicationTests {

    @Autowired
    private ApplyService applyService;
    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 한번만응모() {
        applyService.apply(1L);
        long count = couponRepository.count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void 여려명응모() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);

                } finally {
                    latch.countDown();
                }
            });

        }
        latch.await();
        Thread.sleep(10000);

        long count = couponRepository.count();
        assertThat(count).isEqualTo(100);
        System.out.println("count = " + count);
    }


    @Test
    public void 한명당_한개의쿠폰만_발급() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    applyService.apply(1L);

                } finally {
                    latch.countDown();
                }
            });

        }
        latch.await();
        Thread.sleep(10000);

        long count = couponRepository.count();
        assertThat(count).isEqualTo(1L);
        System.out.println("count = " + count);
    }

}
