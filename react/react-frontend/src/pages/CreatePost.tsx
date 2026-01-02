import { useEffect, useState } from "react";
import { useCreatePost } from "../hooks/useCreatePost";
import styles from "./CreatePost.module.css"
import { useScreen } from "../context/ScreenContext";
import { uploadImageUtil } from "../util/uploadImageUtil";

export default function () {

    const { setScreen } = useScreen();
    const { createPost, loading, error } = useCreatePost();

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [imageFile, setImageFile] = useState<File | null>(null);
    const [imagePreview, setImagePreview] = useState<string | null>(null);
    const [uploading, setUploading] = useState(false);


    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            setUploading(true);

            let uploadedImageUrl: string | undefined;

            if (imageFile) {
                uploadedImageUrl = await uploadImageUtil(
                    imageFile,
                    "temp-user-id" // TODO REPLACE
                );
            }

            const payload = { title, content, imageUrl: uploadedImageUrl || undefined };
            const result = await createPost(payload);

            if (result) {
                setScreen("profile")
            }
        } catch (err: any) {
            console.error("Failed to create post", err)
        } finally {
            setUploading(false);
        }
    };

    useEffect(() => {
        return () => {
            if (imagePreview) {
                URL.revokeObjectURL(imagePreview);
            }
        };
    }, [imagePreview]);

    return (
        <>
            <div className={styles.formContainer}>
                <h2>Create Post</h2>
                <form className={styles.formInput}
                    onSubmit={handleSubmit}>
                    <input
                        id="title"
                        placeholder="Title"
                        value={title}
                        onChange={(string) => setTitle(string.target.value)}
                    />
                    <textarea
                        id="content"
                        name="content"
                        placeholder="Text"
                        value={content}
                        onChange={(string) => setContent(string.target.value)}
                        rows={10}
                        required
                    />
                    <input
                        type="file"
                        accept="image/*"
                        onChange={(e) => {
                            const file = e.target.files?.[0];
                            if (!file) return;

                            setImageFile(file);
                            setImagePreview(URL.createObjectURL(file));
                        }}
                    />

                    {imagePreview && (
                        <img src={imagePreview}
                            alt="preview"
                        />
                    )}

                    {error && <p> {error} </p>}

                    <button
                        type="submit"
                        disabled={loading || uploading}
                    >
                        <p>{uploading ? "Uploading image..." : loading ? "Publishing..." : "Publish"}</p>
                    </button>
                </form>
            </div>
        </>
    )
}