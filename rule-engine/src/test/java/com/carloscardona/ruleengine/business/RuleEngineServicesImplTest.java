package com.carloscardona.ruleengine.business;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-RuleEngine-test.xml" })
public class RuleEngineServicesImplTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngineServicesImplTest.class);

	@Autowired
	private RuleEngineServices ruleEngineServices;

	@Before
	public void setup() {
		LOGGER.info("setup jUnit");
		Security.setProperty("ssl.SocketFactory.provider", "com.ibm.jsse2.SSLSocketFactoryImpl");
		Security.setProperty("ssl.ServerSocketFactory.provider", "com.ibm.jsse2.SSLServerSocketFactoryImpl");
	}

	@Test
	public void testInstancia() {
		Assert.assertNotNull(this.ruleEngineServices);
	}

	@Test
	public void testRegla() {
		List<Object> object = new ArrayList<>();
		this.ruleEngineServices.ejecutar(object);
		Assert.assertNotEquals(null, object);
	}
}