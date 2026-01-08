package showroom.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import showroom.model.Vehicle;
import showroom.util.HibernateUtil;

import java.util.List;

public class VehicleDAO {
    public void saveVehicle(Vehicle vehicle) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Check if table is empty
            Long count = session.createQuery("select count(v) from Vehicle v", Long.class).uniqueResult();
            if (count == 0) {
                try {
                    // Force reset sequence to 1. Using ALTER SEQUENCE is safer as a mutation.
                    session.createNativeMutationQuery("ALTER SEQUENCE vehicles_id_seq RESTART WITH 1").executeUpdate();
                } catch (Exception ex) {
                    System.err.println("Warning: Could not reset sequence ID: " + ex.getMessage());
                }
            }

            session.persist(vehicle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<Vehicle> getAllVehicles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Vehicle", Vehicle.class).list();
        }
    }

    public Vehicle getVehicleById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Vehicle.class, id);
        }
    }

    public void updateVehicle(Vehicle vehicle) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(vehicle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteVehicle(Vehicle vehicle) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.contains(vehicle) ? vehicle : session.merge(vehicle));
            session.flush(); // Ensure delete is executed

            // Reset the sequence to (MAX(id) + 1), so next ID fills the gap at the end
            // or restarts at 1 if empty.
            // Try to reset sequence, but don't fail transaction if it fails
            try {
                session.createNativeMutationQuery(
                        "SELECT setval('vehicles_id_seq', COALESCE((SELECT MAX(id) FROM vehicles), 0) + 1, false)")
                        .executeUpdate();
            } catch (Exception ex) {
                System.err.println("Warning: Could not adjust sequence ID: " + ex.getMessage());
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }
}
