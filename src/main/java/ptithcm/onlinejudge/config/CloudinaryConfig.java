package ptithcm.onlinejudge.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CloudinaryConfig {
    CloudinaryConfig() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ptithcm-online-judge",
                "api_key", "783318887615188",
                "api_secret", "mtlllUU21BT1Y23m6SfF8FY1nG0"
        ));
        SingletonManager manager = new SingletonManager();
        manager.setCloudinary(cloudinary);
        manager.init();
    }
}
