@echo off
echo ==========================================
echo   Subscription Tracker - Manual Launcher
echo ==========================================
echo.
echo [1/3] Cleaning previous build...
rmdir /s /q bin 2>nul
mkdir bin

echo [2/3] Compiling source code...
echo       (Using javac from system PATH)

dir /s /b src\main\java\*.java > sources.txt

javac -d bin -p "C:\Users\Othmane\.m2\repository\org\openjfx\javafx-controls\21\javafx-controls-21-win.jar;C:\Users\Othmane\.m2\repository\org\openjfx\javafx-graphics\21\javafx-graphics-21-win.jar;C:\Users\Othmane\.m2\repository\org\openjfx\javafx-base\21\javafx-base-21-win.jar;C:\Users\Othmane\.m2\repository\org\openjfx\javafx-fxml\21\javafx-fxml-21-win.jar;C:\Users\Othmane\.m2\repository\com\microsoft\sqlserver\mssql-jdbc\12.6.0.jre11\mssql-jdbc-12.6.0.jre11.jar;C:\Users\Othmane\.m2\repository\com\zaxxer\HikariCP\5.1.0\HikariCP-5.1.0.jar;C:\Users\Othmane\.m2\repository\org\slf4j\slf4j-api\1.7.36\slf4j-api-1.7.36.jar" @sources.txt

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo [3/3] Running Application...
echo.

java -p "bin;C:\Users\Othmane\.m2\repository\org\openjfx\javafx-controls\21\javafx-controls-21-win.jar;C:\Users\Othmane\.m2\repository\org\openjfx\javafx-graphics\21\javafx-graphics-21-win.jar;C:\Users\Othmane\.m2\repository\org\openjfx\javafx-base\21\javafx-base-21-win.jar;C:\Users\Othmane\.m2\repository\org\openjfx\javafx-fxml\21\javafx-fxml-21-win.jar;C:\Users\Othmane\.m2\repository\com\microsoft\sqlserver\mssql-jdbc\12.6.0.jre11\mssql-jdbc-12.6.0.jre11.jar;C:\Users\Othmane\.m2\repository\com\zaxxer\HikariCP\5.1.0\HikariCP-5.1.0.jar;C:\Users\Othmane\.m2\repository\org\slf4j\slf4j-api\1.7.36\slf4j-api-1.7.36.jar" -m com.emsi.subtracker/com.emsi.subtracker.Main

pause
