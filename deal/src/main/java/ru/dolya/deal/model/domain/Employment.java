package ru.dolya.deal.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.dolya.deal.model.dto.EmploymentDTO;


import javax.persistence.*;

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

    @Column(name = "employment")
    @Type(type = "jsonb")
    private EmploymentDTO employment;

    @OneToOne(mappedBy = "employment")
    private Client client;
}