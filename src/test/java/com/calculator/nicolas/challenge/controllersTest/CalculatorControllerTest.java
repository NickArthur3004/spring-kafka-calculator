package com.calculator.nicolas.challenge.controllersTest;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.calculator.nicolas.challenge.consumers.CalculatorConsumer;
import com.calculator.nicolas.challenge.controllers.CalculatorController;
import com.calculator.nicolas.challenge.models.OperationMessage;
import static org.hamcrest.Matchers.containsString;
@SpringBootTest
@AutoConfigureMockMvc
public class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private KafkaTemplate<String, OperationMessage> kafkaTemplateMock;
    @Autowired
    private CalculatorConsumer calculatorConsumerMock;

    private CalculatorController calculatorController;

    @BeforeEach
    public void setup() {
        kafkaTemplateMock = mock(KafkaTemplate.class);
        calculatorConsumerMock = mock(CalculatorConsumer.class);
        calculatorController = new CalculatorController(kafkaTemplateMock, calculatorConsumerMock);
    }

    @Test
    public void testCalculateSum() throws Exception {
        String operation = "sum";
        String a = "15";
        String b = "5";

        mockMvc.perform(get("/calculator/" + operation)
            .param("a", a)
            .param("b", b))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Result: 20")));
    }

    @Test
    public void testCalculateSumNotFound() throws Exception {
        String operation = "sum";
        String a = "5";
        String b =  "AHTV";

        mockMvc.perform(get("/calculator/" + operation)
            .param("a", a)
            .param("b", b))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCalculateSub() throws Exception {
        String operation = "sub";
        String a = "30";
        String b = "14.50";

        mockMvc.perform(get("/calculator/" + operation)
            .param("a", a)
            .param("b", b))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Result: 15.50")));
    }

    @Test
    public void testCalculateSubNotFound() throws Exception {
        String operation = "sub";
        String a = "Abril";
        String b = "5";

        mockMvc.perform(get("/calculator/" + operation)
            .param("a", a)
            .param("b", b))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCalculateMul() throws Exception {
        String operation = "mul";
        String a = "9";
        String b = "8";

        mockMvc.perform(get("/calculator/" + operation)
            .param("a", "9")
            .param("b", "8"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Result: 72")));
    }

    @Test
    public void testCalculateMulNotFound() throws Exception {
        String operation = "mul";
        String a = "5";
        String b = "08/12/2025";

        mockMvc.perform(get("/calculator/" + operation)
            .param("a", a)
            .param("b", b))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCalculateDiv() throws Exception {
        String operation = "div";
        String a = "90";
        String b = "3";

        mockMvc.perform(get("/calculator/" + operation)
            .param("a", a)
            .param("b", b))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Result: 30")));
    }

    @Test
    public void testCalculateDivNotFound() throws Exception {
        String operation = "div";
        String a = "5";
        String b = "18:90";

        mockMvc.perform(get("/calculator/" + operation)
            .param("a", a)
            .param("b", b))
            .andExpect(status().isBadRequest());
    }


}
