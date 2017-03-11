package example.HttpClient.ZhiHu;

/**
 * Created by zhuang on 2017/3/12.
 */


import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 作者 ：chenzenan E-mail:chen.ze.nan@163.com
 * @version 创建时间：2012-8-10 下午04:35:48 Copyright ? 2012-8-10 Shanghai XXX Co.
 *          Ltd. All right reserved.
 */
public class LoginDemo {

    public static void main(String[] args) throws IOException {
        login();
    }

    public static void login() throws IOException {
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

        HttpGet getHomePage = new HttpGet("http://www.ZhiHu.com/");
        try {
            //填充登陆请求中基本的参数
            CloseableHttpResponse response = httpClient.execute(getHomePage);
            String responseHtml = EntityUtils.toString(response.getEntity());
            String xsrfValue = responseHtml.split("<input type=\"hidden\" name=\"_xsrf\" value=\"")[1].split("\"/>")[0];
            System.out.println("_xsrf:" + xsrfValue);
            response.close();
            List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
            valuePairs.add(new BasicNameValuePair("_xsrf" , xsrfValue));
//            valuePairs.add(new BasicNameValuePair("email", 用户名));
            valuePairs.add(new BasicNameValuePair("phone_num", "17181710917"));
            valuePairs.add(new BasicNameValuePair("password", "886pkxiaojiba"));
            valuePairs.add(new BasicNameValuePair("rememberme", "true"));

            //获取验证码
//            HttpGet getCaptcha = new HttpGet("http://www.zhihu.com/captcha.gif?r=" + System.currentTimeMillis() + "&type=login");
//            CloseableHttpResponse imageResponse = httpClient.execute(getCaptcha);
//            FileOutputStream out = new FileOutputStream("/tmp/ZhiHu.gif");
//            byte[] bytes = new byte[8192];
//            int len;
//            while ((len = imageResponse.getEntity().getContent().read(bytes)) != -1) {
//                out.write(bytes,0,len);
//            }
//            out.close();
//            Runtime.getRuntime().exec("eog /tmp/ZhiHu.gif");//ubuntu下看图片的命令是eog
//
//            //请用户输入验证码
//            System.out.print("请输入验证码：");
//            Scanner scanner = new Scanner(System.in);
//            String captcha = scanner.next();
//            valuePairs.add(new BasicNameValuePair("captcha", captcha));

            //完成登陆请求的构造
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
            HttpPost post = new HttpPost("https://www.ZhiHu.com/login/phone_num");
            post.setEntity(entity);
            httpClient.execute(post);//登录

            HttpGet g = new HttpGet("http://www.ZhiHu.com/question/following");//获取“我关注的问题”页面
            CloseableHttpResponse r = httpClient.execute(g);
            System.out.println(EntityUtils.toString(r.getEntity()));
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}