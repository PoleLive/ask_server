package cn.lawliex.ask;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AskApplication.class)
@WebAppConfiguration
public class AskApplicationTests {
	public static String jsonStr = "{\"msg\":\"登录成功\",\"code\":0,\"user\":{\"id\":1,\"name\":\"lawliex\",\"password\":\"123\",\"salt\":\"5b3cd\"}}";
	@Test
	public void contextLoads() {
		Gson gson = new Gson();
		BaseResponse response = gson.fromJson(jsonStr,BaseResponse.class);
		System.out.println(response.getData());
	}

}
