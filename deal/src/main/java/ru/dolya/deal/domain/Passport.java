package ru.dolya.deal.domain;



import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "passport")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    private Long passportId;

    @Column(name = "series")
    private String series;

    @Column(name = "number")
    private String number;

    @Column(name = "issue_branch")
    private String issueBranch;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @OneToOne(mappedBy = "passport")
    private Client client;
}
