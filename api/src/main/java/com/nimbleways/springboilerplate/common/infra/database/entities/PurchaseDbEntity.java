package com.nimbleways.springboilerplate.common.infra.database.entities;


import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDbEntity {
    @Id
    @Column(name = "id")
    @UuidGenerator
    @NotNull
    private UUID id;

    @Column(name = "brand")
    @NotNull
    private String brand;

    @Column(name = "model")
    @NotNull
    private String model;

    @Column(name = "price")
    @NotNull
    private Double price;

    @Column(name = "store")
    @NotNull
    private String store;

    @ElementCollection
    @CollectionTable(name = "purchase_images", joinColumns = @JoinColumn(name = "purchase_id"))
    @Column(name = "image_paths")
    @NotNull
    private List<String> imagePaths;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private UserDbEntity user; // Relation avec UserDbEntity


    public Purchase toPurchase() {
        return new Purchase(
                id,
                user.id(),
                brand,
                model,
                price,
                store,
                Immutable.list.ofAll(imagePaths()));
    }

    public static PurchaseDbEntity from(NewPurchase newPurchase) {
        final PurchaseDbEntity purchaseDbEntity = new PurchaseDbEntity();
        purchaseDbEntity.brand(newPurchase.brand());
        purchaseDbEntity.model(newPurchase.model());
        purchaseDbEntity.price(newPurchase.price());
        purchaseDbEntity.store(newPurchase.store());
        purchaseDbEntity.imagePaths(newPurchase.pathImages().toList());
        return purchaseDbEntity;
    }
}
