package example.dao;
import example.pojo.User;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserJDBCTemplate implements UserDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemp;

    @Override
    @Autowired // 根据类型自动装配
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public void create(String username, String password, String email, String phone, String education) {
        String sql = "insert into user(username, password, email, phone, education)" +
                " values(?, ?, ?, ?, ?);";
        jdbcTemp.update(sql, username, password, email, phone, education);
        System.out.println("Created record username = " + username);
    }

    @Override
    public User getUser(String username) {
        String sql = "select * from user where username = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{username}, new UserMapper());
    }

    @Override
    public boolean isUsernameExist(String username) {
        String sql = "select username from user where username = ?;";
        try {
            String res = jdbcTemp.queryForObject(sql, new Object[]{username}, java.lang.String.class);
            System.out.println("res: " + res);
            return res.equals(username);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean isEmailExist(String email) {
        String sql = "select email from user where email = ?;";
        try {
            String res = jdbcTemp.queryForObject(sql, new Object[]{email}, java.lang.String.class);
            System.out.println("res: " + res);
            System.out.println("done.");
            return res.equals(email);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public List<User> listUsers() {
        String sql = "select * from user;";
        return jdbcTemp.query(sql, new UserMapper());
    }

    @Override
    public void delete(String username) {
        String sql = "delete from user where username = ?;";
        jdbcTemp.update(sql, username);
        System.out.println("Deleted record with username = " + username);
    }

    @Override
    public void resetPassword(String username, String newPassword) {
        String sql = "update user set password = ? where username = ?;";
        jdbcTemp.update(sql, newPassword, username);
        System.out.println("Updated record with username = " + username);
    }

    @Override
    public String getStudying(String username) {
        String sql = "select studying from user where username = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{username}, java.lang.String.class);
    }

    @Override
    public void setStudying(String username, String newTitle) {
        // ===== 不确定是否加单引号
        String sql = "update user set studying = ?, studied = '0', plan = '20', finished = '0' where username = ?;";
        jdbcTemp.update(sql, newTitle, username);
    }

    @Override
    public void setPlan(String user, int num) {
        String sql = "update user set plan = ? where username = ?;";
        jdbcTemp.update(sql, num, user);
    }

    @Override
    public int getPlan(String user) {
        String sql = "select plan from user where username = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{user}, java.lang.Integer.class);
    }

    @Override
    public int getStudied(String user) {
        String sql = "select studied from user where username = ?;";
        return jdbcTemp.queryForObject(sql, new Object[]{user}, java.lang.Integer.class);
    }

    @Override
    public void updateStudied(String user, int num) {
        String sql = "update user set studied = studied + ? where username = ?;";
        jdbcTemp.update(sql, num, user);
    }

    @Override
    public void updateDate(String user, String date) {
        String sql = "update user set lastDate = ? where username = ?;";
        jdbcTemp.update(sql, date, user);
    }
}
