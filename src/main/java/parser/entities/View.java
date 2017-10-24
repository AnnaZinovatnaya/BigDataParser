package parser.entities;

import javax.persistence.*;

@Entity
@Table(name = "views")
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "year")
    private int year;

    @Column(name = "month")
    private int month;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "page_id")
    private Page page;

    @Column(name = "viewCount")
    private int viewCount;

    public View(long id, int year, int month, Page page, int viewCount) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.page = page;
        this.viewCount = viewCount;
    }

    public View(int year, int month, Page page, int viewCount) {
        this.year = year;
        this.month = month;
        this.page = page;
        this.viewCount = viewCount;
    }

    public View() {
        this.id = 0;
        this.year = 0;
        this.month = 0;
        this.page = null;
        this.viewCount = 0;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "View{" +
                "id=" + id +
                ", year=" + year +
                ", month=" + month +
                ", page=" + page +
                ", viewCount=" + viewCount +
                '}';
    }
}