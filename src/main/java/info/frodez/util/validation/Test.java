package info.frodez.util.validation;

import info.frodez.dao.result.user.LoginVO;

public class Test {

	public static void main(String[] args) {
		LoginVO vo = new LoginVO();
		System.out.println(ValidationUtil.validate(vo));
	}

}
