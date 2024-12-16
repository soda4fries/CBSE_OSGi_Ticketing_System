# OSGi Ticket System using Apache Felix

This project implements a ticket management system using OSGi bundles with Apache Felix framework.  The `initializeProject.bat` and pom.xml can be modified to suit ur project
## Prerequisites

- JDK 11
- Maven 3.6+
- Apache Felix 7.0.5

## Project Structure

```
ticket-system-parent/
├── pom.xml
├── ticket-api/
│   ├── pom.xml
│   └── src/
├── ticket-core/
│   ├── pom.xml
│   └── src/
└── ticket-consumer/
    ├── pom.xml
    └── src/
```

## Setup Instructions
Powershell in windows can be used for wget.

1. Download and Extract Apache Felix:
```bash
# Create a directory for Felix
mkdir felix
cd felix

# Download Felix Framework
wget https://downloads.apache.org/felix/org.apache.felix.main.distribution-7.0.5.zip -o felix.zip

# Extract the archive
unzip org.apache.felix.main.distribution-7.0.5.zip
```

2. Build the Project:
```bash
# Navigate to project root
cd ticket-system-parent

# Build the parent project first
mvn clean install -N

# Build the API bundle
cd ticket-api
mvn clean install

# Build all modules
cd ..
mvn clean install
```

3. Start Felix:
```bash
cd /path/to/felix
java -jar bin/felix.jar
```

4. Install Bundles in Felix Console:
```
g! install file:/path/to/ticket-system-parent/ticket-api/target/ticket-api-1.0-SNAPSHOT.jar
g! install file:/path/to/ticket-system-parent/ticket-core/target/ticket-core-1.0-SNAPSHOT.jar
g! install file:/path/to/ticket-system-parent/ticket-consumer/target/ticket-consumer-1.0-SNAPSHOT.jar
```

5. Start the Bundles:
```
g! lb    # List bundles to see their IDs
g! start <bundle-id>    # Start each bundle using its ID
```

## Verifying Installation

1. Check Bundle Status:
```
g! lb
```
Expected output should show all bundles as ACTIVE:
```
START LEVEL 1
   ID|State      |Level|Name
    0|Active     |    0|System Bundle (7.0.5)
    1|Active     |    1|Ticket System API (1.0.0.SNAPSHOT)
    2|Active     |    1|Ticket System Core (1.0.0.SNAPSHOT)
    3|Active     |    1|Ticket System Consumer (1.0.0.SNAPSHOT)
```

2. Check Service Registration:
```
g! services
```
You should see the TicketService registered.

## Troubleshooting

1. If bundles fail to start:
```
g! diag <bundle-id>
```
This will show dependency issues or other problems.

2. To refresh bundles:
```
g! refresh <bundle-id>
```

3. Common Issues:

- **Bundle not found**: Verify the file path in the install command
- **Unresolved dependencies**: Check if all required bundles are installed and started
- **Version mismatch**: Ensure all bundles are using compatible versions
- **ClassNotFoundException**: Verify export/import package declarations in bundle manifests

4. To stop Felix:
```
g! stop 0
```

## Bundle Commands Reference

- List bundles: `lb`
- Install bundle: `install <path-to-jar>`
- Start bundle: `start <bundle-id>`
- Stop bundle: `stop <bundle-id>`
- Uninstall bundle: `uninstall <bundle-id>`
- Refresh bundles: `refresh`
- Show bundle headers: `headers <bundle-id>`
- Show service registry: `services`
- Show bundle dependencies: `diag <bundle-id>`

## Testing

The consumer bundle includes comprehensive tests that will run automatically when the bundle starts. You can observe the test results in the Felix console output.

## Development Tips

1. When making changes:
   - Rebuild the affected bundle
   - Uninstall the old version from Felix
   - Install and start the new version

2. For rapid development:
   ```bash
   mvn clean install && \
   felix:uninstall <bundle-id> && \
   felix:install file:/path/to/new/bundle.jar && \
   felix:start <bundle-id>
   ```

3. To enable debug logging, modify `conf/config.properties` in Felix directory:
   ```properties
   org.ops4j.pax.logging.DefaultServiceLog.level=DEBUG
   ```

## Additional Notes

- The system uses in-memory storage by default
- All timestamps are in system default timezone
- Reply trees are maintained in memory and will be lost on bundle restart
- Ticket IDs are UUIDs
- The default SLA for ticket resolution is 24 hours
