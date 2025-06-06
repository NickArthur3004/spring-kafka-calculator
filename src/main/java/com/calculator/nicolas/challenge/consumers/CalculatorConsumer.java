package com.calculator.nicolas.challenge.consumers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.calculator.nicolas.challenge.exceptions.DivisionByZeroException;
import com.calculator.nicolas.challenge.models.OperationMessage;

@Component
public class CalculatorConsumer {

    private final ConcurrentMap<String, BigDecimal> results = new ConcurrentHashMap<>();

    public BigDecimal getResult(String requestId) {
        return results.get(requestId);
    }
    
@KafkaListener(topics = "operations", groupId = "group_id")
public void consume(OperationMessage message){
    System.out.println("Received: " + message.getOperation() + " a=" + message.getA() + " and b=" + message.getB());

    BigDecimal result = null;
    
    switch (message.getOperation().toLowerCase()) {
        case "sum":
            result = message.getA().add(message.getB());
            break;

        case "sub":
            result = message.getA().subtract(message.getB());
            break;
        
        case "mul":
            result = message.getA().multiply(message.getB());
            break;
    
        case "div":
            if(message.getB().compareTo(BigDecimal.ZERO) == 0){
                throw new DivisionByZeroException("Error: division by zero.");
            }
            result = message.getA().divide(message.getB(), MathContext.DECIMAL128);
            break;

        default:
            throw new RuntimeException("Invalid operation: " + message.getOperation());
    }

    System.out.println("Operation Result [" + message.getOperation()+ "]: " + result);
     results.put(message.getRequestId(), result);
}

}
