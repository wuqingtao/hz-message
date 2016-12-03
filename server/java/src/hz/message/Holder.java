package hz.message;

/**
 * 实体类接口
 */
public interface Holder {
	/**
	 * 获取实体内部对象
	 * 
	 * @return 实体内部对象
	 */
	public Object inst();
	
	/**
	 * 销毁数据实体
	 * 
	 * @throws Exception 错误异常
	 */
	public void destroy() throws Exception;
	
	/**
	 * 关闭数据实体
	 * 
	 * @throws Exception 错误异常
	 */
	public void close() throws Exception;
}
