package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepositoryImpl implements UserRepository {
    private Logger log = LoggerFactory.getLogger(JdbcUserRepositoryImpl.class);

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = (ResultSet rs) -> {
        Map<Integer, User> map = new HashMap<>();
        User user = null;
        while (rs.next()) {
            Integer id = rs.getInt("id");
            user = new User(id,
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("calories_per_day"),
                    rs.getBoolean("enabled"),
                    new Date(rs.getDate("registered").getTime()),
                    Collections.singleton(Role.valueOf(rs.getString("role"))));
            map.merge(id, user, (oldUser, newUser) -> {
                Set<Role> oldRoles = oldUser.getRoles();
                oldRoles.addAll(newUser.getRoles());
                oldUser.setRoles(oldRoles);
                return oldUser;
            });
        }
        List<User> list = new ArrayList<>(map.values());
        /*list.sort((o1, o2) -> {
            int result = o1.getName().compareTo(o2.getName());
            return result == 0 ? o1.getEmail().compareTo(o2.getEmail()) : result;
        });*/
        list.sort(Comparator.comparing(User::getName).thenComparing(User::getEmail));
        return list;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
//        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
//                .addValue("id", user.getId())
//                .addValue("name", user.getName())
//                .addValue("email", user.getEmail())
//                .addValue("password", user.getPassword())
//                .addValue("enabled", user.isEnabled())
//                .addValue("registered", user.getRegistered())
//                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        rolesBatchUpdate(user.getId(), user);
        return user;
    }

    private void rolesBatchUpdate(int id, User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=:id", user.getId());
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, id);
                ps.setString(2, new ArrayList<>(user.getRoles()).get(i).name());
            }

            @Override
            public int getBatchSize() {
                return user.getRoles().size();
            }
        });
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }


    @Override
    public User get(int id) {
        log.debug("Transaction active:::: {}", TransactionSynchronizationManager.isActualTransactionActive());
        //List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id=ur.user_id WHERE u.id=?", ROW_MAPPER, id);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            List<Role> roles = jdbcTemplate.queryForList("SELECT DISTINCT role FROM user_roles WHERE user_id=?", Role.class, id);
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            List<Role> roles = jdbcTemplate.queryForList("SELECT DISTINCT role FROM user_roles WHERE user_id=?", Role.class, user.getId());
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id=ur.user_id", RESULT_SET_EXTRACTOR);
    }
}
