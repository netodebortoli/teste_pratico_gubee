package br.com.gubee.interview.entity.model;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class PowerStatsDTO {

    private Integer strength;

    private Integer agility;

    private Integer dexterity;

    private Integer intelligence;
}
