# OSGi Ticket System using Apache Felix
This project implements a ticket management system using OSGi bundles with Apache Felix framework. It uses a modular design with a core bundle that exports the API and a consumer bundle that demonstrates usage.

## Prerequisites
- JDK 11
- Maven 3.6+
- Apache Felix 7.0.5

## Project Structure
```
ticket-system-parent/
├── pom.xml
├── ticket-core/
│   ├── pom.xml
│   └── src/
│       └── main/java/
│           ├── com/ticketsystem/api/
│           │   ├── model/
│           │   │   ├── Ticket.java
│           │   │   ├── Reply.java
│           │   │   └── User.java
│           │   └── service/
│           │       └── TicketService.java
│           └── com/ticketsystem/core/
│               ├── impl/
│               │   └── TicketServiceImpl.java
│               └── Activator.java
└── ticket-consumer/
    ├── pom.xml
    └── src/
        └── main/java/com/ticketsystem/consumer/
            ├── TicketConsumer.java
            └── Activator.java
```

## Bundle Structure
1. **Core Bundle (ticket-core)**
   - Exports API packages:
     - `com.ticketsystem.api.model`
     - `com.ticketsystem.api.service`
   - Private implementation package:
     - `com.ticketsystem.core.impl`
   - Contains service interfaces, models, and implementation

2. **Consumer Bundle (ticket-consumer)**
   - Imports API packages from core bundle
   - Demonstrates service usage
   - Includes test suite

## Setup Instructions
1. Download and Extract Apache Felix:
```bash
mkdir felix
cd felix
# For Windows PowerShell
Invoke-WebRequest -Uri https://downloads.apache.org/felix/org.apache.felix.main.distribution-7.0.5.zip -OutFile felix.zip
Expand-Archive felix.zip -DestinationPath .

# For Linux/Mac
wget https://downloads.apache.org/felix/org.apache.felix.main.distribution-7.0.5.zip
unzip org.apache.felix.main.distribution-7.0.5.zip
```

2. Build the Project:
```bash
cd ticket-system-parent
mvn clean install
```

3. Start Felix:
```bash
cd /path/to/felix
java -jar bin/felix.jar
```

4. Install Bundles in Felix Console:
```
g! install file:/path/to/ticket-system-parent/ticket-core/target/ticket-core-1.0-SNAPSHOT.jar
g! install file:/path/to/ticket-system-parent/ticket-consumer/target/ticket-consumer-1.0-SNAPSHOT.jar
```

## Verifying Installation
1. Check Bundle Status:
```
g! lb
```
Expected output:
```
START LEVEL 1
   ID|State      |Level|Name
    0|Active     |    0|System Bundle (7.0.5)
    1|Active     |    1|Ticket System Core (1.0.0.SNAPSHOT)
    2|Active     |    1|Ticket System Consumer (1.0.0.SNAPSHOT)
```

2. Check Service Registration:
```
g! services
```

## Features
- Ticket Creation and Management
- Hierarchical Reply System
- Status Tracking (Open, In Progress, Resolved)
- Department-based Organization
- SLA Monitoring (24-hour resolution time)
- Advanced Search Functionality
- Comprehensive Test Suite

## Development Workflow
1. Making Changes:
```bash
# After modifying code
mvn clean install             # Build all modules
```

2. Hot Reloading in Felix:
```
g! update <bundle-id>         # Update an existing bundle
g! refresh <bundle-id>        # Refresh dependencies
```

## Troubleshooting
1. Bundle Status Check:
```
g! lb    # List bundles
g! diag <bundle-id>    # Diagnose issues
```

2. Common Issues:
- Bundle State Issues: Use `refresh` command
- Service Not Found: Verify core bundle is started first
- Class Not Found: Check package exports/imports in core bundle

## Technical Details
- Data Storage: In-memory (non-persistent)
- ID Generation: UUID-based
- Thread Safety: ConcurrentHashMap for storage
- Reply Structure: Tree-based with parent-child relationships
- SLA Tracking: 24-hour resolution time
- Search: Case-insensitive content matching
- Department Recognition: Based on username prefix (e.g., "it.user", "hr.user")

## Bundle Commands Reference
```
g! lb                  # List bundles
g! start <bundle-id>   # Start bundle
g! stop <bundle-id>    # Stop bundle
g! headers <bundle-id> # Show bundle headers
g! services            # List services
g! help                # Show all commands
```

## Testing
The consumer bundle runs a comprehensive test suite on startup that verifies:
- Ticket CRUD operations
- Reply tree management
- Status filtering
- Assignee filtering
- Department grouping
- Search functionality
- Concurrent operations

Test results are displayed in the Felix console when the consumer bundle starts.
