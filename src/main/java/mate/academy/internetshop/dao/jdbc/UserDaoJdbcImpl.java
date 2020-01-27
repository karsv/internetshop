package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Role;
import mate.academy.internetshop.model.User;

@Dao
public class UserDaoJdbcImpl extends AbstractDao<User> implements UserDao {
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
    public Optional<User> login(String login) throws DataProcessingException {
        String query = String.format("SELECT users.id AS userId, name, password, token, "
                        + "roles.role_name AS role, roles.id AS idRole FROM %s INNER JOIN %s "
                        + "ON users.id=user_roles.user_id "
                        + "INNER JOIN %s "
                        + "ON user_roles.role_id = roles.id WHERE users.name=?",
                USER_TABLE, USER_ROLES_TABLE, ROLES_TABLE);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            User user = getUserFromResultSet(rs);
            return Optional.of(user);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't login user", e);
        }
    }

    @Override
    public Optional<User> findByToken(String token) throws DataProcessingException {
        String query = String.format("SELECT users.id AS userId, name, password, token, "
                        + "roles.role_name AS role, roles.id AS idRole FROM %s INNER JOIN %s "
                        + "ON users.id=user_roles.user_id "
                        + "INNER JOIN %s "
                        + "ON user_roles.role_id = roles.id WHERE users.token=?",
                USER_TABLE, USER_ROLES_TABLE, ROLES_TABLE);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            User user = getUserFromResultSet(rs);
            return Optional.of(user);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find user by token", e);
        }
    }

    private User getUserFromResultSet(ResultSet rs) throws SQLException {
        User user = null;
        while (rs.next()) {
            user = new User(rs.getString("name"), rs.getString("password"));
            user.setToken(rs.getString("token"));
            user.setUserId(rs.getLong("userId"));
            Role role = new Role(Role.RoleName.valueOf(rs.getString("role")));
            role.setId(rs.getLong("idRole"));
            user.setRole(role);
        }
        return user;
    }

    @Override
    public User create(User entity) throws DataProcessingException {
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
            throw new DataProcessingException("Can't create new user", e);
        }

        user.setRoles(setUserRoles(user));
        return user;
    }

    private Set<Role> setUserRoles(User user) throws DataProcessingException {
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            String query = String.format("SELECT id FROM %s WHERE role_name=?",
                    ROLES_TABLE);
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, role.getRoleName().toString());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    role.setId(rs.getLong("id"));
                }
            } catch (SQLException e) {
                throw new DataProcessingException("Can't get roles id", e);
            }

            query = String.format("INSERT INTO %s(user_id, role_id) VALUES(?, ?)",
                    USER_ROLES_TABLE, user.getUserId(), role.getId());
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, user.getUserId());
                stmt.setLong(2, role.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Can't insert roles for new user", e);
            }
        }
        return roles;
    }

    @Override
    public Optional<User> get(Long userId) throws DataProcessingException {
        String query = String.format("SELECT users.id AS userId, name, password, token, "
                        + "roles.role_name AS role, roles.id AS idRole FROM %s INNER JOIN %s "
                        + "ON users.id=user_roles.user_id "
                        + "INNER JOIN %s "
                        + "ON user_roles.role_id = roles.id WHERE users.id=?",
                USER_TABLE, USER_ROLES_TABLE, ROLES_TABLE);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
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
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get user by id", e);
        }
    }

    @Override
    public User update(User user) throws DataProcessingException {
        User newUser = user;
        String query = String.format("UPDATE %s(name, password, token) VALUE(?, ?, ?)",
                USER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getToken());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update user", e);
        }

        query = String.format("DELETE FROM %s WHERE user_id=?",
                USER_ROLES_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, newUser.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete users role due update", e);
        }

        newUser.setRoles(setUserRoles(newUser));
        return newUser;
    }

    @Override
    public boolean deleteById(Long userId) throws DataProcessingException {
        String query = String.format("SELECT order_id FROM %s WHERE user_id=?",
                ORDERS_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            List<Long> listUserOrdersId = new ArrayList<>();
            while (rs.next()) {
                listUserOrdersId.add(rs.getLong(1));
            }
            for (Long orderId : listUserOrdersId) {
                orderDao.deleteById(orderId);
            }
        } catch (SQLException e) {
            throw new DataProcessingException(
                    "Can't delete user orders by userId due delete user", e);
        }

        query = String.format("SELECT bucket_id FROM %s WHERE user_id=?",
                BUCKETS_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            List<Long> listUserBucketsId = new ArrayList<>();
            while (rs.next()) {
                listUserBucketsId.add(rs.getLong(1));
            }
            for (Long bucketId : listUserBucketsId) {
                bucketDao.deleteById(bucketId);
            }
        } catch (SQLException e) {
            throw new DataProcessingException(
                    "Can't delete user bucket by userId due delete user", e);
        }

        query = String.format("DELETE FROM %s WHERE user_id=?",
                USER_ROLES_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException(
                    "Can't delete user role by userId due delete user", e);
        }

        query = String.format("DELETE FROM %s WHERE id=?",
                USER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete user", e);
        }
    }

    @Override
    public boolean delete(User entity) throws DataProcessingException {
        deleteById(entity.getUserId());
        return true;
    }

    @Override
    public List<User> getAll() throws DataProcessingException {
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
            throw new DataProcessingException("Can't get all users", e);
        }
        return list;
    }
}
