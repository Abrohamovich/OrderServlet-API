package ua.ithillel.jakartaee.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.ithillel.jakartaee.model.Order;
import ua.ithillel.jakartaee.model.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServlet extends HttpServlet {
    private final Map<Integer, Order> orderInMemory = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int orderProductsCount = 1;
        double totalCost = 0;
        int orderCounter = orderInMemory.size() + 1;
        for (Order order : orderInMemory.values()) {
            orderProductsCount += order.getProducts().size();
        }
        Order order = mapper.readValue(req.getReader(), Order.class);
        order.setId((orderCounter++));
        List<Product> orderProducts = order.getProducts();

        for (Product product : orderProducts) {
            product.setId(orderProductsCount++);
            totalCost += product.getCost();
        }
        order.setCost(totalCost);
        orderInMemory.put(order.getId(), order);
        resp.getWriter().write(mapper.writeValueAsString(order));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderId = req.getParameter("id");
        if (orderId == null) {
            resp.getWriter().write(mapper.writeValueAsString(orderInMemory.values()));
        } else if (orderId.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            int id = Integer.parseInt(orderId);
            Order order = orderInMemory.get(id);
            if (order == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            resp.getWriter().write(mapper.writeValueAsString(order));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int orderProductsCount = 1;
        double totalCost = 0;
        for (Order order : orderInMemory.values()) {
            orderProductsCount += order.getProducts().size();
        }
        Order order = mapper.readValue(req.getReader(), Order.class);
        if(order.getId() == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (!orderInMemory.containsKey(order.getId())) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            Order orderDB = orderInMemory.get(order.getId());
            for (Product product : order.getProducts()) {
                product.setId(orderProductsCount++);
                totalCost += product.getCost();
            }
            orderDB.setProducts(order.getProducts());
            orderDB.setCost(totalCost);
            resp.getWriter().write(mapper.writeValueAsString(orderDB));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String orderId = req.getParameter("id");
        if (orderId == null || orderId.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            int id = Integer.parseInt(orderId);
            if (!orderInMemory.containsKey(id)) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            orderInMemory.remove(id);
        }
    }
}
