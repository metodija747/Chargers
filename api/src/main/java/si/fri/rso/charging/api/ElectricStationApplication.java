package si.fri.rso.charging.api;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(title = "Charger details API1", version = "v1", contact = @Contact(email = "mb8696@student.uni-lj.si"), license = @License(name="dev"), description = "API for managing chargers."), servers = @Server(url ="http://20.127.141.29/charge"))
@ApplicationPath("/v1")
public class ElectricStationApplication extends Application {
}
