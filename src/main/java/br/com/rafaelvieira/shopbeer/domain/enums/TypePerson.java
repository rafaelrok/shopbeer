package br.com.rafaelvieira.shopbeer.domain.enums;

import br.com.rafaelvieira.shopbeer.domain.validation.group.CnpjGroup;
import br.com.rafaelvieira.shopbeer.domain.validation.group.CpfGroup;
import lombok.Getter;

@Getter
public enum TypePerson {

	PHYSICALPERSON("Physical Person", "CPF", "000.000.000-00", CpfGroup.class) {
		@Override
		public String formatar(String cpfOuCnpj) {
			return cpfOuCnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})", "$1.$2.$3-");
		}
	},

	LEGALPERSON("Legal Person", "CNPJ", "00.000.000/0000-00", CnpjGroup.class) {
		@Override
		public String formatar(String cpfOuCnpj) {
			return cpfOuCnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})", "$1.$2.$3/$4-");
		}
	};

	private final String description;
	private final String document;
	private final String mask;
	private final Class<?> group;

	TypePerson(String description, String document, String mask, Class<?> group) {
		this.description = description;
		this.document = document;
		this.mask = mask;
		this.group = group;
	}

	public abstract String formatar(String cpfOuCnpj);
	
	public static String removeformatting(String cpfOuCnpj) {
		return cpfOuCnpj.replaceAll("\\.|-|/", "");
	}

}
