package com.example.gracefulstartup;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author hushengbin
 * @date 2023-06-28 14:29
 */
@Component
public class GracefulStartupListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GracefulStartupListener.class);

    @Resource
    private GracefulStartupProperties gracefulStartupProperties;

    @Async
    @EventListener
    public void onInstanceRegisteredEvent(InstanceRegisteredEvent<NacosDiscoveryProperties> event)
            throws NacosException {
        if (!gracefulStartupProperties.isEnabled()) {
            return;
        }

        if (gracefulStartupProperties.getIncreaseWeight() <= 0) {
            LOGGER.warn("InstanceRegisteredEvent graceful, increase weight[{}] is less than 0", gracefulStartupProperties.getIncreaseWeight());
        }

        NacosDiscoveryProperties properties = event.getConfig();
        Instance instance = this.getInstance(properties);
        NamingService namingService = properties.namingServiceInstance();

        BigDecimal targetWeight = BigDecimal.ONE;
        BigDecimal newWeight = BigDecimal.ZERO;
        BigDecimal increaseWeight = BigDecimal.valueOf(gracefulStartupProperties.getIncreaseWeight());
        while (newWeight.compareTo(targetWeight) < 0) {
            newWeight = newWeight.add(increaseWeight);
            if (newWeight.compareTo(targetWeight) > 0) {
                newWeight = targetWeight;
            }
            instance.setWeight(newWeight.doubleValue());
            namingService.registerInstance(properties.getService(), properties.getGroup(), instance);
            LOGGER.info("InstanceRegisteredEvent graceful, weight is set to {}", newWeight);

            try {
                Thread.sleep(gracefulStartupProperties.getEachStepSleepSecond() * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Instance getInstance(NacosDiscoveryProperties nacosDiscoveryProperties) {
        Instance instance = new Instance();
        instance.setIp(nacosDiscoveryProperties.getIp());
        instance.setPort(nacosDiscoveryProperties.getPort());
        instance.setMetadata(nacosDiscoveryProperties.getMetadata());
        instance.setWeight(nacosDiscoveryProperties.getWeight());
        instance.setClusterName(nacosDiscoveryProperties.getClusterName());
        instance.setEnabled(nacosDiscoveryProperties.isInstanceEnabled());
        instance.setEphemeral(nacosDiscoveryProperties.isEphemeral());
        return instance;
    }
}
