package com.kumofactory.cloud.blueprint.domain.aws;

import com.kumofactory.cloud.blueprint.domain.BaseBluePrint;
import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import com.kumofactory.cloud.member.domain.Member;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AwsBluePrint extends BaseBluePrint {
    @OneToMany(mappedBy = "bluePrint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AwsArea> areas;

    @OneToMany(mappedBy = "bluePrint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AwsComponent> cspComponents;

    @OneToMany(mappedBy = "bluePrint", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ComponentLine> lines;

}
