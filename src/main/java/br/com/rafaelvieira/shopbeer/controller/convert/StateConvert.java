package br.com.rafaelvieira.shopbeer.controller.convert;

import br.com.rafaelvieira.shopbeer.domain.State;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StateConvert implements Converter<String, State> {

	@Override
	public State convert(String code) {
		if (!StringUtils.hasText(code)) {
			State state = new State();
			state.setCode(Long.valueOf(code));
			return state;
		}
		
		return null;
	}

}
