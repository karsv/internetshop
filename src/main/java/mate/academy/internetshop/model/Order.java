package mate.academy.internetshop.model;

import mate.academy.internetshop.lib.GeneratorId;

import java.math.BigDecimal;

public class Order {
    private Long orderId;
    private BigDecimal amount;
    private Long userId;

    public Order(BigDecimal amount, Long userId) {
        orderId = GeneratorId.getNewOrderId();
        this.amount = amount;
        this.userId = userId;
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
