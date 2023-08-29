package ru.dolya.deal.model.domain;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.dolya.deal.model.dto.PassportDTO;

import javax.persistence.*;

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

    @Column(name = "passport")
    @Type(type = "jsonb")
    private PassportDTO passportDTO;

    @OneToOne(mappedBy = "passport")
    private Client client;
}
