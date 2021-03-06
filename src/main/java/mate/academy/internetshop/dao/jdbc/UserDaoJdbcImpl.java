package mate.academy.internetshop.dao.jdbc;

import java.sql.*;
import java.util.*;
import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Role;
import mate.academy.internetshop.model.User;

@Dao
public class UserDaoJdbcImpl extends AbstractDao<User> implements UserDao {
    private static final String USER_TABLE = "users";
    private static final String ROLES_TABLE = "roles";
    private static final String USER_ROLES_TABLE = "user_roles";
    private static final String BUCKETS_TABLE = "bucket";
    private static final String ORDERS_TABLE = "orders";

    @Inject
    private static BucketDao bucketDao;

    public UserDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Optional<User> login(String login) throws DataProcessingException {
        String query = String.format("SELECT users.user_id AS userId, name, password, salt, token, "
                        + "roles.role_name AS role, roles.id AS idRole FROM %s INNER JOIN %s "
                        + "ON users.user_id=user_roles.user_id "
                        + "INNER JOIN %s ON user_roles.role_id = roles.id WHERE users.name=?",
                USER_TABLE, USER_ROLES_TABLE, ROLES_TABLE);
        return getUserOptional(getUserByParameter(query, login));
    }

    @Override
    public Optional<User> findByToken(String token) throws DataProcessingException {
        String query = String.format("SELECT users.user_id AS userId, name, password, salt, token, "
                        + "roles.role_name AS role, roles.id AS idRole FROM %s INNER JOIN %s "
                        + "ON users.user_id=user_roles.user_id "
                        + "INNER JOIN %s ON user_roles.role_id = roles.id WHERE users.token=?",
                USER_TABLE, USER_ROLES_TABLE, ROLES_TABLE);
        return getUserOptional(getUserByParameter(query, token));
    }

    private Optional<User> getUserOptional(User user) {
        return user != null ? Optional.of(user) : Optional.empty();
    }

    private User getUserByParameter(String query, String paramater)
            throws DataProcessingException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, paramater);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            while (rs.next()) {
                user = new User(rs.getString("name"), rs.getString("password"));
                user.setToken(rs.getString("token"));
                user.setSalt(Base64.getDecoder().decode(rs.getString("salt")));
                user.setUserId(rs.getLong("userId"));
                Role role = new Role(Role.RoleName.valueOf(rs.getString("role")));
                role.setId(rs.getLong("idRole"));
                user.setRole(role);
            }
            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find user by token", e);
        }
    }

    @Override
    public User create(User user) throws DataProcessingException {
        String query = String.format("INSERT INTO %s(name, password, salt, token) "
                        + "VALUE(?, ?, ?, ?)",
                USER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, Base64.getEncoder().encodeToString(user.getSalt()));
            ps.setString(4, user.getToken());
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            while (resultSet.next()) {
                user.setUserId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create new user", e);
        }

        user.setRoles(getUserRoles(user));
        return user;
    }

    private Set<Role> getUserRoles(User user) throws DataProcessingException {
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
        String query = String.format("SELECT users.user_id AS userId, name, password, salt, token, "
                        + "roles.role_name AS role, roles.id AS idRole FROM %s INNER JOIN %s "
                        + "ON users.user_id=user_roles.user_id "
                        + "INNER JOIN %s ON user_roles.role_id = roles.id WHERE users.user_id=?",
                USER_TABLE, USER_ROLES_TABLE, ROLES_TABLE);
        return Optional.of(getUserByParameter(query, String.valueOf(userId)));
    }

    @Override
    public User update(User user) throws DataProcessingException {
        User newUser = user;
        String query = String.format("UPDATE %s(name, password, salt, token) VALUE(?, ?, ?, ?)",
                USER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, Base64.getEncoder().encodeToString(user.getSalt()));
            ps.setString(4, user.getToken());
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

        newUser.setRoles(getUserRoles(newUser));
        return newUser;
    }

    @Override
    public void deleteById(Long userId) throws DataProcessingException {
        if (bucketDao.getByUserId(userId).isPresent()) {
            Bucket bucket = bucketDao.getByUserId(userId).get();
            bucketDao.delete(bucket);
        }
        deleteFromTableByUserId(userId, ORDERS_TABLE);
        deleteFromTableByUserId(userId, USER_ROLES_TABLE);
        deleteFromTableByUserId(userId, USER_TABLE);
    }

    private void deleteFromTableByUserId(Long userId, String table) throws DataProcessingException {
        String query = String.format("DELETE FROM %s WHERE user_id=?",
                table);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException(
                    "Can't delete user role by userId due delete user", e);
        }
    }

    @Override
    public void delete(User entity) throws DataProcessingException {
        deleteById(entity.getUserId());
    }

    @Override
    public List<User> getAll() throws DataProcessingException {
        List<User> list = new ArrayList<>();
        String query = String.format("SELECT * FROM %s",
                USER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("user_id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                User user = new User(name, password);
                user.setSalt(Base64.getDecoder().decode(resultSet.getString("salt")));
                user.setUserId(id);
                user.setRoles(getUserRoles(user));

                list.add(user);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all users", e);
        }
        return list;
    }
}
