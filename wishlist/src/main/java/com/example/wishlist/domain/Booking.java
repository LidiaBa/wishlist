package com.example.wishlist.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Data //данный класс сущность
@Builder // этого нет на лекции
@NoArgsConstructor // этого нет на лекции
@AllArgsConstructor // этого нет на лекции

public class Booking {

    @Id //
    @GeneratedValue(strategy = GenerationType.IDENTITY) // все перекладывается на плечи субд, если там не описано все упадет, тоесть при создании id не передается
    private Long id;

    @Column(name = "wish_id")
    private Long wishId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "owner_id")
    private Long ownerId;
}