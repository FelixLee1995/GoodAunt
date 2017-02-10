package space.ahaha.project;

/**
 * Created by ROhan on 2016/12/12 0012.
 */

public class PhotoContent {

    private String id ;
    private String url ;
    private String title ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PhotoContent() {
    }

    public PhotoContent(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }
}
