@echo off
echo ============================================================================
echo   COMPILATION GLOBALE ET PACKAGING JAR - TP5 RMI (LSI 3)
echo   Etudiant : Salmen Zekri - FSS
echo ============================================================================

set BASE_DIR=%~dp0SalmenZekri_LSI3_DevAppReparties

echo.
echo [1/6] Compilation de RMIConversion_Server...
cd /d "%BASE_DIR%\RMIConversion_Server"
javac -encoding UTF-8 -d bin src\rmiService\*.java src\rmiServer\*.java
if %ERRORLEVEL% neq 0 ( echo ERREUR ! & pause & exit /b %ERRORLEVEL% )

echo [2/6] Creation du package client conversionClient.jar...
cd /d "%BASE_DIR%\RMIConversion_Server\bin"
jar -cvf "%BASE_DIR%\conversionClient.jar" rmiService\IConversion.class
copy "%BASE_DIR%\conversionClient.jar" "%BASE_DIR%\RMIConversion_Client\lib\conversionClient.jar" /Y >nul

echo [3/6] Compilation de RMIConversion_Client...
cd /d "%BASE_DIR%\RMIConversion_Client"
javac -encoding UTF-8 -cp "lib\conversionClient.jar" -d bin src\rmiClient\ConversionClient.java
if %ERRORLEVEL% neq 0 ( echo ERREUR ! & pause & exit /b %ERRORLEVEL% )

echo [4/6] Compilation de RMIBanque_Server...
cd /d "%BASE_DIR%\RMIBanque_Server"
javac -encoding UTF-8 -d bin src\metier\*.java src\rmiService\*.java src\rmiServer\*.java
copy "src\jndi.properties" "bin\jndi.properties" /Y >nul
if %ERRORLEVEL% neq 0 ( echo ERREUR ! & pause & exit /b %ERRORLEVEL% )

echo [5/6] Creation du package client banqueClient.jar...
cd /d "%BASE_DIR%\RMIBanque_Server\bin"
jar -cvf "%BASE_DIR%\banqueClient.jar" rmiService\IBanque.class metier\Compte.class
copy "%BASE_DIR%\banqueClient.jar" "%BASE_DIR%\RMIBanque_Client\lib\banqueClient.jar" /Y >nul

echo [6/6] Compilation de RMIBanque_Client...
cd /d "%BASE_DIR%\RMIBanque_Client"
javac -encoding UTF-8 -cp "lib\banqueClient.jar" -d bin src\rmiClient\BanqueClient.java src\rmiClient\BanqueClientJNDI.java
copy "src\jndi.properties" "bin\jndi.properties" /Y >nul
if %ERRORLEVEL% neq 0 ( echo ERREUR ! & pause & exit /b %ERRORLEVEL% )

echo.
echo ============================================================================
echo   COMPILATION ET EXPORTATION DES JARS REUSSIES AVEC SUCCES !
echo ============================================================================
pause
