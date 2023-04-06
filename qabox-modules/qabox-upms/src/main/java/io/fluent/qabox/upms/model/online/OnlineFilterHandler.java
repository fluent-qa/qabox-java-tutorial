package io.fluent.qabox.upms.model.online;

import io.fluent.qabox.config.prop.BoxProp;
import io.fluent.qabox.exception.ApiError;
import io.fluent.qabox.frontend.fun.FilterHandler;
import io.fluent.qabox.upms.constant.SessionKey;
import io.fluent.qabox.view.BoxApiModel;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class OnlineFilterHandler implements FilterHandler {

  @Resource
  private BoxProp eruptProp;

  @Resource
  private StringRedisTemplate stringRedisTemplate;

  @Override
  public String filter(String condition, String[] params) {
    if (eruptProp.isRedisSession()) {
      Set<String> keys = stringRedisTemplate.keys(SessionKey.USER_TOKEN + "*");
      if (keys != null && keys.size() > 0) {
        StringBuilder sb = new StringBuilder(UserOnline.class.getSimpleName() + ".token in (");
        keys.forEach(it -> sb.append("'").append(it.substring(SessionKey.USER_TOKEN.length())).append("',"));
        return sb.substring(0, sb.length() - 1) + ")";
      }
      return "1 = 2";
    }
    throw new ApiError(BoxApiModel.Status.INFO,
      "Enable the RedisSession configuration to use this feature", BoxApiModel.PromptWay.NOTIFY);
  }
}
