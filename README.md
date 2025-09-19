<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
        🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>

<h2 align="center">
    UNG DUNG ĐAT VE XEM PHIM
</h2>

<div align="center">
    <!-- Logo -->
    <p>
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/fitdnu_logo.png" alt="FIT DNU Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="180"/>
    </p>

   <!-- Badge -->
   <p>
        <a href="https://www.facebook.com/DNUAIoTLab">
            <img src="https://img.shields.io/badge/AIoTLab-green?style=for-the-badge" alt="AIoTLab"/>
        </a>
        <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
            <img src="https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge" alt="Faculty of Information Technology"/>
        </a>
        <a href="https://dainam.edu.vn">
            <img src="https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge" alt="DaiNam University"/>
        </a>
   </p>
</div>


1. Giới thiệu hệ thống
   Movie Ticket Booking System là hệ thống đặt vé xem phim trực tuyến được phát triển bằng Java, sử dụng mô hình client-server. Hệ thống cho phép người dùng xem danh sách phim, đặt vé, quản lý vé đã đặt, đồng thời cung cấp chức năng        quản lý cho admin để thêm phim mới vào hệ thống.

   Tính năng chính:

   - Đăng ký/Đăng nhập tài khoản

   - Xem danh sách phim với thông tin chi tiết

   - Đặt vé và chọn chỗ ngồi

   - Xem lịch sử vé đã đặt

   - Quản lý phim (cho admin)

   - Lưu trữ dữ liệu trong file text

   - Hỗ trợ đa người dùng

2. Công nghệ sử dụng
   🛠️ Ngôn ngữ và Framework
   Java SE (Java Standard Edition)

   Java Swing (GUI)

   Java Socket Programming (Network communication)

   📊 Kiến trúc hệ thống
   Mô hình Client-Server

   TCP/IP Protocol (Port 12345)

   Object Serialization cho truyền dữ liệu

   Multi-threading cho xử lý đa nhiệm

   💾 Lưu trữ dữ liệu
   File-based storage (Text files)

   CSV format cho dữ liệu

   Thư mục data/ chứa:

   users.txt - Tài khoản người dùng

   movies.txt - Thông tin phim

   tickets.txt - Lịch sử đặt vé

3. Hình ảnh các chức năng
   🖼️ Giao diện đăng nhập
   <img width="972" height="715" alt="image" src="https://github.com/user-attachments/assets/ed9d41ad-c14a-4507-84d5-3dade89c1af6" />

   🎪 Trang chủ - Danh sách phim
   <img width="963" height="724" alt="image" src="https://github.com/user-attachments/assets/0b87439d-bbe5-48fb-b27a-834f3d4427d2" />
    
   🎫 Đặt vé
   <img width="976" height="683" alt="image" src="https://github.com/user-attachments/assets/4b52bdb4-cd68-4871-ae07-def01e1504ec" />


   👤 Quản lý vé đã đặt
   <img width="967" height="649" alt="image" src="https://github.com/user-attachments/assets/b0114ffb-733e-4dff-9bfd-6ada2945ff91" />


   🎬 Thêm phim mới (Admin)
   <img width="959" height="695" alt="image" src="https://github.com/user-attachments/assets/a0eff6c1-13cb-4cb7-8426-6158c97cbb07" />

4. Các bước cài đặt
   📋 Yêu cầu hệ thống

   Java JDK 8 hoặc cao hơn

    Windows / Linux / macOS

   🔧 Cài đặt và chạy ứng dụng

   Bước 1: Tải source code
   git clone <repository-url>
   cd movie-booking-system


 
   Bước 2: Tạo thư mục và file dữ liệu
   mkdir data
   Tạo file data/users.txt với nội dung mẫu:
   admin,admin123,true
   user1,password123,false
   user2,password123,false
   Tạo file data/movies.txt với nội dung mẫu:
   1,Avengers: Endgame,The epic conclusion,12.5,1672531200000,CGV Vincom,100,100
   2,Spider-Man: No Way Home,Spider-Man seeks help,11.0,1672617600000,Lotte Cinema,80,80



 
   Bước 3: Biên dịch và chạy
   👉 Chạy bằng file batch (Windows):
   compile_and_run.bat
   👉 Hoặc chạy thủ công:
   # Biên dịch
   javac -d bin src/*/*.java src/*/*/*.java


   # Mở một terminal chạy server
   java -cp bin server.MovieServer


   # Mở terminal khác chạy client GUI
   java -cp bin client.ClientGUI



   Bước 4: Đăng nhập

   Tài khoản admin mặc định: admin / admin123

   Hoặc bạn có thể đăng ký tài khoản mới trong giao diện client.



5. Thông tin liên hệ
   👨‍💻 Tác giả
   NGUYEN DUC NGOC
   🎓 Student of [DNU University]
   📧 Email: ducngocnguyen004.email@example.com
   🔗 GitHub: ducngocnguyen004
   📞 Hỗ trợ kỹ thuật
   Nếu bạn gặp bất kỳ vấn đề nào với hệ thống, vui lòng:
   Kiểm tra lại các bước cài đặt
   Đảm bảo port 12345 không bị block
   Liên hệ qua email hoặc tạo issue trên GitHub
