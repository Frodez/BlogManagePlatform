package security;

import frodez.BlogManagePlatformApplication;
import frodez.config.security.util.Matcher;
import frodez.constant.enums.user.PermissionTypeEnum;
import frodez.constant.settings.PropertyKey;
import frodez.dao.mapper.user.PermissionMapper;
import frodez.dao.model.user.Permission;
import frodez.util.common.EmptyUtil;
import frodez.util.json.JSONUtil;
import frodez.util.reflect.ReflectUtil;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.PropertyUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.SpringApplication;
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
		List<Permission> permissionList = new ArrayList<>();
		Date date = new Date();
		//拿到mvc里定义的所有端点,然后自动生成权限
		//这里是从spring上下文里拿出所有HandlerMapping
		BeanFactoryUtils.beansOfTypeIncludingAncestors(ContextUtil.context(), HandlerMapping.class, true, false)
			.values().stream().filter((iter) -> {
				//过滤出其中的RequestMappingHandlerMapping
				return iter instanceof RequestMappingHandlerMapping;
			}).map((iter) -> {
				//取出所有的RequestMapping和对应的HandlerMethod,即@RequestMapping,@GetMapping,@PostMapping这些注解和它们所在的方法
				return ((RequestMappingHandlerMapping) iter).getHandlerMethods().entrySet();
			}).flatMap(Collection::stream).forEach((entry) -> {
				//这里遍历map,map的类型是Entry<RequestMappingInfo, HandlerMethod>
				String requestUrl = PropertyUtil.get(PropertyKey.Web.BASE_PATH) + entry.getKey().getPatternsCondition()
					.getPatterns().stream().findFirst().get();
				//只有需要验证的url才有权限
				if (!Matcher.needVerify(requestUrl)) {
					return;
				}
				//把url的server.servlet.context-path以及它前面的部分去掉
				requestUrl = requestUrl.substring(PropertyUtil.get(PropertyKey.Web.BASE_PATH).length());
				//获得请求类型
				String requestType = entry.getKey().getMethodsCondition().getMethods().stream().map(RequestMethod::name)
					.findFirst().orElse(PermissionTypeEnum.ALL.name());
				String permissionName = ReflectUtil.getShortMethodName(entry.getValue().getMethod());
				Permission permission = new Permission();
				permission.setCreateTime(date);
				permission.setUrl(requestUrl);
				permission.setName(permissionName);
				permission.setDescription(permissionName);
				if (requestType.equals("GET")) {
					permission.setType(PermissionTypeEnum.GET.getVal());
				} else if (requestType.equals("POST")) {
					permission.setType(PermissionTypeEnum.POST.getVal());
				} else if (requestType.equals("DELETE")) {
					permission.setType(PermissionTypeEnum.DELETE.getVal());
				} else if (requestType.equals("PUT")) {
					permission.setType(PermissionTypeEnum.PUT.getVal());
				} else {
					permission.setType(PermissionTypeEnum.ALL.getVal());
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
