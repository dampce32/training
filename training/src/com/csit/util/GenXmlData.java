package com.csit.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;
import java.util.zip.DeflaterOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GenXmlData {
	public static String driver = null;   
	public static String url = null;  
	public static String user = null;   
	public static String password = null;
	
	static{
		Properties pro = new Properties();
		try {
			pro.load(GenXmlData.class.getClassLoader().getResourceAsStream("application.properties"));
			driver = pro.getProperty("jdbc.driverClassName");
			url = pro.getProperty("jdbc.url");
			user = pro.getProperty("jdbc.username");
			password = pro.getProperty("jdbc.password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//方法简要说明
	//1. GenNodeXmlData：产生报表需要的XML节点类型数据
	//2. GenFullReportData：根据RecordsetQuerySQL产生提供给报表生成需要的XML数据，并同时将ParameterPart中的报表参数数据一起打包，参数ToCompress指定是否压缩数据 
	//3. GenReportParameterData：根据ParameterQuerySQL获取的报表参数数据一起打包 
	            
	/////////////////////////////////////////////////////////////////////////////////////////
	//产生报表需要的XML节点类型数据，节点类型数据产生数据量比属性类型数据的要大
	public static void GenNodeXmlData(HttpServletResponse response, String QuerySQL, boolean ToCompress)
	{
	    try
	    {
	        try
	        {
	            response.resetBuffer();
	            
	            Class.forName(driver); // Class.forName 装载驱动程序 
	            Connection con=DriverManager.getConnection(url, user, password); //用适当的驱动程序类与 DBMS 建立一个连接
	            
	            Statement stmt=con.createStatement();                         //用于发送简单的SQL语句
	            ResultSet rs=stmt.executeQuery(QuerySQL);

	            ResultSetMetaData rsmd = rs.getMetaData();
	            int ColCount = rsmd.getColumnCount();
	            
	            StringBuffer XmlText = new StringBuffer ("<xml>\n");
	            
	            while( rs.next() ) 
	            {
	                XmlText.append("<row>");
	                for (int i=1; i<=ColCount; i++)
	                {
	                    XmlText.append("<");
	                    XmlText.append(rsmd.getColumnLabel(i)); //getColumnName
	                    XmlText.append(">");
	                    
	                    int ColType = rsmd.getColumnType(i);
	                    if (ColType == Types.LONGVARBINARY || ColType == Types.VARBINARY || ColType == Types.BINARY || ColType == Types.BLOB)
	                    {
	                        byte[] BinData = rs.getBytes(i);
	                        if ( !rs.wasNull() )
	                            XmlText.append( (new sun.misc.BASE64Encoder()).encode( BinData ) );
	                    }
	                    else
	                    {
	                        String Val = rs.getString(i);
	                        if ( !rs.wasNull() )
	                        {
	                            if ( HasSpecialChar(Val) )
	                                XmlText.append( HTMLEncode(Val) );
	                            else
	                                XmlText.append(Val);
	                        }
	                    }
	                    
	                    XmlText.append("</");
	                    XmlText.append(rsmd.getColumnLabel(i)); //getColumnName
	                    XmlText.append(">");
	                }
	                XmlText.append("</row>\n");
			    }
	            rs.close();
	            stmt.close();
	            con.close();
	            
	            XmlText.append("</xml>\n");
	            
	            if ( ToCompress )
		        {
	                byte[] RawData = XmlText.toString().getBytes();
	                
		            //写入特有的压缩头部信息，以便报表客户端插件能识别数据
	                response.addHeader("gr_zip_type", "deflate");                                      //指定压缩方法
	                response.addIntHeader("gr_zip_size", RawData.length);                    //指定数据的原始长度
	                response.addHeader("gr_zip_encode", response.getCharacterEncoding());   //指定数据的编码方式 utf-8 utf-16 ...
	        	
		            //压缩数据并输出
	                ServletOutputStream bos = response.getOutputStream();
	                DeflaterOutputStream zos = new DeflaterOutputStream(bos);
	                zos.write(RawData);
	                zos.close();
	                bos.flush();
		        }
		        else
		        {
	                PrintWriter pw = response.getWriter();
	                pw.print(XmlText.toString());
	                pw.close();  //终止后续不必要内容输出
	            }
	        }
	        catch(Exception e)
	        {
	            //output error message
	            PrintWriter pw = response.getWriter();
	            pw.print(e.toString());
	        }
	    }
	    catch(Exception e)
	    {
	    }
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	//产生报表需要的XML节点类型数据
	//根据RecordsetQuerySQL产生提供给报表生成需要的XML数据，并同时将ParameterPart中的报表参数数据一起打包，参数ToCompress指定是否压缩数据
	public static void GenFullReportData(HttpServletResponse response, String QuerySQL, String ParameterPart, boolean ToCompress)
	{
	    try
	    {
	        try
	        {
	            response.resetBuffer();
	            
	            Class.forName(driver); // Class.forName 装载驱动程序 
	            Connection con=DriverManager.getConnection(url, user, password); //用适当的驱动程序类与 DBMS 建立一个连接
	            
	            Statement stmt=con.createStatement();                         //用于发送简单的SQL语句
	            ResultSet rs=stmt.executeQuery(QuerySQL);

	            ResultSetMetaData rsmd = rs.getMetaData();
	            int ColCount = rsmd.getColumnCount();
	            
	            //StringBuffer XmlText = new StringBuffer ("<xml>\n");
	            StringBuffer XmlText = new StringBuffer("<report>\n<xml>\n");
	            
	            while( rs.next() ) 
	            {
	                XmlText.append("<row>");
	                for (int i=1; i<=ColCount; i++)
	                {
	                    XmlText.append("<");
	                    XmlText.append(rsmd.getColumnLabel(i)); //getColumnName
	                    XmlText.append(">");
	                    
	                    int ColType = rsmd.getColumnType(i);
	                    if (ColType == Types.LONGVARBINARY || ColType == Types.VARBINARY || ColType == Types.BINARY || ColType == Types.BLOB)
	                    {
	                        byte[] BinData = rs.getBytes(i);
	                        if ( !rs.wasNull() )
	                            XmlText.append( (new sun.misc.BASE64Encoder()).encode( BinData ) );
	                    }
	                    else
	                    {
	                        String Val = rs.getString(i);
	                        if ( !rs.wasNull() )
	                        {
	                            if ( HasSpecialChar(Val) )
	                                XmlText.append( HTMLEncode(Val) );
	                            else
	                                XmlText.append(Val);
	                        }
	                    }
	                    
	                    XmlText.append("</");
	                    XmlText.append(rsmd.getColumnLabel(i)); //getColumnName
	                    XmlText.append(">");
	                }
	                XmlText.append("</row>\n");
			    }
	            rs.close();
	            stmt.close();
	            con.close();
	            
	            XmlText.append("</xml>\n");
	            XmlText.append(ParameterPart);
	            XmlText.append("</report>");
	            
	            if ( ToCompress )
		        {
	                byte[] RawData = XmlText.toString().getBytes();
	                
		            //写入特有的压缩头部信息，以便报表客户端插件能识别数据
	                response.addHeader("gr_zip_type", "deflate");                                      //指定压缩方法
	                response.addIntHeader("gr_zip_size", RawData.length);                    //指定数据的原始长度
	                response.addHeader("gr_zip_encode", response.getCharacterEncoding());   //指定数据的编码方式 utf-8 utf-16 ...
	        	
		            //压缩数据并输出
	                ServletOutputStream bos = response.getOutputStream();
	                DeflaterOutputStream zos = new DeflaterOutputStream(bos);
	                zos.write(RawData);
	                zos.close();
	                bos.flush();
		        }
		        else
		        {
	                PrintWriter pw = response.getWriter();
	                pw.print(XmlText.toString());
	                pw.close();  //终止后续不必要内容输出
	            }
	        }
	        catch(Exception e)
	        {
	            //output error message
	            PrintWriter pw = response.getWriter();
	            pw.print(e.toString());
	        }
	    }
	    catch(Exception e)
	    {
	    }
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	//产生报表参数的XML节点类型数据
	//根据ParameterQuerySQL获取的报表参数数据一起打包
	public static String GenReportParameterData(String ParameterQuerySQL)
	{
	    StringBuffer XmlText = new StringBuffer ("<_grparam>\n");
	    
	    try
	    {
	        Class.forName(driver); // Class.forName 装载驱动程序 
	        Connection con=DriverManager.getConnection(url, user, password); //用适当的驱动程序类与 DBMS 建立一个连接
	        
	        Statement stmt=con.createStatement();                         //用于发送简单的SQL语句
	        ResultSet rs=stmt.executeQuery(ParameterQuerySQL);

	        ResultSetMetaData rsmd = rs.getMetaData();
	        rs.next();
	        
	        int ColCount = rsmd.getColumnCount();
	        for (int i=1; i<=ColCount; i++)
	        {
	            XmlText.append("<");
	            XmlText.append(rsmd.getColumnLabel(i)); //getColumnName
	            XmlText.append(">");
	            
	            int ColType = rsmd.getColumnType(i);
	            if (ColType == Types.LONGVARBINARY || ColType == Types.VARBINARY || ColType == Types.BINARY || ColType == Types.BLOB)
	            {
	                byte[] BinData = rs.getBytes(i);
	                if ( !rs.wasNull() )
	                    XmlText.append( (new sun.misc.BASE64Encoder()).encode( BinData ) );
	            }
	            else
	            {
					String Val = rs.getString(i);
					if ( !rs.wasNull() )
					{
						if ( HasSpecialChar(Val) )
							XmlText.append( HTMLEncode(Val) );
						else
							XmlText.append(Val);
					}
				}
	            
	            XmlText.append("</");
	            XmlText.append(rsmd.getColumnLabel(i)); //getColumnName
	            XmlText.append(">\n");
	        }
	            
	        rs.close();
	        stmt.close();
	        con.close();
	    }
	    catch(Exception e)
	    {
	        //output error message
	        XmlText.append(e.toString());
	    }
	    
	    XmlText.append("</_grparam>\n");
	    
	    return XmlText.toString();
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	//产生报表参数的XML节点类型数据，并响应给客户端
	public static void GenParameterXmlData(HttpServletResponse response, String QuerySQL)
	{
	    try
	    {
	        response.resetBuffer();
	        
	        StringBuffer XmlText = new StringBuffer("<report>\n");
	  		String ParameterPart = GenReportParameterData(QuerySQL);
	        XmlText.append(ParameterPart);
	        XmlText.append("</report>");
			
	        PrintWriter pw = response.getWriter();
	        pw.print(XmlText.toString());
	        pw.close();  //终止后续不必要内容输出
	    }
	    catch(Exception e)
	    {
	    }
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	//分批取数: 产生报表需要的XML节点类型数据
	//参数 SessionItemName 指定在 Session 中记录 ResultSet 对象的名称，应该保证每个报表用不同的名称
	//参数 QuerySQL 指定获取报表数据的查询SQL
	//参数 StartNo 指定本次取数的第一条记录的序号，从0开始；当为0时，表示是取第一批次的数据
	//参数 WantRows 指定本次取数希望获取的记录条数，当为0时，自动按100获取
	//参数 ToCompress 指定是否对XML数据进行压缩
	public static void BatchGenXmlData(HttpServletResponse response, HttpSession session, String SessionItemName, String QuerySQL, int StartNo, int WantRows, boolean ToCompress)
	{
	    try
	    {
	        try
	        {
	            response.resetBuffer();
	            
	            //首先从Session中获取ResultSet，如果不存在，则进行创建打开
	            ResultSet rs = (ResultSet)session.getAttribute(SessionItemName);
	            if (rs == null)
	            {
					Class.forName(driver); // Class.forName 装载驱动程序 
					Connection con=DriverManager.getConnection(url, user, password); //用适当的驱动程序类与 DBMS 建立一个连接
	            
					Statement stmt=con.createStatement();                         //用于发送简单的SQL语句
					rs=stmt.executeQuery(QuerySQL);

					session.setAttribute(SessionItemName, rs);
	            }
	            
	            ResultSetMetaData rsmd = rs.getMetaData();
	            int ColCount = rsmd.getColumnCount();
	            
	            StringBuffer XmlText = new StringBuffer("<xml>\n");
	            
	            rs.beforeFirst();
	            rs.relative(StartNo);
	            int ReadedCount = 0;
	            while(rs.next() && ReadedCount<WantRows) 
	            {
	                XmlText.append("<row>");
	                for (int i=1; i<=ColCount; i++)
	                {
	                    XmlText.append("<");
	                    XmlText.append(rsmd.getColumnLabel(i)); //getColumnName
	                    XmlText.append(">");
	                    
	                    int ColType = rsmd.getColumnType(i);
	                    if (ColType == Types.LONGVARBINARY || ColType == Types.VARBINARY || ColType == Types.BINARY || ColType == Types.BLOB)
	                    {
	                        byte[] BinData = rs.getBytes(i);
	                        if ( !rs.wasNull() )
	                            XmlText.append( (new sun.misc.BASE64Encoder()).encode( BinData ) );
	                    }
	                    else
	                    {
	                        String Val = rs.getString(i);
	                        if ( !rs.wasNull() )
	                        {
	                            if ( HasSpecialChar(Val) )
	                                XmlText.append( HTMLEncode(Val) );
	                            else
	                                XmlText.append(Val);
	                        }
	                    }
	                    
	                    XmlText.append("</");
	                    XmlText.append(rsmd.getColumnLabel(i)); //getColumnName
	                    XmlText.append(">");
	                }
	                XmlText.append("</row>\n");
	                
	                ++ReadedCount;
			    }
			    
				//如果没有取得数据，说明数据已经取完，则清理掉Session中记录的数据
	            if (ReadedCount == 0)
	            {
					rs.close();
					//stmt.close();
					//con.close();
					
					session.removeAttribute(SessionItemName);
	            }
	            
	            XmlText.append("</xml>\n");
	            
	            if ( ToCompress )
		        {
	                byte[] RawData = XmlText.toString().getBytes();
	                
		            //写入特有的压缩头部信息，以便报表客户端插件能识别数据
	                response.addHeader("gr_zip_type", "deflate");                                      //指定压缩方法
	                response.addIntHeader("gr_zip_size", RawData.length);                    //指定数据的原始长度
	                response.addHeader("gr_zip_encode", response.getCharacterEncoding());   //指定数据的编码方式 utf-8 utf-16 ...
	        	
		            //压缩数据并输出
	                ServletOutputStream bos = response.getOutputStream();
	                DeflaterOutputStream zos = new DeflaterOutputStream(bos);
	                zos.write(RawData);
	                zos.close();
	                bos.flush();
		        }
		        else
		        {
	                PrintWriter pw = response.getWriter();
	                pw.print(XmlText.toString());
	                pw.close();  //终止后续不必要内容输出
	            }
	        }
	        catch(Exception e)
	        {
	            //output error message
	            PrintWriter pw = response.getWriter();
	            pw.print(e.toString());
	        }
	    }
	    catch(Exception e)
	    {
	    }
	}

	//获取 Count(*) SQL 查询到的数据行数
	//参数 QuerySQL 指定获取报表数据的查询SQL
	public static int BatchGetDataCount(String QuerySQL)
	{
	    int Total = 0;
	    try
	    {
	        Class.forName(driver); // Class.forName 装载驱动程序 
	        Connection con=DriverManager.getConnection(url, user, password); //用适当的驱动程序类与 DBMS 建立一个连接
	        Statement stmt=con.createStatement();                         //用于发送简单的SQL语句
	        ResultSet rs=stmt.executeQuery(QuerySQL);

	        if  ( rs.next() )
				Total = rs.getInt(1);
	            
	        rs.close();
	        con.close();
	    }
	    catch(Exception e)
	    {
	    }
		return Total;
	}
	
	
	//判断是否包含特殊字符
	public static boolean HasSpecialChar(String text)
	{
	    if (text == null) 
	        return false;
	    
	    boolean ret = false;     
	    int len = text.length();
	    for (int i = 0; i < len; ++i)
	    {
	        char c = text.charAt(i);
	        if (c == '&' ||  c == '<' || c == '>' || c == '"')
	        {
	            ret = true;
	            break;
	        }
	    }
	    
	    return ret;
	}

	//对数据中的特殊字符进行编码
	public static String HTMLEncode(String text)
	{
	    int len = text.length();
	    StringBuffer results = new StringBuffer(len + 20);
	    char[] orig = text.toCharArray();
	    
	    int beg = 0;
	    for (int i = 0; i < len; ++i)
	    {
	        char c = text.charAt(i);
	        switch (c) 
	        {
	            case '&':
	                if (i > beg) 
	                    results.append(orig, beg, i - beg);
	                beg = i + 1;
	                results.append("&amp;");
	                break;
	            case '<':
	                if (i > beg) 
	                    results.append(orig, beg, i - beg);
	                beg = i + 1;
	                results.append("&lt;");
	                break;
	            case '>':
	                if (i > beg) 
	                    results.append(orig, beg, i - beg);
	                beg = i + 1;
	                results.append("&gt;");
	                break;
	            case '"':
	                if (i > beg) 
	                    results.append(orig, beg, i - beg);
	                beg = i + 1;
	                results.append("&quot;");
	                break;
	        }
	    }
	    
	    results.append(orig, beg, len - beg);
	    
	    return results.toString();
	}

}
