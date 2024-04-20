package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Save in DB.
     * @param user user.
     * @return user with id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return user;
    }

    /**
     * Update user in DB.
     * @param user user.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    /**
     * Delete user by id.
     * @param userId ID.
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE User WHERE id = :fId")
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    /**
     * List of users order by id.
     * @return list of users.
     */
    public List<User> findAllOrderById() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<User> result = session.createQuery("FROM User ORDER BY id", User.class).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Find user by ID.
     * @return Optional or user.
     */
    public Optional<User> findById(int userId) {
        Session session = sf.openSession();
        session.beginTransaction();
        User result = session.get(User.class, userId);
        session.getTransaction().commit();
        session.close();
        return Optional.of(result);
    }

    /**
     * List of users by login LIKE %key%.
     * @param key key.
     * @return list of users.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query =
                session.createQuery("FROM User WHERE login LIKE '%' || :fKey || '%'", User.class);
        query.setParameter("fKey", key);
        List<User> result = query.list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Find user by login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query = session.createQuery("FROM User WHERE login = :fLogin", User.class);
        query.setParameter("fLogin", login);
        User result = query.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return Optional.of(result);
    }
}
