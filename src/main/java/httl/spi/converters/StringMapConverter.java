/*
 * Copyright 2011-2012 HTTL Team.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package httl.spi.converters;

import httl.spi.Codec;
import httl.spi.Converter;
import httl.internal.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * StringMapConverter. (SPI, Singleton, ThreadSafe)
 * 
 * @see httl.spi.translators.DefaultTranslator#setMapConverter(Converter)
 * @see httl.spi.parsers.AbstractParser#setMapConverter(Converter)
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class StringMapConverter implements Converter<String, Map<String, Object>> {
	
	private String formats = "";

	private Codec[] codecs;

	public void setCodecs(Codec[] codecs) {
		this.codecs = codecs;
		StringBuilder buf = new StringBuilder();
		for (Codec codec : codecs) {
			if (buf.length() > 0) {
				buf.append(",");
			}
			buf.append(codec.getFormat());
		}
		this.formats = buf.toString();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> convert(String value) throws IOException,
			ParseException {
		if (StringUtils.isEmpty(value))
			return null;
		if (codecs != null) {
			value = value.trim();
			for (Codec codec : codecs) {
				if (codec.isDecodable(value)) {
					return (Map<String, Object>) codec.decode(value, Map.class);
				}
			}
		}
		throw new IllegalArgumentException("Unsupported format of the string \"" + value + "\", only support format: " + formats + ". Please add config codecs+=com.your.YourFormatStringCodec in httl.properties.");
	}

}