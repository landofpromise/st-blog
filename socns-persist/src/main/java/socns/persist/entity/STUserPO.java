package socns.persist.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by shaohua.hsh on 2015-08-10.
 */
@Entity
@Table(name = "st2015")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class STUserPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "phone",unique = true, length = 11)
    private String phone;

    @Column(name = "name", length = 18)
    private String name;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "muqu")
    private String muqu;

    @Column(name = "dirth_year")
    private String birthYear;

    @Column(name = "zz_name")
    private String zzName;

    @Column(name = "dis_username")
    private String disUsername;

    @Column(name = "hometown")
    private String hometown;

    @Column(name = "is_dzz")
    private Integer isDzz;

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMuqu() {
        return muqu;
    }

    public void setMuqu(String muqu) {
        this.muqu = muqu;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getZzName() {
        return zzName;
    }

    public void setZzName(String zzName) {
        this.zzName = zzName;
    }

    public String getDisUsername() {
        return disUsername;
    }

    public void setDisUsername(String disUsername) {
        this.disUsername = disUsername;
    }

    public Integer getIsDzz() {
        return isDzz;
    }

    public void setIsDzz(Integer isDzz) {
        this.isDzz = isDzz;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
