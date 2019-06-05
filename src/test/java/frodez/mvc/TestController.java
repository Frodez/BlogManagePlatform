package frodez.mvc;

import frodez.util.beans.result.Result;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/escape")
	public Result escapeEndPoint(@RequestParam("name") String name) {
		return new Cleaner(Whitelist.basic()).isValidBodyHtml(name) ? Result.success() : Result.fail();
	}

}
