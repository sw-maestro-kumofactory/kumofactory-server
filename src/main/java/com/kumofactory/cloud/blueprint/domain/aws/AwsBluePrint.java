package com.kumofactory.cloud.blueprint.domain.aws;

import com.kumofactory.cloud.blueprint.domain.BaseBluePrint;
import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

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
