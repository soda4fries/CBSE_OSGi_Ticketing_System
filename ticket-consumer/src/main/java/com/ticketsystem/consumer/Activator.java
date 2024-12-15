package com.ticketsystem.consumer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import com.ticketsystem.api.service.TicketService;

public class Activator implements BundleActivator {
    private ServiceReference ticketServiceReference;

    @Override
    public void start(BundleContext context) throws Exception {
        ServiceReference ticketServiceReference = context.getServiceReference(TicketService.class.getName());
        if (ticketServiceReference != null) {
            TicketService ticketService = (TicketService) context.getService(ticketServiceReference);
            TicketConsumer consumer = new TicketConsumer(ticketService);

            // Run all tests including new ones
            consumer.runAllTests();

            // Demonstrate all features
            consumer.demonstrateAllFeatures();
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (ticketServiceReference != null) {
            context.ungetService(ticketServiceReference);
        }
    }
}