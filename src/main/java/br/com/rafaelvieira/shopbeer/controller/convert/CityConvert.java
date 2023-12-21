package br.com.rafaelvieira.shopbeer.controller.convert;

import br.com.rafaelvieira.shopbeer.domain.City;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CityConvert implements Converter<String, City> {

	@Override
	public City convert(String code) {
		if (!StringUtils.hasText(code)) {
			City city = new City();
			city.setCode(Long.valueOf(code));
			return city;
		}
		
		return null;
	}

}
