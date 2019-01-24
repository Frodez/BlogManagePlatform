package frodez.constant.redis;

/**
 * redis key标志常量<br>
 * 使用redis时,应在key前面拼接常量中合适的标志.<br>
 * 每一种用途都建立自己的内部类,在里面进行管理.<br>
 * <strong>key不要重复!!!</strong>
 * @author Frodez
 * @date 2018-12-21
 */
public class Redis {

	public class User {

		public static final String TOKEN = "USER:TOKEN:";

		public static final String BASE_INFO = "USER:BASE_INFO:";

		public static final String PERMISSION_ALL = "USER:PERMISSION_ALL:";

	}

	public class Request {

		public static final String NO_REPEAT = "REQUEST:NO_REPEAT:";

	}

}
