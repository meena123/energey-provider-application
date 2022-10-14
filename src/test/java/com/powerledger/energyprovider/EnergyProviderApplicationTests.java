package com.powerledger.energyprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerledger.energyprovider.providers.models.BatteryInfo;
import com.powerledger.energyprovider.providers.models.Provider;
import com.powerledger.energyprovider.providers.payload.request.BatteryRequest;
import com.powerledger.energyprovider.providers.payload.request.ProviderRequest;
import com.powerledger.energyprovider.providers.payload.response.ProviderResponse;
import com.powerledger.energyprovider.providers.repository.BatteryRepository;
import com.powerledger.energyprovider.providers.repository.ProviderRepository;
import com.powerledger.energyprovider.providers.services.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = EnergyProviderApplication.class
)

@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class EnergyProviderApplicationTests {
	@InjectMocks // This allows to inject Mock objects.
	BatteryService batteryService;

	@Autowired
	MockMvc mockMvc ;

	@Mock
	ProviderRepository providerRepository ;

	@Mock
	BatteryRepository batteryRepository;
	Provider providerInfo;
	@BeforeEach
	void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		providerInfo =  Provider.builder()
				.providerName("PowerLedger")
				.build();
		Set<BatteryInfo> batterys = new HashSet<>();

		var battery1 = BatteryInfo.builder()
				.batteryName("Automatic")
				.postCode("123456")
				.wattCapacity(2000).build();

		var battery2 = BatteryInfo.builder()
				.batteryName("Manual")
				.postCode("9809")
				.wattCapacity(5000).build();
		batterys.add(battery1);
		batterys.add(battery2);
		providerInfo.setBatteryInfo(batterys);

	}
	@Test
	void contextLoads() throws Exception {
	}
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void addBatteries() {
		var providers = getProvidersInformation();
		ProviderResponse response = batteryService.createProviders(providers);
		providerRepository.save(providerInfo);
		assertNotNull(response.getMessage());
	}
	private ProviderRequest getProvidersInformation() {
		var providers =  ProviderRequest.builder()
				.providerName("PowerLedger")
				.build();
		Set<BatteryRequest> batterys = new HashSet<>();

		var battery1 = BatteryRequest.builder()
				.batteryName("Automatic")
				.postCode("123456")
				.wattCapacity(2000).build();

		var battery2 = BatteryRequest.builder()
				.batteryName("Manual")
				.postCode("9809")
				.wattCapacity(5000).build();
		batterys.add(battery1);
		batterys.add(battery2);
		providers.setBatteryInfo(batterys);
		return providers;
	}

}
