package com.ticketsystem.core;


import com.ticketsystem.core.impl.TicketServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import com.ticketsystem.api.service.TicketService;

public class Activator implements BundleActivator {
    private ServiceRegistration<?> registration;

    @Override
    public void start(BundleContext context) throws Exception {
        TicketService service = new TicketServiceImpl();
        registration = context.registerService(
                TicketService.class.getName(),
                service,
                null
        );
        System.out.println("Ticket Service started and registered");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (registration != null) {
            registration.unregister();
        }
        System.out.println("Ticket Service stopped and unregistered");
    }
}