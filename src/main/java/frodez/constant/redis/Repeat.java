package frodez.constant.redis;

/**
 * 重复请求控制所用key<br>
 * 每一个控制器都建立自己的一个内部类,在里面进行管理<br>
 * <strong>key不要重复!!!</strong>
 * @author Frodez
 * @date 2018-12-21
 */
public class Repeat {

	public class Login {

		public static final String AUTH = "LOGIN:AUTH:";

	}

}
