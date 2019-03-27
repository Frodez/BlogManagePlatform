package frodez.util.mail;

import frodez.util.constant.setting.PropertyKey;
import frodez.util.io.CircularByteBuffer;
import frodez.util.spring.ContextUtil;
import frodez.util.spring.PropertyUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 邮件工具类<br>
 * <strong>警告!!!如果要使用本类的方法,必须确保MailUtil已经被初始化!</strong><br>
 * <strong>方式:在使用本方法的类上加入@DependsOn("mailUtil")注解。</strong>
 * @author Frodez
 * @date 2019-03-27
 */
@Lazy
@Component("mailUtil")
public class MailUtil {

	private static JavaMailSender mailSender;

	@PostConstruct
	private void init() {
		mailSender = ContextUtil.get(JavaMailSender.class);
		Assert.notNull(mailSender, "mailSender must not be null");
	}

	/**
	 * 获取mailSender
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static JavaMailSender mailSender() {
		return mailSender;
	}

	/**
	 * 获取设置的自己的邮箱
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static String ownUser() {
		return PropertyUtil.get(PropertyKey.Mail.OWN_USER);
	}

	/**
	 * 发送邮件
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static void send(String from, String to, String subject, String text) {
		Assert.notNull(from, "from must not be null");
		Assert.notNull(to, "to must not be null");
		Assert.notNull(subject, "subject must not be null");
		Assert.notNull(text, "text must not be null");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

	/**
	 * 发送邮件
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static void send(SimpleMailMessage message) {
		Assert.notNull(message, "message must not be null");
		mailSender.send(message);
	}

	/**
	 * 发送邮件
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static void send(MimeMessage message) {
		Assert.notNull(message, "message must not be null");
		mailSender.send(message);
	}

	/**
	 * 发送邮件
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static void send(MimeMessageHelper messageHelper) {
		Assert.notNull(messageHelper, "messageHelper must not be null");
		mailSender.send(messageHelper.getMimeMessage());
	}

	/**
	 * 添加附件
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static void attach(MimeMessageHelper messageHelper, String attachmentName, byte[] attachment) {
		try {
			messageHelper.addAttachment(attachmentName, new ByteArrayResource(attachment));
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加附件
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static void attach(MimeMessageHelper messageHelper, String attachmentName, CircularByteBuffer buffer) {
		try {
			messageHelper.addAttachment(attachmentName, new ByteArrayResource(buffer.getInputStream().readAllBytes()));
		} catch (MessagingException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加附件
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static void attach(MimeMessageHelper messageHelper, String attachmentName, ByteArrayOutputStream stream) {
		try {
			messageHelper.addAttachment(attachmentName, new ByteArrayResource(stream.toByteArray()));
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 发送邮件
	 * @author Frodez
	 * @date 2019-03-27
	 */
	public static void sendWithAttachments(String from, String to, String subject, String text, String... filePaths) {
		Assert.notNull(from, "from must not be null");
		Assert.notNull(to, "to must not be null");
		Assert.notNull(subject, "subject must not be null");
		Assert.notNull(text, "text must not be null");
		Assert.notNull(filePaths, "filePaths must not be null");
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);
			for (String filePath : filePaths) {
				FileSystemResource resource = new FileSystemResource(filePath);
				String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
				helper.addAttachment(fileName, resource);
			}
			mailSender.send(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
