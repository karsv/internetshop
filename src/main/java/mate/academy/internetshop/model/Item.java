package mate.academy.internetshop.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Item {
    private Long itemId;
    private String name;
    private BigDecimal price;

    public Item(String name, double price) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        itemId = GeneratorId.getNewItemId();
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(itemId, item.itemId)
                && Objects.equals(name, item.name)
                && Objects.equals(price, item.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, name, price);
    }

    @Override
    public String toString() {
        return "Item{"
                + "itemId="
                + itemId
                + ", name='"
                + name
                + '\''
                + ", price="
                + price
                + '}';
    }
}
