package com.yahoo.athenz.zts.utils;

import org.testng.annotations.Test;

import static org.testng.Assert.fail;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

public class IPBlockTest {

    @Test
    public void testInvalidIPBlock() {
        
        try {
            new IPBlock("10.1.1.1");
            fail();
        } catch (IllegalArgumentException ex) {
        }
        
        try {
            new IPBlock("10.1.1.1%255.255.255.255");
            fail();
        } catch (IllegalArgumentException ex) {
        }
        
        try {
            new IPBlock("10.1.1.1-255.255.255.0");
            fail();
        } catch (IllegalArgumentException ex) {
        }
        
        try {
            new IPBlock("10.1.1.256/255.255.255.0");
            fail();
        } catch (IllegalArgumentException ex) {
        }
        
        try {
            new IPBlock("10.1.1.0/255.265.255.0");
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testIpCheck() {
        
        // subnet/netmask: 10.1.0.1/255.255.255.255
        // address range: 10.1.0.1
        
        IPBlock ipBlock = new IPBlock("10.1.0.1/255.255.255.255");
        assertTrue(ipBlock.ipCheck(IPBlock.convertToLong("10.1.0.1")));
        assertFalse(ipBlock.ipCheck(IPBlock.convertToLong("10.1.0.2")));
        
        // subnet/netmask: 10.1.0.0/255.255.248.0
        // address range: 10.1.0.0 - 10.1.7.255
        
        ipBlock = new IPBlock("10.1.0.0/255.255.248.0");
        assertTrue(ipBlock.ipCheck(IPBlock.convertToLong("10.1.0.0")));
        assertTrue(ipBlock.ipCheck(IPBlock.convertToLong("10.1.7.255")));
        assertTrue(ipBlock.ipCheck(IPBlock.convertToLong("10.1.3.25")));
        assertTrue(ipBlock.ipCheck(IPBlock.convertToLong("10.1.0.24")));
        assertFalse(ipBlock.ipCheck(IPBlock.convertToLong("10.1.8.0")));
        assertFalse(ipBlock.ipCheck(IPBlock.convertToLong("10.0.0.0")));
        assertFalse(ipBlock.ipCheck(IPBlock.convertToLong("10.2.0.0")));
        assertFalse(ipBlock.ipCheck(IPBlock.convertToLong("10.2.1.255")));
    }
    
    @Test
    public void testIpCheckWithSpaces() {
        
        // subnet/netmask: 10.1.0.1/255.255.255.255
        // address range: 10.1.0.1
        
        IPBlock ipBlock = new IPBlock("10.1.0.1 / 255.255.255.255 ");
        assertTrue(ipBlock.ipCheck(IPBlock.convertToLong("10.1.0.1")));
        assertFalse(ipBlock.ipCheck(IPBlock.convertToLong("10.1.0.2")));
    }
    
    @Test
    public void testIpCheckInvalidIPs() {
        
        IPBlock ipBlock = new IPBlock("10.3.0.1/255.255.255.255");
        try {
            ipBlock.ipCheck(IPBlock.convertToLong("10.1987.0.1"));
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            ipBlock.ipCheck(IPBlock.convertToLong("10.0.0.256"));
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
}
