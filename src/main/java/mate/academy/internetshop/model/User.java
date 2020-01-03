package mate.academy.internetshop.model;

public class User {
    private String name;
    private Long userId;

    public User(String name) {
        userId = GeneratorId.getNewUserId();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{"
                + "name='"
                + name
                + '\''
                + ", userId="
                + userId
                + '}';
    }
}
