package frodez.constant.converters;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;

@UtilityClass
public class HttpMethodReverter {

	/**
	 * 按位与将short转换为HttpMethod列表<br>
	 * 某位为0代表无，为1代表有，从低位到高位为GET,POST,PUT,DELETE,HEAD,PATCH,OPTIONS,TRACE
	 * @author Frodez
	 * @date 2019-12-29
	 */
	public static List<HttpMethod> revert(Short methods) {
		if ((methods & 0xff00) != 0) {
			return null;
		}
		List<HttpMethod> result = new ArrayList<>(4);
		if ((methods & 0x01) != 0) {
			result.add(HttpMethod.GET);
		}
		if ((methods & 0x02) != 0) {
			result.add(HttpMethod.POST);
		}
		if ((methods & 0x04) != 0) {
			result.add(HttpMethod.PUT);
		}
		if ((methods & 0x08) != 0) {
			result.add(HttpMethod.DELETE);
		}
		if ((methods & 0x10) != 0) {
			result.add(HttpMethod.HEAD);
		}
		if ((methods & 0x20) != 0) {
			result.add(HttpMethod.PATCH);
		}
		if ((methods & 0x40) != 0) {
			result.add(HttpMethod.OPTIONS);
		}
		if ((methods & 0x80) != 0) {
			result.add(HttpMethod.TRACE);
		}
		return result;
	}

}
