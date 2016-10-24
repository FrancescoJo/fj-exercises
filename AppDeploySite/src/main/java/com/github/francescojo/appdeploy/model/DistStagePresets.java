/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.model;

import java.util.ArrayList;
import java.util.List;

import com.github.francescojo.appdeploy.dto.DistStageDto;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public enum DistStagePresets {
	INHOUSE_DAILY_SNAPSHOT("Inhouse daily snapshot", "Binary is delivered by every code changes "
		+ "in this distribution stage. Product maybe instable although codes are "
		+ "unit-test passed, since this stage does not take any integration/acceptance "
		+ "tests. This stage is referred as ALPHA or DEVELOPMENT in many dev. organisations.", 1),
	CLOSED_BETA("Closed beta", "Only integration/acceptance test passed codes are integrated "
		+ "in this stage; therefore binary is at least shippable. However for development "
		+ "reasons, some debug logs retained in product and unexpected performance degradation "
		+ "maybe found.", 1),
	OPEN_BETA("Open beta", "Code and quality of binary in this stage is essentially same as "
		+ "closed beta; however automatic optimisation by build tool is applied as well, "
		+ "therefore dev-ops team can gather user behaviour by ready-to-ship product "
		+ "in this stage.", 100),
	PRODUCTION("Production", "Binary quality must met production standard in this stage. "
		+ "Agressive code and resources optimisation is applied, all development backdoors "
		+ "for quick peek must be eliminated as well. Problems found in this stage are "
		+ "difficult to reproduce and usually cost a lot to solve.", Integer.MAX_VALUE);

	final String name;
	final String description;
	final int binaryPreserveCount;

	DistStagePresets(String s1, String s2, int i1) {
		this.name = s1;
		this.description = s2;
		this.binaryPreserveCount = i1;
	}

	public static List<DistStageDto> asDistStageDtoList() {
		List<DistStageDto> stageDtoList = new ArrayList<>();
		for(DistStagePresets preset : DistStagePresets.values()) {
			stageDtoList.add(preset.asDistStageDto());
		}
		return stageDtoList;
	}

	public DistStageDto asDistStageDto() {
		DistStageDto distStageDto = new DistStageDto();
		distStageDto.setName(name);
		distStageDto.setDescription(description);
		distStageDto.setPreserveCount(binaryPreserveCount);

		return distStageDto;
	}
}
