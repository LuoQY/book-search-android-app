package qingyang.cmu.edu;

public class BookBean {
    private String title;
    private String authors;
    private String publisher;
    private String imageLink;
    private String searchLink;

    public BookBean(String title, String authors, String publisher, String imageLink, String searchLink) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.imageLink = imageLink;
        this.searchLink = searchLink;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getSearchLink() {
        return searchLink;
    }

    @Override
    public String toString() {
        return "Title: " + title + "  Authors: " + authors + "  Publisher: " + publisher + "  ImageLink: " + imageLink;
    }
}
