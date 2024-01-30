package br.com.gubee.interview.entity.model;

import static lombok.AccessLevel.PRIVATE;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.gubee.interview.entity.enums.Race;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class HeroDTO {

    @NotBlank(message = "message.name.mandatory")
    @Length(min = 1, max = 255, message = "message.name.length.min.1.max.255")
    private String name;

    @NotNull(message = "message.race.mandatory")
    private Race race;

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
