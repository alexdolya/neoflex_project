package ru.dolya.deal.domain;

import lombok.*;

import ru.dolya.deal.domain.enums.Gender;
import ru.dolya.deal.domain.enums.MartialStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientID;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "martial_status")
    @Enumerated(EnumType.STRING)
    private MartialStatus martialStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", referencedColumnName = "passport_id")
    private Passport passport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_id", referencedColumnName = "employment_id")
    private Employment employment;

    @Column(name = "account")
    private String account;

    @OneToOne(mappedBy = "client")
    private Application application;

}
