package ru.dolya.deal.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dolya.deal.domain.enums.EmploymentPosition;
import ru.dolya.deal.domain.enums.EmploymentStatus;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employment")
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employment_id")
    private Long employmentId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    @Column(name = "employer_inn")
    private String employerInn;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private EmploymentPosition position;

    @Column(name = "work_experience_total")
    private int workExperienceTotal;

    @Column(name = "work_experience_current")
    private int workExperienceCurrent;

    @OneToOne(mappedBy = "employment")
    private Client client;
}
