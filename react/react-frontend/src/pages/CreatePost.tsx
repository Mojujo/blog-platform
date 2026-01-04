import { useState } from "react";
import { useCreatePost } from "../hooks/useCreatePost";
import styles from "./CreatePost.module.css"
import { useScreen } from "../context/ScreenContext";
import { uploadImageUtil } from "../util/uploadImageUtil";
import { ImageInputWrapper } from "../components/Posts/ImageInputWrapper";
import { useImageInput } from "../hooks/useImageInput";

export default function () {

    const { setScreen } = useScreen();
    const { createPost, loading, error } = useCreatePost();
    const { imageFile, imagePreview, selectImage, removeImage } = useImageInput();

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [uploading, setUploading] = useState(false);


    // TODO Extract create post into component / hook to reuse in PostItem and create post page
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            setUploading(true);

            let uploadedImageUrl: string | undefined;

            if (imageFile) {
                uploadedImageUrl = await uploadImageUtil(imageFile);
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

    return (
        <>
            <ImageInputWrapper onFileSelect={selectImage}>
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
                            if (file) {
                                selectImage(file);
                            }
                        }}
                    />

                    {imagePreview && (
                        <>
                            <img src={imagePreview}
                                alt="preview"
                            />
                            <button type="button" onClick={removeImage}>Remove image</button>
                        </>

                    )}

                    {error && <p> {error} </p>}

                    <button
                        type="submit"
                        disabled={loading || uploading}
                    >
                        <p>{uploading ? "Uploading image..." : loading ? "Publishing..." : "Publish"}</p>
                    </button>
                </form>
            </ImageInputWrapper>
        </>
    )
}