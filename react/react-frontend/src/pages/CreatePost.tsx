import { useState } from "react";
import { useCreatePost } from "../hooks/useCreatePost";
import styles from "./CreatePost.module.css"
import { useScreen } from "../context/ScreenContext";

export default function () {

    const { setScreen } = useScreen();
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [imageUrl, setImageUrl] = useState("");
    const { createPost, loading, error } = useCreatePost();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const payload = { title, content, imageUrl: imageUrl || undefined };
        const result = await createPost(payload);

        if (result) {
            setScreen("profile")
        }
    };

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
                        id="imageUrl"
                        placeholder="Optional Image"
                        value={imageUrl}
                        onChange={(string) => setImageUrl(string.target.value)}
                    />

                    {error && <p> {error} </p>}

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        <p>{loading ? "Publishing..." : "Publish"}</p>
                    </button>
                </form>
            </div>
        </>
    )
}