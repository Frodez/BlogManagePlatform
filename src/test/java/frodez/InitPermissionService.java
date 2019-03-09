package frodez;

import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.model.user.Permission;
import frodez.util.common.EmptyUtil;
import frodez.util.constant.setting.PropertyKey;
import frodez.util.constant.user.PermissionTypeEnum;
import frodez.util.http.URLMatcher;
import frodez.util.json.JSONUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.spring.context.ContextUtil;
import frodez.util.spring.properties.PropertyUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tk.mybatis.mapper.entity.Example;

/**
 * 根据所有controller初始化权限
 * @author Frodez
 * @date 2019-02-02
 */
public class InitPermissionService {

	public static void main(String[] args) {
		SpringApplication.run(BlogManagePlatformApplication.class, args);
		PermissionMapper permissionMapper = ContextUtil.get(PermissionMapper.class);
		String errorPath = PropertyUtil.get(PropertyKey.Web.BASE_PATH) + "/error";
		List<Permission> permissionList = new ArrayList<>();
		Date date = new Date();
		BeanFactoryUtils.beansOfTypeIncludingAncestors(ContextUtil.context(), HandlerMapping.class, true, false).values()
			.stream().filter((iter) -> {
				return iter instanceof RequestMappingHandlerMapping;
			}).map((iter) -> {
				return RequestMappingHandlerMapping.class.cast(iter).getHandlerMethods().entrySet();
			}).flatMap(Collection::stream).forEach((entry) -> {
				String requestUrl = PropertyUtil.get(PropertyKey.Web.BASE_PATH) + entry.getKey().getPatternsCondition()
					.getPatterns().stream().findFirst().get();
				if (!URLMatcher.needVerify(requestUrl) || requestUrl.equals(errorPath)) {
					return;
				}
				requestUrl = requestUrl.substring(PropertyUtil.get(PropertyKey.Web.BASE_PATH).length());
				String requestType = entry.getKey().getMethodsCondition().getMethods().stream().map(RequestMethod::name)
					.findFirst().orElse(PermissionTypeEnum.ALL.name());
				String permissionName = ReflectUtil.getShortMethodName(entry.getValue().getMethod());
				Permission permission = new Permission();
				permission.setCreateTime(date);
				permission.setUrl(requestUrl);
				permission.setName(permissionName);
				permission.setDescription(permissionName);
				switch (HttpMethod.resolve(requestType)) {
					case GET : {
						permission.setType(PermissionTypeEnum.GET.getVal());
						break;
					}
					case POST : {
						permission.setType(PermissionTypeEnum.POST.getVal());
						break;
					}
					case DELETE : {
						permission.setType(PermissionTypeEnum.DELETE.getVal());
						break;
					}
					case PUT : {
						permission.setType(PermissionTypeEnum.PUT.getVal());
						break;
					}
					default : {
						permission.setType(PermissionTypeEnum.ALL.getVal());
						break;
					}
				}
				permissionList.add(permission);
			});
		System.out.println("权限条目数量:" + permissionList.size());
		System.out.println("权限详细信息:" + JSONUtil.string(permissionList));
		Example example = new Example(Permission.class);
		permissionMapper.deleteByExample(example);
		if (EmptyUtil.no(permissionList)) {
			permissionMapper.insertList(permissionList);
		}
		SpringApplication.exit(ContextUtil.context(), () -> 1);
	}

}
