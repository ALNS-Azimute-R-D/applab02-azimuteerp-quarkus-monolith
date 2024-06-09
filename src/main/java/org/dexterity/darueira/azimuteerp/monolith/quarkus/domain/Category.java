package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Category.
 */
@Entity
@Table(name = "tb_category")
@Cacheable
@RegisterForReflection
public class Category extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Size(max = 30)
    @Column(name = "acronym", length = 30)
    public String acronym;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    public String description;

    @Size(max = 255)
    @Column(name = "handler_clazz_name", length = 255)
    public String handlerClazzName;

    @OneToMany(mappedBy = "mainCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Article> articlesLists = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_parent_id")
    public Category categoryParent;

    @OneToMany(mappedBy = "categoryParent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Category> childrenCategoriesLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Category{" +
            "id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", name='" +
            name +
            "'" +
            ", description='" +
            description +
            "'" +
            ", handlerClazzName='" +
            handlerClazzName +
            "'" +
            "}"
        );
    }

    public Category update() {
        return update(this);
    }

    public Category persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Category update(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("category can't be null");
        }
        var entity = Category.<Category>findById(category.id);
        if (entity != null) {
            entity.acronym = category.acronym;
            entity.name = category.name;
            entity.description = category.description;
            entity.handlerClazzName = category.handlerClazzName;
            entity.articlesLists = category.articlesLists;
            entity.categoryParent = category.categoryParent;
            entity.childrenCategoriesLists = category.childrenCategoriesLists;
        }
        return entity;
    }

    public static Category persistOrUpdate(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("category can't be null");
        }
        if (category.id == null) {
            persist(category);
            return category;
        } else {
            return update(category);
        }
    }
}
