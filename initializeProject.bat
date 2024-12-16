@echo off
echo Creating Ticket System Project Structure since I could not find a easy initalizer Script and Kinda annoying to setup using GUI all this nested folder in IDE

:: Create root directory
mkdir ticket-system-parent
cd ticket-system-parent

:: Create root pom.xml
echo. > pom.xml

:: Create API module
mkdir ticket-api
cd ticket-api
echo. > pom.xml
mkdir src\main\java\com\ticketsystem\api
mkdir src\main\java\com\ticketsystem\api\model
mkdir src\main\java\com\ticketsystem\api\service

:: Create model files
echo. > src\main\java\com\ticketsystem\api\model\Ticket.java
echo. > src\main\java\com\ticketsystem\api\model\Reply.java
echo. > src\main\java\com\ticketsystem\api\model\User.java

:: Create service interface
echo. > src\main\java\com\ticketsystem\api\service\TicketService.java

cd ..

:: Create Core module
mkdir ticket-core
cd ticket-core
echo. > pom.xml
mkdir src\main\java\com\ticketsystem\core
mkdir src\main\java\com\ticketsystem\core\impl

:: Create core files
echo. > src\main\java\com\ticketsystem\core\Activator.java
echo. > src\main\java\com\ticketsystem\core\impl\TicketServiceImpl.java

cd ..

:: Create Consumer module
mkdir ticket-consumer
cd ticket-consumer
echo. > pom.xml
mkdir src\main\java\com\ticketsystem\consumer

:: Create consumer files
echo. > src\main\java\com\ticketsystem\consumer\Activator.java
echo. > src\main\java\com\ticketsystem\consumer\TicketConsumer.java

echo Project structure created successfully!
