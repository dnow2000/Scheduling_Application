package Model;

public class Division {
    private int divisionId;
    private String division;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int countryId;

    public Division(int divisionId, String division, String createDate, String createdBy, String lastUpdate,String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;

    }

    public int getDivisionId() {return divisionId;}
    public String getDivision() {return division;}
    public String getCreateDate() {return createDate;}
    public String getCreatedBy() {return createdBy;}
    public String getLastUpdate() {return lastUpdate;}
    public String getLastUpdatedBy() {return lastUpdatedBy;}

}
