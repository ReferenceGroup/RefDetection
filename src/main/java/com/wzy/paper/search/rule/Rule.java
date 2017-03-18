package com.wzy.paper.search.rule;
/**
 * 规则类
 * @author Dell
 *
 */
public class Rule
{
	/**
	 * 链接
	 */
	private String url;

	/**
	 * 参数集合
	 */
	private String[] params;
	/**
	 * 参数对应的值
	 */
	private String[] values;

	/**
	 *GET / POST
	 * 请求的类型，默认GET
	 */
	private int requestMoethod = GET ; 
	
	public final static int GET = 0 ;
	public final static int POST = 1 ;
	

	public Rule(String url, String[] params, String[] values,
			 int requestMoethod)
	{
		super();
		this.url = url;
		this.params = params;
		this.values = values;
		this.requestMoethod = requestMoethod;
	}

	
	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String[] getParams() {
		return params;
	}


	public void setParams(String[] params) {
		this.params = params;
	}


	public String[] getValues() {
		return values;
	}


	public void setValues(String[] values) {
		this.values = values;
	}


	public int getRequestMoethod() {
		return requestMoethod;
	}


	public void setRequestMoethod(int requestMoethod) {
		this.requestMoethod = requestMoethod;
	}


	public static int getGet() {
		return GET;
	}


	public static int getPost() {
		return POST;
	}


}
