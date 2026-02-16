package com.fawry.fawrypayment.entity;

import com.fawry.fawrypayment.entity.sequence.BillerId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "biller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Biller {

    @Id
    @BillerId
    private String id;

    private String name;

    private boolean ultra;

    @OneToMany(mappedBy = "biller")
    private List<User> users;

}
