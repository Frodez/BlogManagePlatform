package frodez.config.security.settings;

import io.undertow.UndertowOptions;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Undertow配置
 * @author Frodez
 * @date 2019-05-10
 */
@Configuration
public class UndertowConfig {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private ServerProperties serverProperties;

	/**
	 * 配置HTTPS和HTTP2
	 * @author Frodez
	 * @date 2019-05-10
	 */
	@Bean
	public WebServerFactory serverFactory() {
		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
		factory.addBuilderCustomizers(builder -> builder.addHttpListener(serverProperties.getPort(), "0.0.0.0"));
		//开启HTTP2
		factory.addBuilderCustomizers(builder -> {
			builder.setServerOption(UndertowOptions.ENABLE_HTTP2, serverProperties.getHttp2().isEnabled())
				.setServerOption(UndertowOptions.HTTP2_SETTINGS_ENABLE_PUSH, serverProperties.getHttp2().isEnabled());
		});
		//开启HTTP自动跳转至HTTPS
		factory.addDeploymentInfoCustomizers(deploymentInfo -> {
			deploymentInfo.addSecurityConstraint(new SecurityConstraint().addWebResourceCollection(
				new WebResourceCollection().addUrlPattern("/*")).setTransportGuaranteeType(
					TransportGuaranteeType.CONFIDENTIAL).setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
				.setConfidentialPortManager(exchange -> securityProperties.getHttpsPort());
		});
		return factory;
	}

}
