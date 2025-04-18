package com.databaseproject.cinedev.models.movie;

import com.databaseproject.cinedev.models.base.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ticket {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double price;
    private LocalDateTime datePurchase;

    @ManyToOne
    @JoinColumn(name = "chair_id", nullable = false)
    private Chairs chairs;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "function_id", nullable = false)
    private Functions functions;


    public Ticket(Double price, LocalDateTime datePurchase, User user) {
        this.price = price;
        this.datePurchase = datePurchase;
        this.user = user;
    }
}
