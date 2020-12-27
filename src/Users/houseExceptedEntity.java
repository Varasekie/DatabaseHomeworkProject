package Users;

public class houseExceptedEntity {
    private String no;
    private String userId;
    private String area_low = "0";
    private String area_high;
    private String section;
    private String type;
    private String price_low = "0";
    private String price_high;
    private String employerId;
    private String conditions = "未处理";

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
    public String getArea_low() {
        return area_low;
    }

    public void setArea_low(String area_low) {
        this.area_low = area_low;
    }

    public String getArea_high() {
        return area_high;
    }

    public void setArea_high(String area_high) {
        this.area_high = area_high;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice_low() {
        return price_low;
    }

    public void setPrice_low(String price_low) {
        this.price_low = price_low;
    }

    public String getPrice_high() {
        return price_high;
    }

    public void setPrice_high(String price_high) {
        this.price_high = price_high;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }
}
