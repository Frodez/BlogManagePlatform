package frodez.constant.settings;

import lombok.experimental.UtilityClass;

/**
 * spring配置key常量<br>
 * 每一种用途都建立自己的内部类,在里面进行管理.<br>
 * <strong>key不要重复!!!</strong>
 * @author Frodez
 * @date 2019-01-06
 */
@UtilityClass
public class PropertyKey {

	/**
	 * web配置key
	 * @author Frodez
	 * @date 2019-01-06
	 */
	@UtilityClass
	public class Web {

		/**
		 * 项目根路径
		 */
		public static final String BASE_PATH = "server.servlet.context-path";

		/**
		 * 项目端口
		 */
		public static final String PORT = "server.port";

		/**
		 * 默认错误路径
		 */
		public static final String ERROR_PATH = "server.error.path";

	}

	/**
	 * freeMarker配置key
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@UtilityClass
	public class FreeMarker {

		/**
		 * 后缀
		 */
		public static final String SUFFIX = "spring.freemarker.suffix";

		/**
		 * 模板路径
		 */
		public static final String LOADER_PATH = "spring.freemarker.template-loader-path";

	}

	/**
	 * 环境配置key
	 * @author Frodez
	 * @date 2019-02-08
	 */
	@UtilityClass
	public class Enviroment {

		/**
		 * 开发环境
		 */
		public static final String DEV = "dev";

		/**
		 * 测试环境
		 */
		public static final String TEST = "test";

		/**
		 * 发布环境
		 */
		public static final String RELEASE = "release";

		/**
		 * 生产环境
		 */
		public static final String PROD = "prod";

	}

	/**
	 * 邮件配置key
	 * @author Frodez
	 * @date 2019-03-21
	 */
	@UtilityClass
	public class Mail {

		/**
		 * 自己的邮箱
		 */
		public static final String OWN_USER = "spring.mail.username";

	}

}
