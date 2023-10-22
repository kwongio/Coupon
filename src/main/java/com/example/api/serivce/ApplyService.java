package com.example.api.serivce;

import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.CouponCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponeCreateProducer;

    public void apply(Long userId) {
        Long count = couponCountRepository.increment();
        if (count > 100) {
            return;
        }
        couponeCreateProducer.create(userId);

    }
}
