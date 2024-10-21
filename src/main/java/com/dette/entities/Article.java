package com.dette.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "article")
public class Article extends AbstractEntity {
    private String libelle;
    private String ref;
    private Integer qteStock;
    private Double prix;

    @OneToMany(mappedBy = "article")
    private List<Detail> details;

    public Article() {
    }

    public Article(String libelle, Integer qteStock, Double prix) {
        this.libelle = libelle;
        this.qteStock = qteStock;
        this.prix = prix;
    }

    public void addDetail(Detail detail) {
        details.add(detail);
    }

    @Override
    public String toString() {
        return "Article [libelle=" + libelle + ", ref=" + ref + ", qteStock=" + qteStock + ", prix=" + prix + "]";
    }
}
