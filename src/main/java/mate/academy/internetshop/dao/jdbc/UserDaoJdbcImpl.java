package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Role;
import mate.academy.internetshop.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Dao
public class UserDaoJdbcImpl extends AbstractDao<User> implements UserDao {
    private static final Logger LOGGER = LogManager.getLogger(ItemDaoJdbcImpl.class);
    private static String USER_TABLE = "users";
    private static String ROLES_TABLE = "roles";
    private static String USER_ROLES_TABLE = "user_roles";
    private static String BUCKETS_TABLE = "bucket";
    private static String ORDERS_TABLE = "orders";

    @Inject
    private static BucketDao bucketDao;

    @Inject
    private static OrderDao orderDao;

    public UserDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Optional<User> login(String login) {
        String query = String.format("SELECT users.id AS userId, name, password, token, "
                        + "roles.role_name AS role, roles.id AS idRole FROM %s INNER JOIN %s "
                        + "ON users.id=user_roles.user_id "
                        + "INNER JOIN %s "
                        + "ON user_roles.role_id = roles.id WHERE users.name=?",
                USER_TABLE, USER_ROLES_TABLE, ROLES_TABLE);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            while (rs.next()) {
                user = new User(rs.getString("name"), rs.getString("password"));
                user.setToken(rs.getString("token"));
                user.setUserId(rs.getLong("userId"));
                Role role = new Role(Role.RoleName.valueOf(rs.getString("role")));
                role.setId(rs.getLong("idRole"));
                user.setRole(role);
            }
            return Optional.of(user);
        } catch (SQLException ex) {
            LOGGER.warn("Can't login", ex);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByToken(String token) {
        String query = String.format("SELECT users.id AS userId, name, password, token, "
                        + "roles.role_name AS role, roles.id AS idRole FROM %s INNER JOIN %s "
                        + "ON users.id=user_roles.user_id "
                        + "INNER JOIN %s "
                        + "ON user_roles.role_id = roles.id WHERE users.token=?",
                USER_TABLE, USER_ROLES_TABLE, ROLES_TABLE);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            while (rs.next()) {
                user = new User(rs.getString("name"), rs.getString("password"));
                user.setToken(rs.getString("token"));
                user.setUserId(rs.getLong("userId"));
                Role role = new Role(Role.RoleName.valueOf(rs.getString("role")));
                role.setId(rs.getLong("idRole"));
                user.setRole(role);
            }
            return Optional.of(user);
        } catch (SQLException ex) {
            LOGGER.warn("Can't get user bu token", ex);
        }
        return Optional.empty();
    }

    @Override
    public User create(User entity) {
        User user = entity;
        String query = String.format("INSERT INTO %s(name, password, token) VALUE(?, ?, ?)",
                USER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getToken());
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            while (resultSet.next()) {
                user.setUserId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't create user", e);
        }

        Long roleId = null;
        for (Role role : user.getRoles()) {
            query = String.format("SELECT id FROM %s WHERE role_name='%s'",
                    ROLES_TABLE, role.getRoleName().toString());
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    roleId = rs.getLong("id");
                    role.setId(roleId);
                }
            } catch (SQLException e) {
                LOGGER.warn("Can't get role id" + entity.toString(), e);
            }

            query = String.format("INSERT INTO %s(user_id, role_id) VALUES(%d, %d)",
                    USER_ROLES_TABLE, user.getUserId(), roleId);
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                LOGGER.warn("Can't get role id" + entity.toString(), e);

            }
        }
        return user;
    }

    @Override
    public Optional<User> get(Long userId) {
        String query = String.format("SELECT users.id AS userId, name, password, token, "
                        + "roles.role_name AS role, roles.id AS idRole FROM %s INNER JOIN %s "
                        + "ON users.id=user_roles.user_id "
                        + "INNER JOIN %s "
                        + "ON user_roles.role_id = roles.id WHERE users.id=%d",
                USER_TABLE, USER_ROLES_TABLE, ROLES_TABLE, userId);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery(query);
            User user = null;
            while (rs.next()) {
                user = new User(rs.getString("name"), rs.getString("password"));
                user.setToken(rs.getString("token"));
                user.setUserId(rs.getLong("userId"));
                Role role = new Role(Role.RoleName.valueOf(rs.getString("role")));
                role.setId(rs.getLong("idRole"));
                user.setRole(role);
            }
            return Optional.of(user);
        } catch (SQLException e) {
            LOGGER.warn("Can't get user", e);
        }
        return Optional.empty();
    }

    @Override
    public User update(User user) {
        User newUser = user;
        String query = String.format("UPDATE %s(name, password, token) VALUE(?, ?, ?)",
                USER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getToken());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Can't update user", e);
        }

        query = String.format("DELETE FROM %s WHERE user_id=%d",
                USER_ROLES_TABLE, newUser.getUserId());
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.warn("Can't delete roles", e);
        }

        Long roleId = 1L;
        for (Role role : user.getRoles()) {
            query = String.format("SELECT id FROM %s WHERE role_name='%s'",
                    ROLES_TABLE, role.getRoleName().toString());
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    roleId = rs.getLong("id");
                }
            } catch (SQLException e) {
                LOGGER.warn("Can't get role id" + user.toString(), e);
            }

            query = String.format("INSERT INTO %s(user_id, role_id) VALUES(%d, %d)",
                    USER_ROLES_TABLE, newUser.getUserId(), roleId);
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                LOGGER.warn("Can't get role id" + user.toString(), e);

            }
        }
        return newUser;
    }

    @Override
    public boolean deleteById(Long userId) {
        String query = String.format("SELECT order_id FROM %s WHERE user_id=%d",
                ORDERS_TABLE, userId);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery(query);
            List<Long> listUserOrdersId = new ArrayList<>();
            while (rs.next()) {
                listUserOrdersId.add(rs.getLong(1));
            }
            for (Long orderId : listUserOrdersId) {
                orderDao.deleteById(orderId);
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't clean User orders", e);
        }

        query = String.format("SELECT bucket_id FROM %s WHERE user_id=%d",
                BUCKETS_TABLE, userId);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery(query);
            List<Long> listUserBucketsId = new ArrayList<>();
            while (rs.next()) {
                listUserBucketsId.add(rs.getLong(1));
            }
            for (Long bucketId : listUserBucketsId) {
                bucketDao.deleteById(bucketId);
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't clean User orders", e);
        }

        query = String.format("DELETE FROM %s WHERE user_id=%d",
                USER_ROLES_TABLE, userId);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.warn("Can't delete roles for user", e);
        }

        query = String.format("DELETE FROM %s WHERE id=%d",
                USER_TABLE, userId);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Can't delete user by ID", e);
        }
        return false;
    }

    @Override
    public boolean delete(User entity) {
        deleteById(entity.getUserId());
        return true;
    }

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        String query = String.format("SELECT * FROM %s",
                USER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                User user = new User(name, password);
                user.setUserId(id);
                list.add(user);
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't get all users", e);
        }
        return list;
    }
}
