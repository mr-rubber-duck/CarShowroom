package showroom;

import org.hibernate.Session;
import org.hibernate.Transaction;

import showroom.util.HibernateUtil;

public class TestHibernateConnection {
    public static void main(String[] args) {
        System.out.println("Testing Hibernate Connection...");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Session opened successfully!");

            Transaction tx = session.beginTransaction();
            System.out.println("Transaction started...");

            // Check if we can query (even if empty)
            long count = session.createQuery("select count(v) from Vehicle v", Long.class).uniqueResult();
            System.out.println("Number of vehicles in DB: " + count);

            tx.commit();
            System.out.println("Transaction committed.");
            System.out.println("Hibernate connection works!");
        } catch (Exception e) {
            System.err.println("Hibernate connection failed!");
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
