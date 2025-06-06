package com.calculator.nicolas.challenge.controllers;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calculator.nicolas.challenge.consumers.CalculatorConsumer;
import com.calculator.nicolas.challenge.models.OperationMessage;

@RestController
@RequestMapping(value = "/calculator")
public class CalculatorController {

    @Autowired
    private KafkaTemplate<String, OperationMessage> kafkaTemplate;
    private final CalculatorConsumer calculatorConsumer;

    public CalculatorController(KafkaTemplate<String, OperationMessage> kafkaTemplate,
                                CalculatorConsumer calculatorConsumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.calculatorConsumer = calculatorConsumer;
    }

    @GetMapping(value = "/{operation}")
    public ResponseEntity<String> calculate(@PathVariable String operation, 
                                           @RequestParam BigDecimal a,
                                           @RequestParam BigDecimal b){
        
        String requestId = UUID.randomUUID().toString();
        OperationMessage message = new OperationMessage(requestId, operation, a, b);

        returnKafkaDelay();
        kafkaTemplate.send("operations", message);
        returnKafkaDelay();
        
        BigDecimal result = calculatorConsumer.getResult(requestId);
        return ResponseEntity.ok("Result: " + result.toString());

    }

    public void returnKafkaDelay(){
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
