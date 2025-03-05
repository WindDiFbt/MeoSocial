export default function Footer() {
    return (
        <div>
        <footer className="bg-gray-100 p-6 text-center text-gray-600 mt-10">
            <p>&copy; 2025 SocialApp. Tất cả các quyền được bảo lưu.</p>
            <div className="flex justify-center space-x-4 mt-2">
                <a href="#" className="hover:text-blue-600">Chính sách bảo mật</a>
                <a href="#" className="hover:text-blue-600">Điều khoản sử dụng</a>
                <a href="#" className="hover:text-blue-600">Liên hệ</a>
            </div>
        </footer>
        </div>
    );
}