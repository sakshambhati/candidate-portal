package com.portal.candidate.loginfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logging {

    Logger log = LoggerFactory.getLogger(Logging.class);

    @Pointcut(value = "execution(* com.portal.candidate.*.*.*(..) )")
    public void pointCut()
    {

    }

    @Around("pointCut()")
    public Object candidateLogger(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();

        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();
        Object[] array = pjp.getArgs();
        log.info("method invoked " + className + " : " + methodName + "()" +
                "argument : " + mapper.writeValueAsString(array));

        Object object = pjp.proceed();   // catch response
        log.info(className + " : " + methodName + "()" +
                "Response : " + mapper.writeValueAsString(object));
        return object;
    }

}
