package org.nguyen.orderjava.models.jpa;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.nguyen.orderjava.models.BeanTypeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ORDERS")
public class OrderJpa {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ORDER_ID")
    private String id;

    @Column(name = "ORDERED_BY")
    private String orderedBy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    List<OrderContentJpa> contents = new ArrayList<>();

    public List<OrderContentJpa> getContents() {
        return contents;
    }

    public void addContent(OrderContentJpa content) {
        this.contents.add(content);
    }

    public void removeContent(BeanTypeEnum type) {
        OrderContentJpa content = findMatchingContent(type.getName(), this.contents);

        this.contents.remove(content);
    }

    private OrderContentJpa findMatchingContent(String type, List<OrderContentJpa> suspectContent) {
        for (OrderContentJpa suspect : suspectContent) {
            if (suspect.getBeanType().equals(type)) {
                return suspect;
            }
        }

        return null;
    }
}
