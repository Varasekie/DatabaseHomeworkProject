package Entity;

import java.util.Date;

public class dealEntity {
    private String No;
    private String employId;
    private String houseId;
    private Date date;
    private String contract;
    private String price;
    private String employerName;
    private String BuyerName;

    private String BuyerTele;

    public String getBuyerTele() {
        return BuyerTele;
    }

    public void setBuyerTele(String buyerTele) {
        BuyerTele = buyerTele;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }


    private String houseLocation;
    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        this.No = no;
    }

    public String getEmployId() {
        return employId;
    }

    public void setEmployId(String employId) {
        this.employId = employId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
