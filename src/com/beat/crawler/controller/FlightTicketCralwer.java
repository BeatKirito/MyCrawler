package com.beat.crawler.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beat.crawler.domain.ft.CType;
import com.beat.crawler.domain.ft.PType;
import com.beat.crawler.domain.ft.Route;
import com.beat.crawler.service.CrawlerContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class FlightTicketCralwer
 */
public class FlightTicketCralwer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FlightTicketCralwer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		Gson gson = null;
		CrawlerContext cc = new CrawlerContext("ticket");

		//第一步：包装请求参数
		Route route =  packReParam(request);
		
		//第二步：使用爬虫业务，处理请求参数
		if(route != null) {
			cc.getResult(route); 
		}
		
		//第三步：输出经过处理的route对象（JSON格式）
		try {
			
			out = response.getWriter();
			
			//使用GsonBuilder来控制时间格式
			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
			
			//输出route对象的Json格式
			if(route != null) {
				out.print(gson.toJson(route));
			}
			else {
				out.print(""); 
			}
			
		} catch (Exception e) {
			System.out.println("Json数据输出错误：" + e);
			e.printStackTrace();
		}finally {
			if(out != null) {
				out.close();
			}
		}
		
	}
	
	/**
	 * 获取并包装客户端的请求参数
	 * @param request
	 * @return	返回被包装后的机票路线对象
	 */
	private Route packReParam(HttpServletRequest request) {
		
		Route route = new Route();
		
		
		try {
			
			route.setdCity(request.getParameter("dCity").toUpperCase()); 
			route.setaCity(request.getParameter("aCity").toUpperCase());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			route.setdDate(sdf.parse(request.getParameter("dDate")));
			
			route.setpNum(Integer.parseInt(request.getParameter("num")));
			
			String pType = request.getParameter("pType");
			
			switch (pType) {
			case "adu":
				route.setpType(PType.ADU); 
				break;
			case "chi":
				route.setpType(PType.CHI); 
				break;
			case "bab":
				route.setpType(PType.BAB); 
				break;
			default:
				route.setpType(PType.ADU);  //默认设置为成人票
				break;
			}
			
			String cType = request.getParameter("cType");
			
			switch (cType) {
			case "y":
				route.setcType(CType.Y);
				break;
			case "cf":
				route.setcType(CType.CF); 
				break;
			default:
				route.setcType(CType.Y); //默认设置为经济舱
				break;
			}
			
			
		} catch (Exception e) {
			System.out.println("请求参数包装出错：" + e);
			e.printStackTrace();
			return null;
		}
		
		return route;
	}

}
