package br.com.gubee.interview.core.features.powerstats;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.gubee.interview.entity.model.PowerStatsDTO;

public class PowerStatsServiceImpl extends PowerStatsService {

   public PowerStatsServiceImpl(PowerStatsRepository powerStatsRepository) {
      super(powerStatsRepository);
   }

   @Override
   public UUID create(@Valid @NotNull PowerStatsDTO powerStatsDTO) {
      return UUID.randomUUID();
   }

   @Override
   public void delete(@NotNull UUID id) {

   }

   @Override
   public PowerStatsDTO findById(@NotNull UUID id) {
      return new PowerStatsDTO(7, 7, 7, 7);
   }

   @Override
   public PowerStatsDTO update(@NotNull UUID id, @Valid @NotNull PowerStatsDTO powerStatsDTO) {
      return new PowerStatsDTO(8, 8, 8, 8);
   }

}
