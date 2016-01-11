package com.csit.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.csit.model.ReportConfig;
import com.csit.service.ReportConfigService;
import com.csit.util.GenXmlData;

/**
 * @Description:报表生成Servlet
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-23
 * @author lys
 * @vesion 1.0
 */
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 5483286765910301562L;

	public ReportServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String kind = request.getParameter("kind");
		String reportCode = request.getParameter("reportCode");
		ApplicationContext ac = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		ReportConfigService reportConfigService = (ReportConfigService) ac
				.getBean("reportConfigServiceImpl");
		if ("save".equals(kind)) {
			int DataLen = request.getContentLength();
			if (DataLen > 0) {
				final int BufSize = 1024; // 一次读写数据的缓冲区大小

				// 打开写入文件
				String FileName = request.getSession().getServletContext()
						.getRealPath("/report")
						+ File.separator + reportCode + ".grf";
				FileOutputStream fos = new FileOutputStream(FileName);

				// 注意：要分批读写，不然在某些条件下对大数据(>8K)模板保存不成功
				// 读出客户端发送的数据，并写入文件流
				byte[] DataBuf = new byte[BufSize];
				ServletInputStream sif = request.getInputStream();
				int TotalReadedLen = 0;
				while (DataLen > TotalReadedLen) {
					int TheReadedlen = sif.read(DataBuf, 0, BufSize);
					if (TheReadedlen <= 0)
						break;

					fos.write(DataBuf, 0, TheReadedlen);

					TotalReadedLen += TheReadedlen;
				}

				fos.close();
			}
		} else {
			ReportConfig reportConfig = reportConfigService
					.getByReportCode(reportCode);
			String parameterData = GenXmlData.GenReportParameterData(createSql(
					request, reportConfig.getReportParamsSql()));
			response.setCharacterEncoding("utf-8");
			GenXmlData.GenFullReportData(response,
					createSql(request, reportConfig.getReportDetailSql()),
					parameterData, false);
		}
	}

	public void init() throws ServletException {

	}

	/**
	 * @Description: 生成Sql
	 * @Create: 2013-1-23 下午3:10:39
	 * @author lys
	 * @update logs
	 * @param request
	 * @param sql
	 * @return
	 */
	private String createSql(HttpServletRequest request, String sql) {
		if (StringUtils.isEmpty(sql)) {
			return null;
		}
		Pattern p = Pattern.compile("@(\\w+)[,,)]", Pattern.CASE_INSENSITIVE);// 正则表达式，后面的参数指定忽略大小写
		Matcher m = p.matcher(sql);// 匹配的字符串
		while (m.find())//
		{
			String param = m.group(1);
			sql = sql.replace("@" + param, "'%s'");
			String value = request.getParameter(param);
			if (value == null) {
				value = "";
			}
			sql = String.format(sql, value);
		}
		return sql;
	}

}
