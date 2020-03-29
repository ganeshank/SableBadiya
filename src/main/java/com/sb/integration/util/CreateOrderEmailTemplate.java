package com.sb.integration.util;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.sb.integration.vo.CartDetails;

public class CreateOrderEmailTemplate {
	public static String getEmailContent(CartDetails cartDetails){
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        /*  next, get the Template  */
        Template t = ve.getTemplate("/create_order.vm");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("cartDetails", cartDetails);
        /*context.put("name", "World");*/
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        return writer.toString();   
	}
	
	public static String getOrderBillContent(CartDetails cartDetails){
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        /*  next, get the Template  */
        Template t = ve.getTemplate("/order_bill.vm");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("cartDetails", cartDetails);
        /*context.put("name", "World");*/
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        return writer.toString();   
	}
}
