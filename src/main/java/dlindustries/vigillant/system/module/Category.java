package dlindustries.vigillant.system.module;

import dlindustries.vigillant.system.utils.EncryptedString;

public enum Category {
	sword(EncryptedString.of("SMP")),
	CRYSTAL(EncryptedString.of("CPVP")),
	pot(EncryptedString.of("POT")),
	mace(EncryptedString.of("MACE")),
	optimizer(EncryptedString.of("Optimizer")),
	RENDER(EncryptedString.of("Render")),

	CLIENT(EncryptedString.of("Client"));
	public final CharSequence name;

	Category(CharSequence name) {
		this.name = name;
	}
}
