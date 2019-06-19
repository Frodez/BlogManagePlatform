package frodez.async;

import com.github.pagehelper.PageHelper;
import frodez.config.aop.log.annotation.DurationLog;
import frodez.config.aop.log.annotation.MethodLog;
import frodez.config.aop.log.annotation.ResultLog;
import frodez.config.aop.request.annotation.TimeoutLock;
import frodez.config.aop.validation.annotation.Check;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.util.beans.param.QueryPage;
import frodez.util.beans.result.Result;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class AsyncService {

	@Autowired
	private PermissionMapper permissionMapper;

	@Check
	@MethodLog
	@ResultLog
	@DurationLog(threshold = 10)
	@TimeoutLock(10)
	public ListenableFuture<Result> async(@Valid @NotNull QueryPage param) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Result.errorService().async();
		}
		return Result.page(PageHelper.startPage(param).doSelectPage(() -> permissionMapper.selectAll())).async();
	}

	@Check
	@MethodLog
	@ResultLog
	@DurationLog(threshold = 10)
	@TimeoutLock(10)
	public Result result(@Valid @NotNull QueryPage param) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Result.errorService();
		}
		return Result.page(PageHelper.startPage(param).doSelectPage(() -> permissionMapper.selectAll()));
	}

}
