package com.diploma.ccms.domain;

import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Messages.class)
public class MessagesIntegrationTest {

	@Test
	public void testMarkerMethod() {
		Messages message = new Messages();
		message.setTitle("Test1");
		message.persist();
		System.out.println("Count messages before: "
				+ message.countMessageses());
		
		message.setTitle("Test2");
		message.persist();
		System.out.println("Count messages before: "
				+ message.countMessageses());

	}
}
