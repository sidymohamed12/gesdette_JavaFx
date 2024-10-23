package com.dette.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dette.enums.Etat;
import com.dette.enums.EtatConvert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "dette")
public class Dette extends AbstractEntity {

    private Double montant;
    private Double montantVerser;

    @Transient
    private Double montantRestant;

    public Double getMontantRestant() {
        return (this.montant - this.montantVerser);
    }

    private Boolean archiver;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private Client clientD;

    @Convert(converter = EtatConvert.class)
    @Column(name = "etatId")
    private Etat etatD;

    @OneToMany(mappedBy = "dette")
    private List<Detail> details = new ArrayList<>();

    @OneToMany(mappedBy = "dette")
    private List<Payement> payements = new ArrayList<>();

    public void addDetail(Detail detail) {
        details.add(detail);
    }

    public void addPayement(Payement pay) {
        payements.add(pay);
        setMontantRestant(montant - montantVerser);
    }

    @Override
    public String toString() {
        return "Dette [id=" + id + ", montant=" + montant + ", montantVerser=" + montantVerser + ", montantRestant="
                + montantRestant
                + ", date=" + date + ", clientD=" + clientD + ", etatD=" + etatD + "]";
    }

}
