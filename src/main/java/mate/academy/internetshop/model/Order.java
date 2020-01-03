package mate.academy.internetshop.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private Long orderId;
    private BigDecimal amount;
    private Long userId;
    private List<Item> items;

    public Order(List<Item> items, Long userId) {
        this.items = items;
        this.amount = countAmoutn(items);
        this.userId = userId;
    }

    private BigDecimal countAmoutn(List<Item> items) {
        return BigDecimal.valueOf(items.stream()
                .map(i -> i.getPrice())
                .count());
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "Order{"
                + "orderId="
                + orderId
                + ", amount="
                + amount
                + ", userId="
                + userId
                + '}';
    }
}
