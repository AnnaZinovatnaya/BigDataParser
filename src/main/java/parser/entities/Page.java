package parser.entities;

import javax.persistence.*;

@Entity
@Table(name = "pages")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pageName")
    private String pageName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    public Page(String pageName, Category category) {
        this.pageName = pageName;
        this.category = category;
    }

    public Page(long id, String pageName, Category category) {
        this.id = id;
        this.pageName = pageName;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ",\tpageName='" + pageName + '\'' +
                ",\tcategory=" + category +
                '}';
    }
}