package Model;

public class User {
    private int userId;
    private String username;
    private String password;
//    private String createDate;
//    private String createdBy;
//    private String lastUpdate;
//    private String lastUpdatedBy;

    public User (int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
//        this.createDate = createDate;
//        this.createdBy = createdBy;
//        this.lastUpdate = lastUpdate;
//        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getUserId() {return userId;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}
//    public String getCreateDate() {return createDate;}
//    public String getCreatedBy() {return createdBy;}
//    public String getLastUpdate() {return lastUpdate;}
//    public String getLastUpdatedBy() {return lastUpdatedBy;}

}
