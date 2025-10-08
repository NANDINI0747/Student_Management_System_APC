package com.example.studentservice.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class loggingAspect {

    @Before("execution(* com.example.studentservice.service.StudentService.*(..))")
    public void logBefore() {
        System.out.println("StudentService method is called");
    }
}
