import { useState, useEffect } from "react";
import Modal from "react-modal";
import { getUserPostMedia } from "../../services/APIService";
import { X } from "lucide-react";

Modal.setAppElement("#root");
export default function Media() {
    const [media, setMedia] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedVideo, setSelectedVideo] = useState(null);

    useEffect(() => {
        const storedUserId = localStorage.getItem("id");
        const fetchMedia = async () => {
            try {
                const response = await getUserPostMedia(storedUserId);
                if (response?.data?.response) {
                    setMedia(response.data.response);
                }
            } catch (error) {
                console.error("Error fetching media:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchMedia();
    }, []);

    const images = media.filter((item) => item.mediaType === 1);
    const videos = media.filter((item) => item.mediaType === 2);
    const openModal = (videoUrl) => {
        setSelectedVideo(videoUrl);
        setIsModalOpen(true);
    };
    const closeModal = () => {
        setSelectedVideo(null);
        setIsModalOpen(false);
    };

    if (loading) {
        return <p className="text-center">Loading media...</p>;
    }
    return (
        <div className="space-y-8">
            {/* Images area */}
            <div>
                <h2 className="text-lg font-semibold mb-4">Images</h2>
                {images.length === 0 ? (
                    <p className="text-center text-gray-500">No images available.</p>
                ) : (
                    <div className="grid grid-cols-3 gap-4">
                        {images.map((item) => (
                            <div key={item.id} className="relative">
                                <img
                                    src={item.mediaUrl}
                                    alt="Image"
                                    className="w-full h-48 object-cover rounded-lg"
                                />
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Videos area */}
            <div className="pb-10">
                <h2 className="text-lg font-semibold mb-4">Videos</h2>
                {videos.length === 0 ? (
                    <p className="text-center text-gray-500">No videos available.</p>
                ) : (
                    <div className="grid grid-cols-2 gap-4">
                        {videos.map((item) => (
                            <div
                                key={item.id}
                                className="relative cursor-pointer"
                                onClick={() => openModal(item.mediaUrl)}
                            >
                                {/* Video */}
                                <div className="w-full aspect-video bg-gray-800 rounded-lg overflow-hidden">
                                    <video
                                        className="w-full h-full object-contain"
                                        muted
                                        loop
                                    >
                                        <source src={item.mediaUrl} type="video/mp4" />
                                    </video>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Video Modal*/}
            <Modal
                isOpen={isModalOpen}
                onRequestClose={closeModal}
                contentLabel="Video Player"
                className="fixed inset-0 flex items-center justify-center z-50"
                overlayClassName="fixed inset-0 backdrop-blur-xs z-40"
            >
                <div className="relative bg-gray-900 rounded-lg p-4 max-w-7xl w-full max-h-80vh">
                    <button
                        onClick={closeModal}
                        className="absolute top-2 right-2 text-gray-600 hover:text-gray-400 transition duration-200 z-30"
                    >
                        <X size={30} />
                    </button>
                    {selectedVideo && (
                        <video
                            controls
                            autoPlay
                            className="w-full max-h-[75vh] rounded-lg"
                        >
                            <source src={selectedVideo} type="video/mp4" />
                        </video>
                    )}
                </div>
            </Modal>
        </div>
    );
}