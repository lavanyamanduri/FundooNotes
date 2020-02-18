package com.bridgelabz.fundoonotes.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class ControllerAop {

	@Around("execution(* com.bridgelabz.fundoonotes.controller.*.*(*))")
	public Object commonMethod(ProceedingJoinPoint JointPoint) throws Throwable {
		log.info("Before " + JointPoint.getSignature().getName() + " method of "
				+ JointPoint.getSignature().getDeclaringType().getSimpleName());
		Object returning = JointPoint.proceed();
		log.info("After " + JointPoint.getSignature().getName() + "  method of class"
				+ JointPoint.getSignature().getDeclaringType().getSimpleName());
		return returning;
	}
}
