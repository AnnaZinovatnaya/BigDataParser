package parser;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import parser.entities.Category;

import javax.persistence.Query;
import java.util.List;

public class WikiDB {
  private static WikiDB instance = null;

  public static synchronized WikiDB getInstance() {
    if (instance == null) {
      instance = new WikiDB();
    }
    return instance;
  }

  private WikiDB() {
  }

  public synchronized void addCategory(Category category) {

    SessionFactory sessionFactory = HibernateUtilities.getSessionFactory();

    try (Session session = sessionFactory.openSession()) {
      Transaction tx;
      tx = session.beginTransaction();

      session.save(category);

      tx.commit();
    }
  }
}