package parser;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import parser.entities.Category;
import parser.entities.Page;

import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
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

      SessionFactory sessionFactory2 = HibernateUtilities.getSessionFactory();

      List<Page> pages = new ArrayList<>();

      try (Session session = sessionFactory2.openSession()) {
        Transaction tx;
        tx = session.beginTransaction();
        Query query = session.createQuery("select "
                + "new parser.entities.Page(p.id, p.pageName, p.category) "
                + "from Page p");

        pages =  query.getResultList();

        tx.commit();
      }

      for (Page page : pages) {
        text.add("\t->\t" + page.toStringWithoutCategory());
      }
    }
    Files.write(file, text, Charset.forName("UTF-8"));
  }
}