package br.com.rafaelvieira.shopbeer.controller.convert;

import br.com.rafaelvieira.shopbeer.domain.GroupEmployee;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
public class GroupConvert implements Converter<String, GroupEmployee> {

	@Override
	public GroupEmployee convert(String code) {
		if (!StringUtils.isEmpty(code)) {
			GroupEmployee groupEmployee = new GroupEmployee();
			groupEmployee.setCode(Long.valueOf(code));
			return groupEmployee;
		}
		
		return null;
	}

}
