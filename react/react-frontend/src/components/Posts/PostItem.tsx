import { useState } from "react";
import { Post } from "../../types/Post";
import { useImageInput } from "../../hooks/useImageInput";
import { uploadImageUtil } from "../../util/uploadImageUtil";
import { ImageInputWrapper } from "./ImageInputWrapper";
import styles from "./PostItem.module.css"
import { PostMenu } from "./PostMenu";
import { useAuth } from "../../context/AuthContext";
import { useAutoResizeTextarea } from "../../hooks/useAutoResizeTextarea";

type Props = {
    post: Post;
    onEdit: (post: Post) => void;
    onDelete: (id: string) => void;
    showAuthor?: boolean;
}

export default function PostItem({ post, onEdit, onDelete, showAuthor }: Props) {

    const [isEditing, setIsEditing] = useState(false);
    const [title, setTitle] = useState(post.title);
    const [content, setContent] = useState(post.content);

    const { user } = useAuth();
    const { imageFile, imagePreview, shouldRemoveImage, selectImage, removeImage, resetImage } = useImageInput(post.imageUrl);
    const { ref: contentRef } = useAutoResizeTextarea(content);

    const handleSave = async () => {

        let imageUrl = post.imageUrl;

        if (imageFile) {
            imageUrl = await uploadImageUtil(imageFile);
        }

        if (shouldRemoveImage) {
            imageUrl = null;
        }

        onEdit({ ...post, title, content, imageUrl });
        setIsEditing(false);
    }

    const handleCancel = () => {
        setTitle(post.title);
        setContent(post.content);
        resetImage();
        setIsEditing(false);
    }

    return (
        <li className={styles.postLayout}>
            {isEditing ? (
                <ImageInputWrapper onFileSelect={selectImage}>

                    <input
                        type="text"
                        name="edit-title"
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                        placeholder="Title"
                    />
                    <textarea
                        ref={contentRef}
                        value={content}
                        name="edit-content"
                        onChange={e => setContent(e.target.value)}
                        placeholder="Content"
                    />

                    {imagePreview && (
                        <div className={styles.imagePreviewWrapper}>
                            <img src={imagePreview} alt="Preview" className={styles.imagePreview} />
                            <button type="button" onClick={removeImage} className={styles.removeImageButton}>
                                <img src="/assets/cross.svg" alt="" />
                            </button>
                        </div>
                    )}

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

                    <button className={styles.saveButton}
                        onClick={handleSave}>Save
                    </button>

                    <button className={styles.cancelButton}
                        onClick={handleCancel}>Cancel
                    </button>

                </ImageInputWrapper>
            ) : (
                <>
                    <h3>{post.title}</h3>
                    <p>{post.content}</p>

                    {post.imageUrl && (
                        <img
                            src={post.imageUrl}
                            alt={post.title}
                        />
                    )}

                    <small>
                        {showAuthor ? `By ${post.authorUsername} · ` : ""}
                        {new Date(post.createdDate).toLocaleString()}
                    </small>

                    {user && post.isOwner && (
                        <PostMenu
                            onEdit={() => setIsEditing(true)}
                            onDelete={() => onDelete?.(post.id)}
                        />
                    )}
                </>
            )}
        </li>
    )
}