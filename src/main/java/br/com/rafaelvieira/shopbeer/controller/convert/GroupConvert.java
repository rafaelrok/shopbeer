package br.com.rafaelvieira.shopbeer.controller.convert;

import br.com.rafaelvieira.shopbeer.domain.Group;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
public class GroupConvert implements Converter<String, Group> {

	@Override
	public Group convert(String code) {
		if (!StringUtils.isEmpty(code)) {
			Group group = new Group();
			group.setCode(Long.valueOf(code));
			return group;
		}
		
		return null;
	}

}
