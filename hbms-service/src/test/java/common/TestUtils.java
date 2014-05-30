package common;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wangjinsi on 14-5-30.
 */
public abstract class TestUtils {


  protected static ClassPathXmlApplicationContext applicationContext = null;

  static {
    String path = "classpath:applicationContext.xml";
    applicationContext = new ClassPathXmlApplicationContext(path);
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

}
