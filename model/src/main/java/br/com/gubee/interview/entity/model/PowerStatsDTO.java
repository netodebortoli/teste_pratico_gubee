package br.com.gubee.interview.entity.model;

import static lombok.AccessLevel.PRIVATE;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class PowerStatsDTO {

    @Min(value = 0, message = "message.powerstats.strength.min.0")
    @Max(value = 10, message = "message.powerstats.strength.max.10")
    @NotNull(message = "message.powerstats.strength.mandatory")
    private Integer strength;

    @Min(value = 0, message = "message.powerstats.agility.min.0")
    @Max(value = 10, message = "message.powerstats.agility.max.10")
    @NotNull(message = "message.powerstats.agility.mandatory")
    private Integer agility;

    @Min(value = 0, message = "message.powerstats.dexterity.min.0")
    @Max(value = 10, message = "message.powerstats.dexterity.max.10")
    @NotNull(message = "message.powerstats.dexterity.mandatory")
    private Integer dexterity;

    @Min(value = 0, message = "message.powerstats.intelligence.min.0")
    @Max(value = 10, message = "message.powerstats.intelligence.max.10")
    @NotNull(message = "message.powerstats.intelligence.mandatory")
    private Integer intelligence;
}
