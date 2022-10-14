package com.powerledger.energyprovider.providers.services;

import com.powerledger.energyprovider.providers.models.BatteryInfo;
import com.powerledger.energyprovider.providers.models.Provider;
import com.powerledger.energyprovider.providers.payload.request.BatteryRequest;
import com.powerledger.energyprovider.providers.payload.request.PowerRangeRequest;
import com.powerledger.energyprovider.providers.payload.request.ProviderRequest;
import com.powerledger.energyprovider.providers.payload.response.ProviderResponse;
import com.powerledger.energyprovider.providers.payload.response.RangeResponse;
import com.powerledger.energyprovider.providers.repository.BatteryRepository;
import com.powerledger.energyprovider.providers.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Meena Shah
 */
@Service
@Transactional
@ApplicationScope
public class BatteryService {

  @Autowired
  ProviderRepository providerRepository;

  @Autowired
  BatteryRepository batteryRepository;

  public ProviderResponse createProviders(@Valid ProviderRequest providerRequest) {
      ProviderResponse providerResponse = new ProviderResponse();

      if (providerRepository.existsByProviderName(providerRequest.getProviderName())) {
          providerResponse.setMessage("Provider already exists");
          return providerResponse;
      }
      Provider providerData = Provider.builder().providerName(providerRequest.getProviderName()).build();

      Set<BatteryRequest> batteryInfos = providerRequest.getBatteryInfo();

      if (batteryInfos == null) {
          providerResponse.setMessage("Battery Information doesnot exists");
          return providerResponse;
      } else {
          batteryInfos.forEach(
             battery -> {
                var info =
                   BatteryInfo.builder()
                     .batteryName(battery.getBatteryName())
                     .postCode(battery.getPostCode())
                     .wattCapacity(battery.getWattCapacity())
                     .build();
                info.setProvider(providerData);
                batteryRepository.save(info);
          });
          providerRepository.save(providerData);
          ProviderResponse providerResponse1 = new ProviderResponse();
          providerResponse1.setMessage("Provider Created");

          return providerResponse1;
      }

  }

  public RangeResponse getRangeResponse(PowerRangeRequest powerRangeRequest){
      RangeResponse rangeResponse = new RangeResponse();
      Integer startRange = powerRangeRequest.getStartRange();
      Integer endRange = powerRangeRequest.getEndRange();
      if(startRange > endRange){
          throw new RuntimeException("Start range cannot be greater than end range");
      }

      var providerList = providerRepository.findAllProvider(startRange,endRange);
      var batteryList = batteryRepository.findAllBatteryInfo(startRange,endRange);

      if (providerList.size() > 0) {
          var totalCapacity =
                batteryList.stream().map(BatteryInfo::getWattCapacity).reduce(0, Integer::sum);
          var averageCapacity = totalCapacity / batteryList.size();
          rangeResponse.setAverageCapacity(averageCapacity);
          rangeResponse.setTotalCapacity(totalCapacity);
          Set<ProviderRequest> infos = new HashSet<>();

      providerList.forEach(
          pid -> {
            var batteryInfo = batteryRepository.findAllBatteryInfoById(startRange, endRange, pid);
            var provider = providerRepository.findById(pid);
            if (provider.isPresent()) {
              var prov =
                  ProviderRequest.builder().providerName(provider.get().getProviderName()).build();
              Set<BatteryRequest> batInfos = new HashSet<>();
              batteryInfo.stream()
                  .sorted()
                  .forEach(
                      bat -> {
                        var battery =
                            BatteryRequest.builder()
                                .batteryName(bat.getBatteryName())
                                .postCode(bat.getPostCode())
                                .wattCapacity(bat.getWattCapacity())
                                .build();
                        batInfos.add(battery);
                      });
              prov.setBatteryInfo(batInfos);

              infos.add(prov);
            }
          });
         rangeResponse.setProviderInfo(infos);
      }
      return rangeResponse;
  }
}



