package ua.ithillel.jakartaee.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.jakartaee.model.Order;
import ua.ithillel.jakartaee.model.Product;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServletTests {
    private OrderServlet orderServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper mapper;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        orderServlet = new OrderServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mapper = new ObjectMapper();
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testDoPost_success() throws Exception {
        Order order = new Order();
        Product product = new Product();
        product.setName("Test Product");
        product.setCost(100.0);
        order.setProducts(List.of(product));

        String jsonOrder = mapper.writeValueAsString(order);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonOrder)));

        orderServlet.doPost(request, response);

        Order createdOrder = mapper.readValue(responseWriter.toString(), Order.class);
        assertEquals(1, createdOrder.getId());
        assertEquals(100.0, createdOrder.getCost());
    }

    @Test
    void testDoGet_returnsAllOrders_EmptyList() throws Exception {
        when(request.getParameter("id")).thenReturn(null);
        orderServlet.doGet(request, response);
        String responseString = responseWriter.toString();
        assertTrue(responseString.contains("[]"));
    }

    @Test
    void testDoGet_returnsResponse_SC_NOT_FOUND() throws Exception {
        when(request.getParameter("id")).thenReturn("0");
        orderServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoGet_returnsResponse_SC_BAD_REQUEST() throws Exception {
        when(request.getParameter("id")).thenReturn("");
        orderServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPut_UpdatesOrder() throws Exception {
        Order order = new Order();
        order.setId(1);
        order.setProducts(List.of(new Product()));

        Field field = OrderServlet.class.getDeclaredField("orderInMemory");
        field.setAccessible(true);
        Map<Integer, Order> orderInMemory = (Map<Integer, Order>) field.get(orderServlet);

        orderInMemory.put(1, order);

        Product product = new Product();
        product.setName("Updated Product");
        product.setCost(50.0);
        order.setProducts(Collections.singletonList(product));

        String jsonOrder = mapper.writeValueAsString(order);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonOrder)));
        orderServlet.doPut(request, response);

        Order updatedOrder = orderInMemory.get(1);
        assertEquals(50.0, updatedOrder.getCost());
    }

    @Test
    void testDoDelete_RemovesOrder() throws Exception {
        Order order = new Order();
        order.setId(1);

        Field field = OrderServlet.class.getDeclaredField("orderInMemory");
        field.setAccessible(true);
        Map<Integer, Order> orderInMemory = (Map<Integer, Order>) field.get(orderServlet);

        orderInMemory.put(1, order);

        when(request.getParameter("id")).thenReturn("1");
        orderServlet.doDelete(request, response);

        assertFalse(orderInMemory.containsKey(1));
    }
}
