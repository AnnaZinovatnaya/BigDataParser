package parser.entities;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "categoryName")
    private String categoryName;

    @Column(name = "numberOfFiles")
    private long numberOfFiles;

    @Column(name = "numberOfPages")
    private long numberOfPages;

    public Category(String categoryName, long numberOfFiles, long numberOfPages) {
        this.categoryName = categoryName;
        this.numberOfFiles = numberOfFiles;
        this.numberOfPages = numberOfPages;
    }

    public Category(long id, String categoryName, long numberOfFiles, long numberOfPages) {
        this.id = id;
        this.categoryName = categoryName;
        this.numberOfFiles = numberOfFiles;
        this.numberOfPages = numberOfPages;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getNumberOfFiles() {
        return numberOfFiles;
    }

    public void setNumberOfFiles(long numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    public long getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(long numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ",\tcategoryName='" + categoryName + '\'' +
                ",\tnumberOfFiles=" + numberOfFiles +
                ",\tnumberOfPages=" + numberOfPages +
                '}';
    }
}