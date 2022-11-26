package tech.antoniosgarbi.desafioapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "tb_access_tag_register")
@Data
@NoArgsConstructor
public class AccessTagRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    @ManyToOne
    private Tag tag;
    @ManyToOne
    private UserCustomer user;

    public AccessTagRegister(Tag tag, UserCustomer user) {
        this.tag = tag;
        this.user = user;
        this.date = new Date();
    }
}
