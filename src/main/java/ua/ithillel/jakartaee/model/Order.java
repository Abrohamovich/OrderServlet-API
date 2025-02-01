package ua.ithillel.jakartaee.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Order {
    private Integer id;
    private Date date;
    private double cost;
    private List<Product> products;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
