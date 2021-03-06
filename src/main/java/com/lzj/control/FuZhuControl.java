package com.lzj.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lzj.bean.StockBkDataDay;
import com.lzj.bean.StockDataDay;
import com.lzj.bean.StockZsDataDay;
import com.lzj.dao.StockBkDataDayDao;
import com.lzj.dao.StockDataDayDao;
import com.lzj.dao.StockZsDataDayDao;
import com.lzj.util.ColorUtil;
import com.lzj.util.DateUtil;

@Controller
@RequestMapping("/fz")
public class FuZhuControl {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值
	}

	@RequestMapping("/hushenagu")
	public String hushenagu(HttpServletRequest request) {
		return "/bootstrap/hushenagu";
	}
	
	@RequestMapping("/zhangdiefufenbu")
	public String zhangdiefufenbu(HttpServletRequest request) {
		return "/bootstrap/zhangdiefufenbu";
	}
	
	@RequestMapping("/hushenagu_lishi")
	public String aguLishi(HttpServletRequest request, Date queryDate) {

		StockBkDataDayDao stockBkDataDayDao = new StockBkDataDayDao();

		if (queryDate == null) {
			queryDate = new Date();
		}

		String currentDate = DateUtil.formatDate(queryDate, "yyyy-MM-dd");

		String preDate = stockBkDataDayDao.getPreDateByCreateDate(currentDate);
		String nextDate = stockBkDataDayDao.getNextDateByCreateDate(currentDate);

		StockDataDayDao stockDataDayDao = new StockDataDayDao();
		List<StockDataDay> stockDataDays = stockDataDayDao.getByCreateDate(queryDate);

		request.setAttribute("stockDataDays", stockDataDays);
		request.setAttribute("preDate", preDate);
		request.setAttribute("nextDate", nextDate);
		// return "redirect:" + "/bootstrap/hushenagu_lishi.jsp";
		return "bootstrap/hushenagu_lishi";
	}

	@RequestMapping("/hushenbk_lishi")
	public String bkLishi(HttpServletRequest request, Date queryDate) {
		StockBkDataDayDao stockBkDataDayDao = new StockBkDataDayDao();

		String currentDate = DateUtil.formatDate(queryDate, "yyyy-MM-dd");
		String preDate = stockBkDataDayDao.getPreDateByCreateDate(currentDate);
		String nextDate = stockBkDataDayDao.getNextDateByCreateDate(currentDate);

		if (queryDate == null) {
			queryDate = new Date();
			while (true) {
				queryDate = DateUtil.addDay(queryDate, -1);

				Calendar cal = Calendar.getInstance();
				cal.setTime(queryDate);

				int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
				if (w >= 1 && w <= 5) {
					break;
				}
			}

		}
		List<StockBkDataDay> stockBkDataDays = stockBkDataDayDao.getByCreateDate(queryDate);

		request.setAttribute("stockBkDataDays", stockBkDataDays);
		request.setAttribute("preDate", preDate);
		request.setAttribute("nextDate", nextDate);
		// return "redirect:" + "/bootstrap/hushenagu_lishi.jsp";
		return "/bootstrap/hushenbk_lishi";
	}

	/**
	 * 板块曲线比较
	 * 
	 * @param request
	 * @param queryDate
	 * @return
	 */
	@RequestMapping("/bkduibi_line")
	public String bkBiJiaoLine(HttpServletRequest request, String bkType) {
		request.setAttribute("bkType", bkType);
		return "/bootstrap/bkduibi_line";
	}
	
	/**
	 * 指数板块曲线比较
	 * 
	 * @param request
	 * @param queryDate
	 * @return
	 */
	@RequestMapping("/zsduibi_line")
	public String zsBiJiaoLine(HttpServletRequest request, String bkType) {
		request.setAttribute("bkType", bkType);
		return "/bootstrap/zsduibi_line";
	}
	
	@RequestMapping("/reloadStockData")
	public String reloadStockData(HttpServletRequest request) throws Exception {
		StockDataDayDao stockDataDayDao = new StockDataDayDao();
		String filePath = this.getClass().getResource("/mytbl.txt").getPath().toString();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath), "UTF-8"));
		String line = "";
		
		while ((line = br.readLine()) != null) {
			String[] datas = line.split("	");
			StockDataDay stockDataDay = new StockDataDay();
			stockDataDay.setB(datas[1]);
			stockDataDay.setC(datas[2]);
			stockDataDay.setD(Double.parseDouble(datas[3]));
			stockDataDay.setE(Double.parseDouble(datas[4]));
			stockDataDay.setR(Double.parseDouble(datas[5]));
			stockDataDay.setP(Double.parseDouble(datas[6]));
			stockDataDay.setQ(Double.parseDouble(datas[7]));
			stockDataDay.setS(Double.parseDouble(datas[8]));
			stockDataDay.setO(Double.parseDouble(datas[9]));
			stockDataDay.setM(Double.parseDouble(datas[10]));
			stockDataDay.setM5(Double.parseDouble(datas[11]));
			stockDataDay.setM10(Double.parseDouble(datas[12]));
			stockDataDay.setM20(Double.parseDouble(datas[13]));
			stockDataDay.setM30(Double.parseDouble(datas[14]));
			stockDataDay.setM60(Double.parseDouble(datas[15]));
			stockDataDay.setM120(Double.parseDouble(datas[16]));
			stockDataDay.setM250(Double.parseDouble(datas[17]));
			stockDataDay.setCreateDate(DateUtil.str2Date(datas[18],"yyyy-MM-dd"));
			stockDataDayDao.addStockDataDay(stockDataDay );
		}
		br.close();
		return "/bootstrap/zhangdiefufenbu";
	}
	
	
	@RequestMapping("/reloadBkStockData")
	public String reloadBkStockData(HttpServletRequest request) throws Exception {
		StockBkDataDayDao stockBkDataDayDao = new StockBkDataDayDao();
		String filePath = this.getClass().getResource("/mybktbl.txt").getPath().toString();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath), "UTF-8"));
		String line = "";
		
		while ((line = br.readLine()) != null) {
			String[] datas = line.split("	");
			StockBkDataDay stockBkDataDay = new StockBkDataDay();
			stockBkDataDay.setB(datas[1]);
			stockBkDataDay.setC(datas[2]);
			stockBkDataDay.setD(Double.parseDouble(datas[3]));
			stockBkDataDay.setE(Double.parseDouble(datas[4]));
			stockBkDataDay.setR(Double.parseDouble(datas[5]));
			stockBkDataDay.setP(Double.parseDouble(datas[6]));
			stockBkDataDay.setQ(Double.parseDouble(datas[7]));
			stockBkDataDay.setS(Double.parseDouble(datas[8]));
			stockBkDataDay.setO(Double.parseDouble(datas[9]));
			stockBkDataDay.setM(Double.parseDouble(datas[10]));
			stockBkDataDay.setM5(Double.parseDouble(datas[11]));
			stockBkDataDay.setM10(Double.parseDouble(datas[12]));
			stockBkDataDay.setM20(Double.parseDouble(datas[13]));
			stockBkDataDay.setM30(Double.parseDouble(datas[14]));
			stockBkDataDay.setM60(Double.parseDouble(datas[15]));
			stockBkDataDay.setM120(Double.parseDouble(datas[16]));
			stockBkDataDay.setM250(Double.parseDouble(datas[17]));
			stockBkDataDay.setCreateDate(DateUtil.str2Date(datas[18],"yyyy-MM-dd"));
			stockBkDataDayDao.addStockBkDataDay(stockBkDataDay );
		}
		br.close();
		return "/bootstrap/zhangdiefufenbu";
	}
	
	@RequestMapping(value = "/bkduibi_line_data", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String bkBiJiaoLineData(HttpServletRequest request, String bkType) {
		StockBkDataDayDao stockBkDataDayDao = new StockBkDataDayDao();
		Map<String,String> codeAndType = new HashMap<String,String>();
		codeAndType.put("8802", "地区板块");
		codeAndType.put("8803", "行业板块1");
		codeAndType.put("8804", "行业板块2");
		codeAndType.put("8805", "概念板块1");
		codeAndType.put("8809", "概念板块2");
		codeAndType.put("8808", "风格板块");
		List<String> bkCodeList = stockBkDataDayDao.getBkCodeList(bkType);
		Date createDate = new Date();
		createDate = DateUtil.addDay(createDate, -60);

		Map<String, List<StockBkDataDay>> stockBkDataDayMap = new HashMap<String, List<StockBkDataDay>>();
		for (String bkCode : bkCodeList) {
			stockBkDataDayMap.put(bkCode, stockBkDataDayDao.getLineByBkCode(createDate, bkCode));
		}

		StringBuffer jsonArray = new StringBuffer();
		try {
			// { "elements": [ { "type": "line", "values": [ 1, 2, 1, null,
			// null, null, null, null ] } ], "title": { "text": "Sat Mar 07
			// 2015" } }

			jsonArray.append("{ \"elements\": [ ");

			int maxValue = 0;
			int minValue = 0;

			int maxCount = 0;

			for (String bkCode : stockBkDataDayMap.keySet()) {
				List<StockBkDataDay> bkDataDays = stockBkDataDayMap.get(bkCode);
				maxCount = Math.max(maxCount, bkDataDays.size());
			}

			int count = 0;
			for (String bkCode : stockBkDataDayMap.keySet()) {
				String colour = ColorUtil.getRandColorCode();
				count++;
				List<StockBkDataDay> bkDataDays = stockBkDataDayMap.get(bkCode);
				int offsetCount = maxCount - bkDataDays.size();
				for (int i = 0; i < offsetCount; i++) {
					StockBkDataDay bkDataDay = new StockBkDataDay();
					bkDataDay.setE(0);
					bkDataDays.add(0, bkDataDay);
				}
				double totalE = 0;
				String nameText = bkDataDays.get(bkDataDays.size() - 1).getC();
				jsonArray.append("{ \"type\": \"line\",\"text\":\"" + nameText + "\", \"values\": [");
				for (int i = 0; i < bkDataDays.size(); i++) {

					StockBkDataDay bkDataDay = bkDataDays.get(i);
					double e = bkDataDay.getE();
					if (e == -99.99) {
						e = 0;
					}
					totalE = totalE + e;
					maxValue = Math.max(maxValue, (int) totalE);
					minValue = Math.min(minValue, (int) totalE);
					if (i == (bkDataDays.size() - 1)) {
						//jsonArray.append(String.format("%.2f",totalE));
						jsonArray.append("{\"value\":"+String.format("%.2f",totalE)+",\"tip\":\""+nameText+"#val#\"}");
						//{"value":7, "colour":"#FF0000", "tip":"LINE<br>#val#", "dot-size":12, "halo-size": 3 }
					} else {
						jsonArray.append("{\"value\":"+String.format("%.2f",totalE)+",\"tip\":\""+nameText+"#val#\"}").append(",");
						//jsonArray.append(String.format("%.2f",totalE)).append(",");
					}
				}

				jsonArray
						.append(" ], \"dot-style\": { \"type\": \"hollow-dot\", \"dot-size\": 4, \"halo-size\": 1, \"colour\": \"#"
								+ colour + "\" },\"colour\": \"#" + colour + "\"}");
				if (count != (stockBkDataDayMap.size())) {
					jsonArray.append(",");
				}
			}

			jsonArray.append("]");
			jsonArray.append(",\"title\": {\"text\": \""+codeAndType.get(bkType)+"\" },");
			jsonArray.append("\"x_legend\": { \"text\": \"日期\", \"style\": \"{font-size: 12px; color: #778877}\" }, ");
			jsonArray.append(
					"\"y_axis\": { \"min\": " + (minValue - 2) + ", \"max\": " + (maxValue + 2) + ", \"steps\": 5 } }");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "/zsduibi_line_data", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String zsBiJiaoLineData(HttpServletRequest request, String bkType) {
		StockZsDataDayDao stockZsDataDayDao = new StockZsDataDayDao();
		Map<String,String> codeAndType = new HashMap<String,String>();
		
		codeAndType.put("8888", "指数板块");
		
		List<String> bkCodeList = stockZsDataDayDao.getZsCodeList();
		Date createDate = new Date();
		createDate = DateUtil.addDay(createDate, -60);

		Map<String, List<StockZsDataDay>> stockZsDataDayMap = new HashMap<String, List<StockZsDataDay>>();
		for (String bkCode : bkCodeList) {
			stockZsDataDayMap.put(bkCode, stockZsDataDayDao.getLineByZsCode(createDate, bkCode));
		}

		StringBuffer jsonArray = new StringBuffer();
		try {
			// { "elements": [ { "type": "line", "values": [ 1, 2, 1, null,
			// null, null, null, null ] } ], "title": { "text": "Sat Mar 07
			// 2015" } }

			jsonArray.append("{ \"elements\": [ ");

			int maxValue = 0;
			int minValue = 0;

			int maxCount = 0;

			for (String bkCode : stockZsDataDayMap.keySet()) {
				List<StockZsDataDay> zsDataDays = stockZsDataDayMap.get(bkCode);
				maxCount = Math.max(maxCount, zsDataDays.size());
			}

			int count = 0;
			for (String bkCode : stockZsDataDayMap.keySet()) {
				String colour = ColorUtil.getRandColorCode();
				count++;
				List<StockZsDataDay> zsDataDays = stockZsDataDayMap.get(bkCode);
				int offsetCount = maxCount - zsDataDays.size();
				for (int i = 0; i < offsetCount; i++) {
					StockZsDataDay zsDataDay = new StockZsDataDay();
					zsDataDay.setE(0);
					zsDataDays.add(0, zsDataDay);
				}
				double totalE = 0;
				String nameText = zsDataDays.get(zsDataDays.size() - 1).getC();
				jsonArray.append("{ \"type\": \"line\",\"text\":\"" + nameText + "\", \"values\": [");
				for (int i = 0; i < zsDataDays.size(); i++) {

					StockZsDataDay bkDataDay = zsDataDays.get(i);
					double e = bkDataDay.getE();
					if (e == -99.99) {
						e = 0;
					}
					totalE = totalE + e;
					maxValue = Math.max(maxValue, (int) totalE);
					minValue = Math.min(minValue, (int) totalE);
					if (i == (zsDataDays.size() - 1)) {
						//jsonArray.append(String.format("%.2f",totalE));
						jsonArray.append("{\"value\":"+String.format("%.2f",totalE)+",\"tip\":\""+nameText+"#val#\"}");
						//{"value":7, "colour":"#FF0000", "tip":"LINE<br>#val#", "dot-size":12, "halo-size": 3 }
					} else {
						jsonArray.append("{\"value\":"+String.format("%.2f",totalE)+",\"tip\":\""+nameText+"#val#\"}").append(",");
						//jsonArray.append(String.format("%.2f",totalE)).append(",");
					}
				}

				jsonArray
						.append(" ], \"dot-style\": { \"type\": \"hollow-dot\", \"dot-size\": 4, \"halo-size\": 1, \"colour\": \"#"
								+ colour + "\" },\"colour\": \"#" + colour + "\"}");
				if (count != (stockZsDataDayMap.size())) {
					jsonArray.append(",");
				}
			}

			jsonArray.append("]");
			jsonArray.append(",\"title\": {\"text\": \""+codeAndType.get(bkType)+"\" },");
			jsonArray.append("\"x_legend\": { \"text\": \"日期\", \"style\": \"{font-size: 12px; color: #778877}\" }, ");
			jsonArray.append(
					"\"y_axis\": { \"min\": " + (minValue - 2) + ", \"max\": " + (maxValue + 2) + ", \"steps\": 5 } }");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray.toString();
	}
	
public static void main(String[] args) {
	System.out.println(String.format("%.2f", 0.1212125454d));
	 DecimalFormat df = new DecimalFormat("#.00");  
     System.out.println(df.format(0.1212125454d));  

}
}