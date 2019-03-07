package car.guangqi.Exeception;
/**
 *  通用异常类,将所有异常包装成该类抛出，便于做统一处理
 * @author tom
 * @date 2016年10月21日
 */
public class CommonException  extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CommonException(String errorMessage,
			Throwable cause) {
		super(errorMessage, cause);
	}
	public CommonException(Throwable cause) {
		super( cause);
	}
	public CommonException(String errorMessage) {
		super( errorMessage);
	}  
}
