package com.goods.controller.business;
import com.goods.business.service.HealthService;
import com.goods.common.model.business.Health;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.JWTUtils;
import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * project:goods
 * package:com.goods.controller.business
 * class:HealthController
 *
 * @author: smile
 * @create: 2023/8/9-8:47
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/business/health")
public class HealthController {
    @Resource
    private HealthService healthService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:是否打卡
     */
    @GetMapping("isReport")
    public ResponseBean<?> isReport(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = JWTUtils.getUsername(token);
        HealthVO healthVO = healthService.isReport(username);
        return ResponseBean.success(healthVO);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:打卡
     */
    @PostMapping("report")
    public ResponseBean<?> report(@RequestBody Health health, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = JWTUtils.getUsername(token);
        healthService.report(health,username);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:签到记录
     */
    @GetMapping("history")
    public ResponseBean<?> history(@RequestParam Integer pageSize,
                                   @RequestParam Integer pageNum) {
        PageVO<Health> history = healthService.history(pageNum, pageSize);
        return ResponseBean.success(history);
    }
}
