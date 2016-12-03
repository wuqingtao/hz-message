package hz.message;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;

/**
 * Servlet服务
 * <p>
 * 基于Jetty嵌入式功能实现
 */
public class ServletServer {
	/**
	 * HTTP请求处理Handler
	 */
	private static class Handler extends AbstractHandler {
		/** mysql用户名 */
		private final String _user = "root";
		/** mysql用户密码 */
		private final String _password = "Ningning~1";
		/** mysql数据库名 */
		private final String _database = "message";

		/** 日志对象 */
		private Logger _logger;

		/**
		 * 构造函数
		 * <p>
		 * 创建日志对象
		 */
		public Handler() {
			// 获取该jar包的路径（带jar文件名）
			String classPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
			
			// 创建日志目录
			File loggerPath = new File(classPath.substring(0, classPath.lastIndexOf(File.separator) + 1) + "logs");
			loggerPath.mkdirs();
			
			// 设置日志目录至环境变量，用日志配置文件访问
			System.setProperty("log4j.file", loggerPath.getAbsolutePath());
			
			// 创建日志对象
			_logger = Logger.getLogger("message");
		}

		@Override
		public void handle(String target, Request baseRequest,
				HttpServletRequest request, HttpServletResponse response) throws IOException,
				ServletException {
			// 仅支持POST请求，否则返回HTTP 501
			if (!request.getMethod().equals("POST")) {
				response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
				baseRequest.setHandled(true);
				return;
			}
			
			try {
				// 获取请求参数
				String paramStr = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());

				// 将请求参数从string转换为json
				Object[] paramErr = Converter.str2json(paramStr);
				JSONObject param = (JSONObject) paramErr[0];
				JSONObject err = (JSONObject) paramErr[1];
				if (err != null) {
					// 如果转换失败，记录日志，返回错误
					String resultStr = Converter.json2str(err);
					_logger.info(String.format("req:%s res:%s", paramStr, resultStr));
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType("text/html; charset=utf-8");
					IOUtils.write(resultStr, response.getOutputStream(), "utf-8");
					baseRequest.setHandled(true);
					return;
				}
								
				// 创建Message对象
				Message message = MessageCreator.createMessageByMysql(_user, _password, _database);
				
				// 处理请求
				JSONObject result = message.request(param);
				
				// 关闭message对象
				message.close();
				
				// 将处理结果从json转换为string，记录日志，返回
				String resultStr = Converter.json2str(result);
				_logger.info(String.format("req:%s res:%s", paramStr, resultStr));
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("text/html; charset=utf-8");
				IOUtils.write(resultStr, response.getOutputStream(), "utf-8");
				baseRequest.setHandled(true);
			} catch (Exception e) {
				// 如果处理结果出现异常，记录异常日志，返回HTTP 500
				_logger.error(e, e);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				baseRequest.setHandled(true);
				return;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// 解析命令行端口参数
		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		options.addOption("p", "port", true, "Listening port");
		CommandLine commandLine = parser.parse(options, args);
		int port = Integer.parseInt(commandLine.getOptionValue('p'));
		
		// 创建Server，监听指定端口
		Server server = new Server(port);
		
		// 设置HTTP请求处理Handler
		server.setHandler(new Handler());
		
		// 启动服务
		server.start();
		
		// 等待退出
		server.join();
	}
}
