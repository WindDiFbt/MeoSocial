import React from "react";
import "./index.css"

export default function LandingPage() {
  return (
    <div className="font-sans bg-gray-100 text-gray-900">
      <header className="bg-blue-600 text-white text-center py-16">
        <h1 className="text-5xl font-bold">Mạng Xã Hội Meo Social</h1>
        <p className="mt-4 text-lg">Kết nối, chia sẻ và khám phá thế giới xung quanh bạn</p>
        <button className="mt-6 px-6 py-3 bg-white text-blue-600 font-semibold rounded-lg shadow-lg hover:bg-gray-200">
          Tham gia ngay
        </button>
      </header>

      <section className="py-16 text-center">
        <h2 className="text-3xl font-bold">Mục tiêu của chúng tôi</h2>
        <p className="mt-4 text-lg">Xây dựng một cộng đồng an toàn và thân thiện cho tất cả mọi người.</p>
      </section>

      <section className="bg-white py-16 text-center">
        <h2 className="text-3xl font-bold">Tính năng nổi bật</h2>
        <div className="mt-8 flex justify-center gap-6">
          <div className="p-6 bg-gray-200 rounded-lg shadow-lg max-w-sm">
            <h3 className="text-xl font-semibold">Tương tác</h3>
            <p className="mt-2 text-gray-600">Đăng bài, chia sẻ bài viết và tương tác với hàng nghìn người trên thế giới.</p>
          </div>
          <div className="p-6 bg-gray-200 rounded-lg shadow-lg max-w-sm">
            <h3 className="text-xl font-semibold">Bảng tin cá nhân</h3>
            <p className="mt-2 text-gray-600">Chia sẻ bài viết, hình ảnh và video.</p>
          </div>
          <div className="p-6 bg-gray-200 rounded-lg shadow-lg max-w-sm">
            <h3 className="text-xl font-semibold">Bảo mật cao</h3>
            <p className="mt-2 text-gray-600">Mọi dữ liệu của bạn đều được bảo vệ.</p>
          </div>
        </div>
      </section>

      <section className="py-16 text-center bg-gray-100">
        <h2 className="text-3xl font-bold">Phản hồi từ người dùng</h2>
        <p className="mt-4 text-lg italic">"Tôi rất thích mạng xã hội này! Rất dễ sử dụng và thân thiện."</p>
        <p className="mt-2 font-semibold">- Nguyễn Văn A</p>
      </section>

      <section className="py-16 text-center bg-gray-100">
        <p className="mt-4 text-lg italic">"Mạng xã hội này thật tuyệt vời"</p>
        <p className="mt-2 font-semibold">- Nguyễn Văn B</p>
      </section>

      <section className="bg-blue-600 text-white text-center py-16">
        <h2 className="text-3xl font-bold">Sẵn sàng tham gia?</h2>
        <button className="mt-6 px-6 py-3 bg-white text-blue-600 font-semibold rounded-lg shadow-lg hover:bg-gray-200">
          Đăng ký ngay
        </button>
      </section>
    </div>
  );
}
