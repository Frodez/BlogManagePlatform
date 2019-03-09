package frodez.util.constant.setting;

/**
 * spring配置key常量<br>
 * 每一种用途都建立自己的内部类,在里面进行管理.<br>
 * <strong>key不要重复!!!</strong>
 * @author Frodez
 * @date 2019-01-06
 */
public class PropertyKey {

	/**
	 * web配置key
	 * @author Frodez
	 * @date 2019-01-06
	 */
	public class Web {

		/**
		 * 项目根路径
		 */
		public static final String BASE_PATH = "server.servlet.context-path";

		/**
		 * 项目端口
		 */
		public static final String PORT = "server.port";

	}

	/**
	 * 环境配置key
	 * @author Frodez
	 * @date 2019-02-08
	 */
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

}
