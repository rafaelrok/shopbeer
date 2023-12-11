package br.com.rafaelvieira.shopbeer.controller.convert;

import br.com.rafaelvieira.shopbeer.domain.Style;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StyleConvert implements Converter<String, Style> {

	@Override
	public Style convert(String code) {
		if (!StringUtils.hasText(code)) {
			Style style = new Style();
			style.setCode(Long.valueOf(code));
			return style;
		}
		
		return null;
	}

}
