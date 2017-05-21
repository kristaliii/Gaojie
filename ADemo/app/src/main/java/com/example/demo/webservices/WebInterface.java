package com.example.demo.webservices;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class WebInterface {
	
	private static String targetNameSpace = "http://www.dicpweb.com/"; // wsdl文档 命名空间
//	private static String soapAction = "http://www.hxstudio.com/DicpCommonService/DICP_Call"; //命名空间 +契约名称+ 方法名
	private static String serviceURL = "http://222.89.135.25:8721/DicpWebService.asmx"; // 指明 wsdl 文档URL
	
	private static String Dicp_Call = "Dicp_Call"; // 需要调用 de webservice方法名
	private static SoapObject soapObject;
	private static SoapSerializationEnvelope envelope;
	
	/**
	 * 登录  gps 上传 的服务接口
	 * @param args json格式的字符串
	 */
	// 同步一下，在于共享数据的安全问题，没有则在多次点击登陆时抛出空指针异常
	public  static synchronized String Dicp_Call(String args) { 
		soapObject = new SoapObject(targetNameSpace, Dicp_Call);
		soapObject.addProperty("args", args);

		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);

		HttpTransportSE transport = new HttpTransportSE(serviceURL);
			try {
				transport.call(null, envelope); // 连接服务器过程
				SoapPrimitive result= (SoapPrimitive) envelope.getResponse();
				
				return result.toString();
			} catch (IOException | XmlPullParserException e) {
				e.printStackTrace();
			}
		return null;
	}
}
