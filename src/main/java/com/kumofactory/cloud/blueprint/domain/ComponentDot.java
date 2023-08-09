package com.kumofactory.cloud.blueprint.domain;

import com.kumofactory.cloud.blueprint.dto.ComponentDotDto;
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
public class ComponentDot {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		@CreationTimestamp
		private Date created_at;
		@UpdateTimestamp
		private Date updated_at;

		private Integer x; // 점의 x 좌표
		private Integer y; // 점의 y 좌표
		private String componentId; // 점이 속한 컴포넌트의 id

		public static ComponentDot createComponentDot(ComponentDotDto componentDotDto) {
				ComponentDot componentDot = new ComponentDot();
				componentDot.setComponentId(componentDotDto.getComponentId());
				componentDot.setX(componentDotDto.getX());
				componentDot.setY(componentDotDto.getY());
				return componentDot;
		}

}
