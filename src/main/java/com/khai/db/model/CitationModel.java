package com.khai.db.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "citation")
public class CitationModel {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", length = 6, nullable = false)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "type")
    private String type;
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "editor_type")
    private String editorType;

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "authors")
//    private List<Person> authors;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "authors")
    private Set<Person> authors;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "editors")
    private List<Person> editors;

    @Column(name = "publisher_name")
    private String publisherName;
    @Column(name = "publisher_city")
    private String publisherCity;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "year")
    private String year;
    @Column(name = "date")
    private String date;
    @Column(name = "volume")
    private String volume;
    @Column(name = "no")
    private String no;
    @Column(name = "page")
    private String page;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Person> authors) {
        this.authors = authors;
    }

    public List<Person> getEditors() {
        return editors;
    }

    public void setEditors(List<Person> editors) {
        this.editors = editors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEditorType() {
        return editorType;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setEditorType(String editorType) {
        this.editorType = editorType;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherCity() {
        return publisherCity;
    }

    public void setPublisherCity(String publisherCity) {
        this.publisherCity = publisherCity;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }


    @Override
    public String toString() {
        return title;
    }
}
