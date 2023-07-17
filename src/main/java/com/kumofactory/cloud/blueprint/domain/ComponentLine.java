package com.kumofactory.cloud.blueprint.domain;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.ComponentLineDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComponentLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    private String ui_id; // Client 에서 생성하는 uuid
    @OneToOne(cascade = CascadeType.ALL)
    private ComponentDot source;
    @OneToOne(cascade = CascadeType.ALL)
    private ComponentDot destination;
    @ManyToOne
    private AwsBluePrint bluePrint;

    public static ComponentLine createComponentLink(ComponentLineDto lineDto, AwsBluePrint bluePrint) {
        ComponentLine componentLink = new ComponentLine();
        componentLink.setUi_id(lineDto.getId());
        componentLink.setSource(ComponentDot.createComponentDot(lineDto.getSrc()));
        componentLink.setDestination(ComponentDot.createComponentDot(lineDto.getDst()));
        componentLink.setBluePrint(bluePrint);
        return componentLink;
    }
}
