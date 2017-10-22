package parser;

import org.hibernate.*;
import parser.entities.*;
import javax.persistence.Query;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

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

  public synchronized void addPage(Page page) throws HibernateException {

    SessionFactory sessionFactory = HibernateUtilities.getSessionFactory();

    try (Session session = sessionFactory.openSession()) {
      Transaction tx;
      tx = session.beginTransaction();

      session.save(page);
      tx.commit();
    } catch (HibernateException ex) {
      throw ex;
    }
  }

  public synchronized void saveAllCategoriesToFile(String filePath) throws IOException {
    Path file = Paths.get(filePath);

    SessionFactory sessionFactory = HibernateUtilities.getSessionFactory();

    List<Category> categories = new ArrayList<>();

    try (Session session = sessionFactory.openSession()) {
      Transaction tx;
      tx = session.beginTransaction();
      Query query = session.createQuery("select "
              + "new parser.entities.Category(c.id, c.categoryName, c.numberOfFiles, c.numberOfPages) "
              + "from Category c");

      categories =  query.getResultList();
      tx.commit();
    }

    List<String> text = new ArrayList<>();
    for (Category category : categories) {
      text.add(category.toString());
    }
    Files.write(file, text, Charset.forName("UTF-8"));
  }

  public synchronized void saveAllPagesToFile(String filePath) throws IOException {
    Path file = Paths.get(filePath);

    SessionFactory sessionFactory = HibernateUtilities.getSessionFactory();

    List<Page> pages = new ArrayList<>();

    try (Session session = sessionFactory.openSession()) {
      Transaction tx;
      tx = session.beginTransaction();
      Query query = session.createQuery("select "
              + "new parser.entities.Page(p.id, p.pageName, p.category) "
              + "from Page p");

      pages =  query.getResultList();
      tx.commit();
    }

    List<String> text = new ArrayList<>();
    for (Page page : pages) {
      text.add(page.toString());
    }
    Files.write(file, text, Charset.forName("UTF-8"));
  }

  public synchronized void saveAllDataToFile(String filePath) throws IOException {
    Path file = Paths.get(filePath);
    List<String> text = new ArrayList<>();

    List<Category> categories = new ArrayList<>();

    for (long i = 1; i <=  13000 ; i+=500) {

      SessionFactory sessionFactory = HibernateUtilities.getSessionFactory();

      try (Session session = sessionFactory.openSession()) {
        Transaction tx;
        tx = session.beginTransaction();

        Query query = session.createQuery("select "
                + "new parser.entities.Category(c.id, c.categoryName, c.numberOfFiles, c.numberOfPages) "
                + "from Category c "
                + "where c.id>=:min and c.id<=:max");

        query.setParameter("min", i);
        query.setParameter("max", i+499);

        categories =  query.getResultList();
        tx.commit();
      }

      for (Category category : categories) {
        System.out.println("Category - " + category.getId() + "/13000");

        text.add(category.toString());

        SessionFactory sessionFactory2 = HibernateUtilities.getSessionFactory();

        List<Page> pages = new ArrayList<>();

        try (Session session2 = sessionFactory2.openSession()) {
          Transaction tx2;
          tx2 = session2.beginTransaction();
          Query query2 = session2.createQuery("select "
                  + "new parser.entities.Page(p.id, p.pageName, p.category) "
                  + "from Page p where p.category.id=:id");
          query2.setParameter("id", category.getId());

          pages =  query2.getResultList();
          tx2.commit();
        }

        for (Page page : pages) {
          text.add("\t->\t" + page.toStringWithoutCategory());
        }
      }
    }
    Files.write(file, text, Charset.forName("UTF-8"));
  }

  public static List<Category> getAllCategories(){
    SessionFactory sessionFactory = HibernateUtilities.getSessionFactory();

    List<Category> categories = new ArrayList<>();

    try (Session session = sessionFactory.openSession()) {
      Transaction tx;
      tx = session.beginTransaction();
      Query query = session.createQuery("select "
              + "new parser.entities.Category(c.id, c.categoryName, c.numberOfFiles, c.numberOfPages) "
              + "from Category c");

      categories =  query.getResultList();
      tx.commit();
    }
    return categories;
  }
}