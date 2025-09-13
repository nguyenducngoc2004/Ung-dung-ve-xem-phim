@echo off
echo Compiling Movie Booking System...

REM Tạo thư mục output nếu chưa tồn tại
if not exist bin mkdir bin

REM Biên dịch tất cả file Java
javac -d bin -cp . src/client/*.java src/server/*.java src/model/*.java src/utils/*.java

if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!

REM Chạy server trong cửa sổ mới
start "Movie Server" cmd /k "java -cp bin server.MovieServer"

REM Đợi server khởi động
timeout /t 3 /nobreak

REM Chạy client trong cửa sổ mới
start "Movie Client" cmd /k "java -cp bin client.ClientGUI"

echo Server and Client started!
pause