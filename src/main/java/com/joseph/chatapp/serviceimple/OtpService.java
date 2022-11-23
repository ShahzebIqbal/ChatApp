package com.joseph.chatapp.serviceimple;


import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

@Service
public class OtpService {


    public OtpService(){
        super();
    }

    public int generateOTP(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return otp;
    }
}

