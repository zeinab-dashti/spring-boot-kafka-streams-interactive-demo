@echo off

REM Set the base URL of the microservice
set BASE_URL_INSTANCE_ONE=http://localhost:8080/v1/vehicles
set BASE_URL_INSTANCE_TWO=http://localhost:8081/v1/vehicles
set PATH_VARIABLE_VEHICLE_ONE=vehicle1
set PATH_VARIABLE_VEHICLE_TWO=vehicle2


REM Query all vehicles from instance one
echo Query all vehicles from instance one...
curl -X GET %BASE_URL_INSTANCE_ONE% -H "Content-Type: application/json"
echo.


REM Query all vehicles from instance two
echo Query all vehicles from instance two...
curl -X GET %BASE_URL_INSTANCE_TWO% -H "Content-Type: application/json"
echo.


REM Query vehicle1 from instance one
echo Query vehicle1 from instance one...
curl -X GET %BASE_URL_INSTANCE_ONE%/%PATH_VARIABLE_VEHICLE_ONE% -H "Content-Type: application/json"
echo.


REM Query vehicle1 from instance two
echo Query vehicle1 from instance two...
curl -X GET %BASE_URL_INSTANCE_ONE%/%PATH_VARIABLE_VEHICLE_TWO% -H "Content-Type: application/json"
echo.


REM Query vehicle2 from instance one
echo Query vehicle2 from instance one...
curl -X GET %BASE_URL_INSTANCE_TWO%/%PATH_VARIABLE_VEHICLE_ONE% -H "Content-Type: application/json"
echo.


REM Query vehicle2 from instance two
echo Query vehicle2 from instance two...
curl -X GET %BASE_URL_INSTANCE_TWO%/%PATH_VARIABLE_VEHICLE_TWO% -H "Content-Type: application/json"
echo.
