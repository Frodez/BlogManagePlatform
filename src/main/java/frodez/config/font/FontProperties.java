package frodez.config.font;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 字体配置
 * @author Frodez
 * @date 2019-03-21
 */
@Data
@Component
@PropertySource(value = { "classpath:settings/global/font.properties" })
@ConfigurationProperties(prefix = "font")
public class FontProperties {

	/**
	 * 字体存放路径
	 */
	private String path = "";

	/**
	 * key对应别名,value对应文件名
	 */
	private Map<String, String> alias = new HashMap<>();

}
