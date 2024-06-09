package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.SizeOptionEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Article.
 */
@Entity
@Table(name = "tb_article")
@Cacheable
@RegisterForReflection
public class Article extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "inventory_product_id", nullable = false)
    public Long inventoryProductId;

    @Size(max = 60)
    @Column(name = "sku_code", length = 60)
    public String skuCode;

    @Size(max = 150)
    @Column(name = "custom_name", length = 150)
    public String customName;

    @Size(max = 8192)
    @Column(name = "custom_description", length = 8192)
    public String customDescription;

    @Column(name = "price_value", precision = 21, scale = 2)
    public BigDecimal priceValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_size", nullable = false)
    public SizeOptionEnum itemSize;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_tb_article__asset_collection",
        joinColumns = @JoinColumn(name = "tb_article_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "asset_collection_id", referencedColumnName = "id")
    )
    @JsonbTransient
    public Set<AssetCollection> assetCollections = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "main_category_id")
    public Category mainCategory;

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrderItem> ordersItemsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Article{" +
            "id=" +
            id +
            ", inventoryProductId=" +
            inventoryProductId +
            ", skuCode='" +
            skuCode +
            "'" +
            ", customName='" +
            customName +
            "'" +
            ", customDescription='" +
            customDescription +
            "'" +
            ", priceValue=" +
            priceValue +
            ", itemSize='" +
            itemSize +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }

    public Article update() {
        return update(this);
    }

    public Article persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Article update(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("article can't be null");
        }
        var entity = Article.<Article>findById(article.id);
        if (entity != null) {
            entity.inventoryProductId = article.inventoryProductId;
            entity.skuCode = article.skuCode;
            entity.customName = article.customName;
            entity.customDescription = article.customDescription;
            entity.priceValue = article.priceValue;
            entity.itemSize = article.itemSize;
            entity.activationStatus = article.activationStatus;
            entity.assetCollections = article.assetCollections;
            entity.mainCategory = article.mainCategory;
            entity.ordersItemsLists = article.ordersItemsLists;
        }
        return entity;
    }

    public static Article persistOrUpdate(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("article can't be null");
        }
        if (article.id == null) {
            persist(article);
            return article;
        } else {
            return update(article);
        }
    }

    public static PanacheQuery<Article> findAllWithEagerRelationships() {
        return find("select distinct article from Article article left join fetch article.assetCollections");
    }

    public static Optional<Article> findOneWithEagerRelationships(Long id) {
        return find(
            "select article from Article article left join fetch article.assetCollections where article.id =?1",
            id
        ).firstResultOptional();
    }
}
